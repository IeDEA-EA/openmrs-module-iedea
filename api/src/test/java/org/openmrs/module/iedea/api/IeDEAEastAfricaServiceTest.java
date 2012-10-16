/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.iedea.api;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.openmrs.api.context.Context;
import org.openmrs.module.iedea.ConfigParser;
import org.openmrs.module.iedea.IeDEAConstants;
import org.openmrs.module.iedea.ImportLogItem;
import org.openmrs.module.iedea.ImportUtil;
import org.openmrs.test.BaseModuleContextSensitiveTest;

/**
 * Tests {@link ${IeDEAEastAfricaService}}.
 */
public class  IeDEAEastAfricaServiceTest extends BaseModuleContextSensitiveTest {
	
    private IeDEAEastAfricaService getIeDEA() {
        return Context.getService(IeDEAEastAfricaService.class);
    }
	
    @Test
	public void shouldSetupContext() {
        assertNotNull(Context.getService(IeDEAEastAfricaService.class));
    }
	
    @Test
	public void testLogImportDao() {
        assertEquals("Initial size of table should be zero.", 
                     getIeDEA().getDao().getImportLogCount(), 0);
        ImportLogItem log = new ImportLogItem("12345", "IN PROGRESS", "Ok");
        getIeDEA().getDao().saveOrUpdate(log);
        assertEquals("Inserted 1 row.", getIeDEA().getDao().getImportLogCount(), 1);
    }
	
    private String getTestImportDirectory() {
        return "/home/sgithens/code/openmrs-module-iedea/api/src/test/resources/importfiles";
    }
	
    @Test
	public void testImportLogDir() {
        String dir = getTestImportDirectory();
        Context.getAdministrationService().setGlobalProperty(
                                                             IeDEAConstants.IMPORT_FILE_DIR_GP, 
                                                             dir);
        String logFileDir = getIeDEA().getImportLogDir();
        assertEquals(dir,logFileDir);
    }
	
    @Test
	public void testMappingConfigFetch() {
        ConfigParser cp = new ConfigParser();
        Map config = (Map) cp.getConfig("facesInitial");
        assertEquals("Check type", "csv", config.get("type"));
    }
	
    @Test
	public void testLoadingDockFiles() {
        ConfigParser cp = new ConfigParser();
        ImportUtil util = new ImportUtil();
        Map<String,List> m = util.getImportFilesByType(getTestImportDirectory(),cp.getImportFileSpecs());
        assertEquals(m.size(), 1);
        assertTrue(m.containsKey("facesInitial"));
        assertTrue(m.get("facesInitial").contains("CCSPInitialForm_v7_results.csv"));
        assertTrue(m.get("facesInitial").contains("CCSPInitialForm_v7_results2.csv"));
    }
	
    /**
     * Here we'll test a full iteration of importing the files from the 
     * configured directory.
     */
    @Test
	public void testProcessODKImportDirectory() {
        assertEquals("Initial size of table should be zero.", 
                     getIeDEA().getDao().getImportLogCount(), 0);
        int numNewRecords = getIeDEA().importLoadingDockFiles();
        assertEquals(numNewRecords, 2);
        // The initial small data set is in 2 files, where one file is a 
        // duplicate record, so it should only be imported once.
        assertEquals("Size after import should be 2.", 
                     getIeDEA().getDao().getImportLogCount(), 2);
		
    }
}
