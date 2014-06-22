/**
 * 
 */
package org.mongo.viewer.util;

import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author vvenkatraman
 * 
 */
public class PropertySingleton {
    private static final String PATH_TO_PROPERTIES = "application";

    private static Log log = LogFactory.getLog(PropertySingleton.class);

    private static Properties prop = null;

    /**
     * Instantiates a new property singleton.
     */
    private PropertySingleton() {

    }

    /**
     * Gets the properties.
     * 
     * @return the properties
     */
    public static Properties getProperties() {
        if (null == prop) {
            prop = PropertyLoader.loadProperties(PATH_TO_PROPERTIES);
        }
        return prop;
    }

    /**
     * Gets the string.
     * 
     * @param key
     *            the key
     * @return the string
     */
    public static String getString(String key) {
        return getProperties().getProperty(key);
    }

    /**
     * Gets the string.
     *
     * @param key the key
     * @param def the default value
     * @return the string
     */
    public static String getString(String key, String def) {
        String value = getProperties().getProperty(key);
        if (null == value) {
            return def;
        }
        return value;

    }
    
    public static void main(String a[]) {
        System.out.println("Value = " + getString("key"));
    }
}
