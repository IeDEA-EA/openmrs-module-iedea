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
package org.openmrs.module.iedea.web.controller;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.iedea.api.IeDEAEastAfricaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.view.RedirectView;
import org.openmrs.module.iedea.ConfigParser;
import org.openmrs.module.iedea.DictionaryCSVImport;
import org.openmrs.module.iedea.ImportUtil;
import org.openmrs.module.iedea.OdkXLSFormUtil;

/**
 * The main controller.
 */
@Controller
@SessionAttributes("loadingDockImportResults")
public class  IeDEAEastAfricaManageController {

    protected final Log log = LogFactory.getLog(getClass());

    @RequestMapping(value = "/module/iedea/loadingdock", method = RequestMethod.GET)
    public void loadingDock(ModelMap model){
        IeDEAEastAfricaService serv = Context.getService(IeDEAEastAfricaService.class);
        model.addAttribute("loadingDockDirectory", serv.getImportLogDir());
        ImportUtil importUtil = new ImportUtil();
        Map<String,List> importFiles = importUtil.getImportFilesByType();
        model.addAttribute("importFiles", importFiles);		
    }

    @RequestMapping(value = "/module/iedea/checkPatientsOnImport.form", method=RequestMethod.POST)
    public RedirectView checkPatientsOnImport(ModelMap model) {
        RedirectView togo = new RedirectView("/openmrs/module/iedea/loadingdock.form");
        model.addAttribute("loadingDockImportResults", "This is totally what happened!");
        return togo;
    }

    @RequestMapping(value = "/module/iedea/manage", method = RequestMethod.GET)
    public void manage(ModelMap model) {
        model.addAttribute("conceptCount", Context.getConceptService().getAllConcepts().size());
        model.addAttribute("user", Context.getAuthenticatedUser());

        ConfigParser cp = new ConfigParser();
        cp.getEntireConfig();
    }

    @RequestMapping(value = "/module/iedea/importConceptDictionaryCSV",
            method = RequestMethod.POST)
    public String importConceptDictionaryCSV(ModelMap model) {
        IeDEAEastAfricaService serv = Context.getService(IeDEAEastAfricaService.class);
        serv.importConceptDictionaryCSV();
        return "redirect:/module/iedea/manage.form";
    }

    @RequestMapping(value = "/module/iedea/dictionaryOperation",
            method = RequestMethod.POST)
    public RedirectView dictionaryOperation(
            @RequestParam("op") String op,
            @RequestParam("importCsvPath") String importCsvPath,
            Model model,
            HttpSession session
            ) {
        DictionaryCSVImport dict = new DictionaryCSVImport();

        if (op.equals("purgeDictOp")) {
            dict.purgeAllConcepts()
        } 
        else if (op.equals("importCsvOp")) {
            dict.importFromCSV("file://${importCsvPath}")
        }
        else if (op.equals("buildAmpathDictionary")) {
            dict.purgeAllConcepts()
            dict.importFromCSV("classpath:///iedea-dictionaries/ampath-original-conceptDictionary161012_954.csv")
        }
        else if (op.equals("buildFacesDictionary")) {
            dict.purgeAllConcepts()
            dict.importFromCSV("classpath:///iedea-dictionaries/faces-original-conceptDictionary28912_1116.csv")
        }
        else {
            System.out.println("IeDEA: Unknown Operation");
        }

        RedirectView rv = new RedirectView("/openmrs/module/iedea/manage.form");
        return rv;
    }

    @RequestMapping(value = "/module/iedea/entireWorkflowTest.form",
            method = RequestMethod.POST)
    public RedirectView entireWorkflowTest(Model model) {
        System.out.println("About to test the entire workflow import");
        OdkXLSFormUtil odk = new OdkXLSFormUtil();
        odk.runEntireImportProcess();
        RedirectView rv = new RedirectView("/openmrs/module/iedea/manage.form");
        return rv;	    
    }
    
    @RequestMapping(value = "/module/iedea/odkImportOperation.form",
            method = RequestMethod.POST)
    public RedirectView odkImportOperation(Model model,
        @RequestParam("odkCsvExportFilePath") String odkCsvExportFilePath) {
        System.out.println("ODK SIngle file export: " + odkCsvExportFilePath);
        OdkXLSFormUtil odk = new OdkXLSFormUtil();
        odk.runOdkImportForOneFile(odkCsvExportFilePath);
        System.out.println("Done with ODK Single file export.");
        RedirectView rv = new RedirectView("/openmrs/module/iedea/manage.form");
        return rv;        
    }
}
