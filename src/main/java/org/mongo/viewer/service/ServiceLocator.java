/*
 * 
 */
package org.mongo.viewer.service;

import java.util.Map;
import java.util.Properties;

import org.mongo.viewer.util.PropertyLoader;
import org.mongo.viewer.util.PropertySingleton;
import org.mongo.viewer.util.SimpleCache;

/**
 * The Class ServiceLocator.
 */
public class ServiceLocator {

    static Map<String, Service> cache = new SimpleCache<String, Service>();
    private static Properties services = PropertyLoader
            .loadProperties("services");

    /**
     * Lookup.
     * 
     * @param <T>
     *            the generic type
     * @param key
     *            the key
     * @param type
     *            the type
     * @param init
     *            calls inint if true
     * @return the t
     */
    public static <T> T lookup(String key, Class<T> type, boolean init) {
        Service s = cache.get(key);

        if (null == s) {
            s = getService(key);
            cache.put(key, s);
        }

        if (init) {
            s.init();
        }

        T service = (T) s;
        return service;
    }

    /**
     * Lookup.
     * 
     * @param <T>
     *            the generic type
     * @param key
     *            the key
     * @param type
     *            the type
     * @return the t
     */
    public static <T> T lookup(String key, Class<T> type) {
        return lookup(key, type, true);
    }

    /**
     * Gets the service.
     * 
     * @param key
     *            the key
     * @return the service
     */
    private static Service getService(String key) {
        String clazz = services.getProperty(key);

        if (null == clazz) {
            throw new RuntimeException("cannot instantiate " + clazz);
        }
        Service s = null;
        try {

            s = (Service) Class.forName(clazz).newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException("cannot instantiate " + clazz, e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("cannot instantiate " + clazz, e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("cannot instantiate " + clazz, e);
        }

        return s;
    }

}
