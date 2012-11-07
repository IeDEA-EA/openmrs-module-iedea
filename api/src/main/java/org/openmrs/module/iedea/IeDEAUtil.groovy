package org.openmrs.module.iedea

public class IeDEAUtil {

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
            return new InputStreamReader(path.getClass().getResourceAsStream(path.substring(12)))
        }
        else {
            throw new IllegalArgumentException()
        }
    }
    
    public InputStream getInputStream(path) {
        if (path.startsWith("file://")) {
            return new FileInputStream(path.substring(7))
        }
        else if (path.startsWith("classpath://")) {
            return path.getClass().getResourceAsStream(path.substring(12))
        }
        else {
            throw new IllegalArgumentException()
        }
    }
}
