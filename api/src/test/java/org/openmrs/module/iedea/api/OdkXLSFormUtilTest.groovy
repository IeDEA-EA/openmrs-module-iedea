package org.openmrs.module.iedea.api

import static org.junit.Assert.*;
import org.openmrs.module.iedea.OdkXLSFormUtil
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.junit.Test;
import org.openmrs.api.context.Context;

public class OdkXLSFormUtilTest extends BaseModuleContextSensitiveTest {

    /*
     @Test
     public void testXSLFormOpenMRSMetadata() {
     def togo = [
     "l603": [
     ["coded",7487,1115],
     ["coded",7487,7507],
     ["coded",7487,7508]
     ]
     ]
     def odk = new OdkXLSFormUtil()
     def totest = odk.processExport()
     assertEquals(togo, totest)
     }
     */

    @Test
    public void testGetColumnMapping() {
        def odk = new OdkXLSFormUtil()
        def wb = odk.openWorkbook("classpath:///CCSPS08_ElectronicInitialVisitForm_v7.xls")
        //def wb = odk.openWorkbook("file:///home/sgithens/code/openmrs-module-iedea/api/src/test/resources/CCSPS08_ElectronicInitialVisitForm_v7.xls")
        def totest = odk.getExcelColumnMapping(wb)
        assertEquals(totest['openmrs:type'],0)
        assertEquals(totest['openmrs:mapping'],1)
        assertEquals(totest['name'],4)
    }
    /* TODO fix test
    @Test
    public void testEntireWorkfow() {
        def encServ = Context.getEncounterService()
        def odk = new OdkXLSFormUtil()
        def rows = odk.parseAggregateExport("classpath:///importfiles/CCSPInitialForm_v7_results.csv")
        assertEquals(rows.size(), 2)
        odk.sendOdkImportRowsToOpenMRS(rows);
    }
    */

    @Test
    public void testBasicPatientCreate() {
        def odk = new OdkXLSFormUtil()
        def patientServ = Context.getPatientService()
        // Not sure what query I have to use for getCountOfPatients to just
        // count them all.
        //def count = patientServ.getCountOfPatients("")
        def count = patientServ.getAllPatients().size()
        assertEquals("The demo data set starts with 4 patients.", count, 4)
        def newPatient = odk.createPatient()
        count = patientServ.getAllPatients().size()
        assertEquals("We've added 1 new patient.", count, 5)
    }
}