/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you m3ay not use this file except in
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

import org.openmrs.api.OpenmrsService;
import org.openmrs.module.iedea.api.db.IeDEAEastAfricaDAO;
import org.springframework.transaction.annotation.Transactional;

/**
 * This service exposes module's core functionality. It is a Spring managed bean which is configured in moduleApplicationContext.xml.
 * <p>
 * It can be accessed only via Context:<br>
 * <code>
 * Context.getService(IeDEAEastAfricaService.class).someMethod();
 * </code>
 * 
 * @see org.openmrs.api.context.Context
 */
@Transactional
    public interface IeDEAEastAfricaService extends OpenmrsService {
    
    public String getImportLogDir();
	
    /*
     * Add service methods here
     * 
     */
    public IeDEAEastAfricaDAO getDao();

    /**
     * Process the files recorded as being part of the FacesODKImport and return
     * the number of *new* records entered. (Does not include duplicates 
     * already in the database.
     * @return Number of new records added to the database.
     */
    public int importLoadingDockFiles();
	
    /**
     * Import a CSV export Concept Dictionary
     */
    public void importConceptDictionaryCSV();
}