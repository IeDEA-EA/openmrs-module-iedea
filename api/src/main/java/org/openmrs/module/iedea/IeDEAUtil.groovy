package org.openmrs.module.iedea

import org.openmrs.module.ModuleFactory

public class IeDEAUtil {
    def moduleClassLoader
       
    public IeDEAUtil() {
        moduleClassLoader = ModuleFactory.getModuleClassLoader("iedea")
    }
    
    def InputStream getResourceAsStream(path) {
        println("IeDEAUtil.getResourceAsStream: ${path}")
        URL url = moduleClassLoader.findResource(path)
        return url.openStream()
    }

    /*
     * Get's a reader using a path with a filesystem type. At the moment, you
     * *must* include the path type.
     * 
     * Currently we accept:
     * file:///abs/path/to/file.csv
     * classpath:///class/path/to/file.csv
     * 
     * TODO Can these be done with java's URL or something similar?
     * 
     * @return A Reader from the source.
     * This method is intended to have minimal, if any side effects.
     */
    public Reader getReader(path) {
        if (path.startsWith("file://")) {
            return new FileReader(path.substring(7)) 
        }
        else if (path.startsWith("classpath://")) {
            return new InputStreamReader(getResourceAsStream(path.substring(12)))
        }
        else {
            throw new IllegalArgumentException()
        }
    }
    
    /*
     * See comments above for getReader
     */
    public InputStream getInputStream(path) {
        if (path.startsWith("file://")) {
            return new FileInputStream(path.substring(7))
        }
        else if (path.startsWith("classpath://")) {
            return getResourceAsStream(path.substring(12))
        }
        else {
            throw new IllegalArgumentException()
        }
    }
}