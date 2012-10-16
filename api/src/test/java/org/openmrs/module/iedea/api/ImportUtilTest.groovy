package org.openmrs.module.iedea.api

import static org.junit.Assert.*;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.junit.Test;
import org.openmrs.api.context.Context;
import org.openmrs.EncounterType;
import org.openmrs.module.iedea.ImportUtil;

class ImportUtilTest extends BaseModuleContextSensitiveTest {
    
    @Test
    public void testCreateIedeaEncounters() {
        def encServ = Context.getEncounterService();
        def allEncTypes = encServ.getAllEncounterTypes();
        assertEquals("In the demo data set we should be starting with 3 enctypes",
            allEncTypes.size(), 3);
        
        ImportUtil importUtil = new ImportUtil()
        // Create any encounter types that aren't in the system yet.
        importUtil.initializeIedeaEncounterTypes()

        allEncTypes = encServ.getAllEncounterTypes()
        assertEquals("We should have added 1 new type with the current test data",
            allEncTypes.size(), 4)
        
        def iedeaEncounterTypes = importUtil.getIedeaEncounterTypes()
        assertEquals(iedeaEncounterTypes.size(), 1)

        def facesInitialEncounterType = importUtil.getIedeaEncounterType("facesInitial")
        assertEquals(facesInitialEncounterType.getName(), "CCSPInitialForm")
        
    }

}
