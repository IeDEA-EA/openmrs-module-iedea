package org.openmrs.module.iedea

import groovy.json.JsonSlurper
import org.codehaus.groovy.reflection.ReflectionUtils

public class ConfigParser {
    def getResource(name) {
        return getClass().getClassLoader().getResourceAsStream(name)
    }

    public Object getEntireConfig() {
        def jsonConfig = this.getResource("ccsp-mappings.json").text
        def slurper = new JsonSlurper()
        def config = slurper.parseText(jsonConfig)
        return config
    }

    def getConfig(sourceName) {
        def config = getEntireConfig()
        def inconfig = config.inputs[sourceName]
        return inconfig
    }

    /**
     * Returns the mapping of file names to the config specification.
     * 
     * @return
     */
    def getImportFileSpecs() {
        def config = getEntireConfig()
        return config.importFileMatches
    }

    def getEncounterTypeSpecs() {
        def config = getEntireConfig()
        return config.encounterTypes
    }
}