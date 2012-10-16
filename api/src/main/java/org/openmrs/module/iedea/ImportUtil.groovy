package org.openmrs.module.iedea

import org.openmrs.api.context.Context
import org.openmrs.EncounterType;
import org.openmrs.module.iedea.api.IeDEAEastAfricaService

class ImportUtil {

    /**
     * Returns the current files we have in the loading dock by the type of
     * import specification they are. (ie. FACES Initial, FACES Followup, 
     * AMPATH Via, etc)
     * @param directoryPath location where we are storing files to import
     * @param configObj the configuration object with our files to dirs
     * {
     * "facesInitial": "CCSPInitialForm_v7_results.*csv",
     * "AMPATHVia": "AMPATHvia.xls"
     * }
     * @return
     */
    public Map<String,List> getImportFilesByType(directoryPath,configObj) {
        def togo = [:]
        def dir = new File(directoryPath)
        dir.eachFile(groovy.io.FileType.FILES, { f ->
            configObj.each { key, value ->
                if (f.name =~ value) {
                    if (!togo.containsKey(key)) {
                        togo[key] = []
                    }
                    togo[key].add(f.name)
                }
            }
        })
        return togo
    }

    public Map<String,List> getImportFilesByType() {
        def serv = Context.getService(IeDEAEastAfricaService.class)
        def dir = serv.getImportLogDir()
        def configObj = new ConfigParser().getImportFileSpecs()
        return getImportFilesByType(dir,configObj)
    }

    public void initializeIedeaEncounterTypes() {
        def encServ = Context.getEncounterService();
        def encTypeSpecs = new ConfigParser().getEncounterTypeSpecs();
        encTypeSpecs.each { key, val ->
            def encTypeName = val.id;
            def encTypeDesc = val.description
            def encType = encServ.getEncounterType(encTypeName)
            if (encType == null) {
                def newEncType = new EncounterType(encTypeName,encTypeDesc)
                encServ.saveEncounterType(newEncType)
            }
        }
    }
    
    public List<EncounterType> getIedeaEncounterTypes() {
        def encServ = Context.getEncounterService();
        def encTypeSpecs = new ConfigParser().getEncounterTypeSpecs();
        def togo = []
        encTypeSpecs.each { key, val ->
            def encType = encServ.getEncounterType(val.id)
            if (encType == null) {
                throw new RuntimeException("IeDEA Encounter Not in System yet")
            }
            else {
                togo.push(encType)
            }
        }
        return togo
    }
    
    public EncounterType getIedeaEncounterType(typeName) {
        def togo
        def encServ = Context.getEncounterService()
        def encTypeSpecs = new ConfigParser().getEncounterTypeSpecs()
        togo = encServ.getEncounterType(encTypeSpecs[typeName].id)
        if (togo == null) {
            throw new RuntimeException("IeDEA Encounter ${typeName} does not exist.")
        }
        return togo
    }    
}
