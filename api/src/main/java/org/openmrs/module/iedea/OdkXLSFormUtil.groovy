package org.openmrs.module.iedea

import au.com.bytecode.opencsv.CSVReader
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.openmrs.Patient
import org.openmrs.Encounter
import org.openmrs.PatientIdentifier
import org.openmrs.Location
import org.openmrs.Obs
import org.openmrs.api.context.Context
import org.openmrs.module.iedea.api.IeDEAEastAfricaService
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory


public class OdkImportRow {
    def uuid, patient 
    def obs = []

    public OdkImportRow(uuid) {
        this.uuid = uuid
    }
}


/**
 * This class will read an Excel XLSForm of the schema that is used by the
 * XLSForm project to generate XForms capable of being loaded on to a device 
 * running ODK Collect.
 * 
 * In our spreadsheet we are adding custom columns to map various form questions
 * to OpenMRS concepts to automate the import of ODK Aggregate exports into 
 * OpenMRS. 
 *
 */
public class OdkXLSFormUtil {    
    def DEBUG_BLANK_ENCOUNTER_UUIDS = true
    
    def log = LogFactory.getLog(this.getClass());
    def iedeaService;

    public OdkXLSFormUtil() {
        iedeaService = Context.getService(IeDEAEastAfricaService.class)
    }

    /**
     * Parse a string such as:
     * 
     * 1 7487:1115 2 7487:7507 3 7487:7508
     * 
     * and return a map like:
     * 
     * {
     *   "1": [7487,1115],
     *   "2": [7487,7507],
     *   "3": [7487,7508]
     * }
     *   
     * @param mapping
     * @return
     */
    def parseCodedMapping(String mapping) {
        def togo = [:]
        def parts = mapping.split()
        if (parts.size() % 2 != 0) {
            throw new IllegalArgumentException("Values need to be paired.")
        }
        for (int i = 0; i < parts.size()-1; i+=2) {
            togo[parts[i]] = parts[i+1].split(":")
        }
        return togo
    }

    /**
     * Using the first row, returns a mapping of column names to their
     * integer index.
     * 
     * @return Map with Column Name -> Integer 
     */
    def getExcelColumnMapping(workbook) {
        def togo = [:]
        def sheet = workbook.getSheet("survey")
        def firstRow = sheet.getRow(0)
        firstRow.eachWithIndex { cell, idx ->
            togo[cell.getStringCellValue()] = idx
        }
        return togo
    }

    def Workbook openWorkbook(xlsFileName) {
        def xlsFileNameStatic = "/home/sgithens/code/openmrs-module-iedea/api/src/test/resources/CCSPS08_ElectronicInitialVisitForm_v7.xls"
        def workbook = new HSSFWorkbook(new FileInputStream(xlsFileNameStatic));
        return workbook
    }

    public Patient createPatient() {
        def locationServ = Context.getLocationService()
        def patientServ = Context.getPatientService()
        def pLocation = new Location()
        pLocation.name = "My Home"
        def savedLocation = locationServ.saveLocation(pLocation)
        def pIdentType = patientServ.getPatientIdentifierType(2)
        def pIdent = new PatientIdentifier("newidentifer", pIdentType, savedLocation)
        
        def p = new Patient()
        p.addIdentifier(pIdent)
        p.gender = "F"
        patientServ.savePatient(p)
        return p
    }
    
    public Encounter createEncounter(patient,encType,provider=1) {
        def personServ = Context.getPersonService()
        def providerObj = personServ.getPerson(provider)
        def encounterServ = Context.getEncounterService()
        def togo = new Encounter()
        togo.encounterDatetime = new Date()
        togo.patient = patient
        togo.encounterType = encType
        togo.provider = providerObj
        encounterServ.saveEncounter(togo)
        return togo
    }
    
    /*
     * This method should have no side effects. 
     * 
     * @param val The value parameter is the raw value that is contained in the
     * cell on the row we're currently doing from the ODK Aggregate Export.
     * 
     * @param xlsformInfo A map describing the metadata necessary for this 
     * particular item. The keys are named after the header rows in the XLSForm
     * specification.
     * 
     * @param odkEncounter The OdkEncounter object we're building up to 
     * eventually do the import into OpenMRS. This object will ultimately be
     * passed in to create the patient (if necessary), and the encounter and all
     * it's observations that the row from ODK Aggregate represents.
     */
    def turnOdkAggregateCellIntoObservation(val, xlsformInfo, odkEncounter) {
        def type = xlsformInfo["openmrs:type"]
        def codedMapping = xlsformInfo["openmrs:mapping"] 
        if (type == "patient_identifier") {
            odkEncounter.patient = val
        }
        else if (type == "coded") {
            def mapping = parseCodedMapping(codedMapping)
            odkEncounter.obs.push((["coded"] << mapping[val]).flatten())
        }
    }
    
    /*
     * Given a single OdkImportRow, send this to OpenMRS as a new Encounter,
     * using OpenMRS API's to do all the work.
     * 
     * If the patient doesn't yet exist, we need to create that patient as well.
     * Then we create a new Encounter, and add all the Observations to it.
     *
     * This will do the following: use the UUID as the encounter uuid in 
     * OpenMRS. Then we will look at the patient in the OdkImportRow and see
     * if they exist. If not we'll create another one. This will need to be a 
     * pluggable mechanism since it is likely to work differently on different
     * versions of OpenMRS. 
     * 
     * After that we will iterate through the obs list importing them according
     * to their specifications. So far we support the following specs:
     * 
     * Coded Observation:
     *   If the obs is a list and the first member is the word "coded", we will
     *   import this as a coded observation. The second member of the array will
     *   be the question concept, and the third member will be the answer 
     *   concept.
     *   example: [coded, 7487, 7508]
     * 
     * @param odkImportRow The row from the ODK Aggregate export to import.
     */
    def sendOdkImportRowToOpenMRS(OdkImportRow odkImportRow) {
        def conceptServ = Context.getConceptService()
        def obsServ = Context.getObsService()
        def locServ = Context.getLocationService()
        def location = locServ.getLocation(1)
        
        def importUtil = new ImportUtil()
        
        // First make sure we've got the encounter types loaded in.
        // We may want to move to this to the bulk version of this code (rather
        // than checking every time).
        importUtil.initializeIedeaEncounterTypes()
        
        // TODO Properly create a patient if they do not exist
        //def patient = createPatient()
        def patientServ = Context.getPatientService()
        def patient = patientServ.getPatients(odkImportRow.patient)[0]
        
        def encType = importUtil.getIedeaEncounterType("facesInitial")
        
        def encounter = createEncounter(patient,encType)
        
        if (!DEBUG_BLANK_ENCOUNTER_UUIDS) {
            encounter.setUuid(odkImportRow.uuid)
        }
        encounter.setLocation(location)
        
        odkImportRow.obs.eachWithIndex { val, idx ->
            if (val instanceof java.util.List && val[0] == "coded") {
                def question = conceptServ.getConcept(val[1])
                def answer = conceptServ.getConcept(val[2])
                // Get a proper location
                def obs = new Obs(patient, question, new Date(), location)
                obs.setValueCoded(answer)
                obsServ.saveObs(obs, "New Obs")
                encounter.addObs(obs)
            }
        }
        def encServ = Context.getEncounterService()
        encServ.saveEncounter(encounter);
        
    }
    
    public void sendOdkImportRowsToOpenMRS(rows) {
        rows.each {
            sendOdkImportRowToOpenMRS(it)
        }
    }

    /**
     * This will process an export from a single ODK Aggregate export. Returning
     * a map where each item is one of the encounter rows by it's ODK UUID, and
     * the list is a list of operations.
     * 
     * @return
     */
    def List<OdkImportRow> parseAggregateExport(filename=null,xlsformFilename=null) {
        //
        // Build Up Metadata Mapping from Excel File
        //
        def togo = []
        def workbook = openWorkbook()
        def colMappings = getExcelColumnMapping(workbook)
        def sheet = workbook.getSheet("survey")
        def aggregateHeaderMapping = [:]
        sheet.rowIterator().eachWithIndex { row, rowIdx ->
            def omrsTypeCell = row.getCell(colMappings["openmrs:type"])
            def omrsMappingCell = row.getCell(colMappings["openmrs:mapping"])
            def odkNameCell = row.getCell(colMappings["name"])
            def omrsType = ""
            if (omrsTypeCell != null) { 
                omrsType = omrsTypeCell.getStringCellValue()
            }
            def omrsMapping = ""
            if (omrsMappingCell != null) {
                omrsMapping = omrsMappingCell.getStringCellValue()
            }
            if (odkNameCell != null) {
                aggregateHeaderMapping[odkNameCell.getStringCellValue()] = 
                ["openmrs:type":omrsType,
                "openmrs:mapping":omrsMapping] 
            }
        }

        //
        // Open Aggregate *.csv Export and process
        //
        if (filename == null) {
            filename = "/home/sgithens/code/openmrs-module-iedea/api/src/test/resources/importfiles/CCSPInitialForm_v7_results.csv"
        }
        
        def reader = new CSVReader(new FileReader(filename))
        def String[] headerLine = reader.readNext()
        def String[] nextLine
        
        def csvColMap = [:]
        headerLine.eachWithIndex { cell, idx -> csvColMap[cell] = idx }
                
        while((nextLine = reader.readNext()) != null) {
            def uuid = nextLine[csvColMap["meta:instanceID"]]
            uuid = uuid[5..uuid.length()-1]
            def odkEncounter = new OdkImportRow(uuid)
            nextLine.eachWithIndex { val, idx ->
                def curCsvHeader = headerLine[idx]
                def xlsformInfo = aggregateHeaderMapping[curCsvHeader];
                if (xlsformInfo != null) {                    
                    turnOdkAggregateCellIntoObservation(val, xlsformInfo, odkEncounter)
                }
            }
            togo.push(odkEncounter)
        }
        
        return togo
    }
    
    public void runOdkImportForOneFile(String filepath) {
        def rows = parseAggregateExport(filepath)
        sendOdkImportRowsToOpenMRS(rows);
    }
    
    public void runEntireImportProcess() {
        def rows = parseAggregateExport()
        sendOdkImportRowsToOpenMRS(rows);
    }
}
