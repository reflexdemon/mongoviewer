/**
 * 
 */
package org.mongo.viewer.util;

import javax.servlet.ServletConfig;

/**
 * @author vvenkatraman
 * 
 */
public class LoggingUtils {

    /**
     * Inits the default logging.
     * 
     * @param config
     */
    public static void initDefaultLogging(ServletConfig config) {

        ClassLoader loader = null;
        // made this junit friendly
        if (null == config) {
            loader = LoggingUtils.class.getClassLoader();
        } else {
            loader = config.getServletContext().getClassLoader();
        }

        org.apache.log4j.PropertyConfigurator.configure(loader
                .getResource("log4j.properties"));
    }

    public static void initDefaultLogging() {
        initDefaultLogging(null);
    }
}
