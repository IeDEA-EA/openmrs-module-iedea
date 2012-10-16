package org.openmrs.module.iedea

import au.com.bytecode.opencsv.CSVReader
import org.openmrs.Concept
import org.openmrs.ConceptName
import org.openmrs.ConceptDescription
import org.openmrs.api.context.Context
import java.util.Locale

/**
 * Utility to import dictionary concepts from the csv import. 
 * 
 * @author sgithens
 */
public class DictionaryCSVImport {

    /**
     * The headers we get from the csv are:
     * 0 Concept Id
     * 1 Name
     * 2 Description
     * 3 Synonyms
     * 4 Answers
     * 5 Set Members
     * 6 Class
     * 7 Datatype
     * 8 Changed By
     * 9 Creator
     1,"ANEMIA, BLOOD LOSS","Anemia due to bleeding or a hemorrhagic process.","ANEMIA, BLOOD LOSS","","","Diagnosis","N/A","Super User","Super User"
     * @return
     */
    public void importFromCSV (String filePath) {
        def conService = Context.getConceptService()

        if (filePath == null) {
            filePath = "/home/sgithens/code/openmrs-module-iedea/api/src/test/resources/defaultConceptDictionary1.8.3.csv"
        }
        def reader = new CSVReader(new FileReader(filePath))
        def String [] headerLine = reader.readNext()
        def String [] nextLine
        while ((nextLine = reader.readNext()) != null) {
            println nextLine
            def id = nextLine[0].toInteger()
            if (conService.getConcept(id) != null) {
                continue
            }
            if (conService.getConceptByName(nextLine[1])) {
                continue
            }
            def concept = new Concept()
            concept.conceptId = id
            concept.addName(new ConceptName(nextLine[1],Locale.US))
            concept.addDescription(new ConceptDescription(nextLine[2],Locale.US))
            def dataType = conService.getConceptDatatypeByName(nextLine[7])
            concept.setDatatype(dataType)
            def conClass = conService.getConceptClassByName(nextLine[6])
            concept.setConceptClass(conClass)
            conService.saveConcept(concept)
        }
    }

    // TODO Unit Tests
    public void purgeAllConcepts() {
        def conceptServ = Context.getConceptService()
        def concepts = conceptServ.getAllConcepts()
        concepts.each {
            conceptServ.purgeConcept(it)
        }
    }
}
