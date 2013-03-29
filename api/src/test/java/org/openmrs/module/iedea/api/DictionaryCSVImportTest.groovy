package org.openmrs.module.iedea.api

import static org.junit.Assert.*;
import org.junit.Test;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.openmrs.api.context.Context;
import org.openmrs.module.iedea.DictionaryCSVImport;

/*
 Typical Built in concepts from the test data
 3 : COUGH SYRUP
 4 : CIVIL STATUS
 5 : SINGLE
 6 : MARRIED
 7 : YES
 8 : NO
 9 : HIV PROGRAM
 10 : MDR-TB PROGRAM
 11 : MALARIA PROGRAM
 12 : TREATMENT STATUS
 13 : ANTIRETROVIRAL TREATMENT GROUP
 14 : FOLLOWING
 16 : DIED
 17 : WAITING FOR RESULTS
 18 : FOOD ASSISTANCE
 19 : FAVORITE FOOD, NON-CODED
 20 : DATE OF FOOD ASSISTANCE
 21 : FOOD ASSISTANCE FOR ENTIRE FAMILY
 22 : UNKNOWN
 23 : FOOD CONSTRUCT
 24 : null
 88 : ASPIRIN
 792 : STAVUDINE LAMIVUDINE AND NEVIRAPINE
 5089 : WEIGHT (KG)
 5497 : CD4 COUNT
 */
class DictionaryCSVImportTest extends BaseModuleContextSensitiveTest {

    @Test
    public void testConcepts() { /*
        def conService = Context.getConceptService()
        def concepts = conService.getAllConcepts()
        concepts.each { concept ->
            println "${concept.id} : ${concept.name}"
        }
        assertEquals(25, concepts.size())

        def dictImport = new DictionaryCSVImport()
        dictImport.importFromCSV("classpath:///defaultConceptDictionary1.8.3.csv");
           
        def moreConcepts = conService.getAllConcepts()
        assertTrue(moreConcepts.size() > 100)

        def skindis = conService.getConcept(79)
        assertEquals("SKIN DISORDERS",skindis.getName().getName()) */
    }

}
