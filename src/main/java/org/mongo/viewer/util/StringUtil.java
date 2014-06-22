package org.mongo.viewer.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class StringUtil {
    private static Log log = LogFactory.getLog(StringUtil.class);
    private static final Map<Character, String> charMap = new HashMap<Character, String>();
    static {
        charMap.put('&', "&amp;");
        charMap.put('\"', "&quot;");
        charMap.put('\'', "&apos;");
        charMap.put('<', "&lt;");
        charMap.put('>', "&gt;");
    }

    /**
     * Escape string.
     * 
     * @param inputString
     *            the input string
     * @return the string
     */
    public static String escapeString(String inputString) {
        StringBuilder builder = new StringBuilder();
        if (null == inputString) {
            return null;
        }
        log.debug("Escaping " + inputString);
        for (char c : inputString.toCharArray()) {
            String newChar = escapeChar(c);
            builder.append(newChar);
        }

        return builder.toString();
    }

    /**
     * Escape char.
     * 
     * @param c
     *            the c
     * @return the string
     */
    public static String escapeChar(char c) {
        String v = charMap.get(c);
        if (null == v) {
            v = "" + c;
        }

        return v;
    }

}
