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
package org.openmrs.module.iedea.api.impl;

import org.openmrs.api.context.Context;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.module.iedea.DictionaryCSVImport;
import org.openmrs.module.iedea.IeDEAConstants;
import org.openmrs.module.iedea.api.IeDEAEastAfricaService;
import org.openmrs.module.iedea.api.db.IeDEAEastAfricaDAO;

/**
 * It is a default implementation of {@link IeDEAEastAfricaService}.
 */
public class IeDEAEastAfricaServiceImpl extends BaseOpenmrsService implements IeDEAEastAfricaService {

    protected final Log log = LogFactory.getLog(this.getClass());

    private IeDEAEastAfricaDAO dao;

    /**
     * @param dao the dao to set
     */
    public void setDao(IeDEAEastAfricaDAO dao) {
        this.dao = dao;
    }

    /**
     * @return the dao
     */
    public IeDEAEastAfricaDAO getDao() {
        return dao;
    }

    public String getImportLogDir() {
        return Context.getAdministrationService().getGlobalProperty(
                                                                    IeDEAConstants.IMPORT_FILE_DIR_GP);
    }

    @Override
        public int importLoadingDockFiles() {
        // 1. Look up the physical files we need to import
        // 2. For each file, run it through parser according to it's spec
        // 3. 
        return 0;
    }

    @Override
        public void importConceptDictionaryCSV() {
        //DictionaryCSVImport dictImport = new DictionaryCSVImport();
        //dictImport.importFromCSV();
    }


}