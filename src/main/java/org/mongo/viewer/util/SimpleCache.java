/**
 * 
 */
package org.mongo.viewer.util;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * The Class Simple Cache.
 * 
 * 
 * This is a glorified map with maxAge and maxSize. This is a simple
 * implementation of the cache that can be used within the application for
 * caching any simple objects.<br>
 * The following features are available
 * <ul>
 * <li>Ensures there is size does not grow beyond <code>maxSize</code></li>
 * <li>Ensures objects inside this not live beyond beyond <code>maxAge</code>
 * seconds</li>
 * </ul>
 * 
 * @param <K>
 *            the type of keys maintained by this map
 * @param <V>
 *            the type of mapped values
 * 
 * @author vvenkatraman
 * 
 * @since 14.1
 */
public class SimpleCache<K, V> extends LinkedHashMap<K, V> implements Map<K, V> {

    /** The Log. */
    private static Log log = LogFactory.getLog(SimpleCache.class);

    /** The load factor. */
    private static final float LOAD_FACTOR = 0.75f; // Defaulting to 0.75

    /** The Constant DEFAULT_SIZE. */
    public static final int DEFAULT_SIZE = 100; // defaulting to 100 objects

    /** The Constant DEFAULT_AGE. */
    public static final int DEFAULT_AGE = 30 * 60; // defaulting to 30 minutes

    /** The max size. */
    private int maxSize = DEFAULT_SIZE;

    /** The max age. */
    private int maxAge = DEFAULT_AGE;

    /** The age. This records the age of an object in memory */
    protected Map<K, Long> ageMap = new HashMap<K, Long>();

    /**
     * Instantiates a new cbeyond cache with default values.
     */
    public SimpleCache() {
        this(DEFAULT_SIZE, DEFAULT_AGE);
    }

    /**
     * Instantiates a new cbeyond cache.
     * 
     * @param maxSize
     *            the max number of objects in memory
     * @param maxAge
     *            the max age in seconds
     */
    public SimpleCache(int maxSize, int maxAge) {
        super(maxSize, LOAD_FACTOR, true);
        this.maxAge = maxAge;
        this.maxSize = maxSize;
        log.debug("Cache with size of " + maxSize + " and age of " + maxAge
                + " created");
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.LinkedHashMap#removeEldestEntry(java.util.Map.Entry)
     */
    @Override
    protected boolean removeEldestEntry(java.util.Map.Entry<K, V> eldest) {
        // Remove the eldest entry if the size of the cache exceeds the
        // maximum size
        return size() > getMaxSize();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.HashMap#put(java.lang.Object, java.lang.Object)
     */
    @Override
    public V put(K key, V value) {
        ageMap.put(key, System.currentTimeMillis());
        return super.put(key, value);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.LinkedHashMap#get(java.lang.Object)
     */
    @Override
    public V get(Object key) {
        if (isAlive(key)) {
            return super.get(key);
        }

        return null;
    }

    /**
     * Checks if is alive.
     * 
     * @param key
     *            the key
     * @return true, if is alive
     */
    private boolean isAlive(Object key) {
        long seconds = (System.currentTimeMillis() - getAge(key)) / 1000;
        // if we have cahed an element we will remove it
        if (seconds > getMaxAge()) {
            if (super.containsKey(key)) {
                super.remove(key);
            }
            return false;
        }
        return true;
    }

    @Override
    public boolean containsKey(Object key) {
        return (isAlive(key) && super.containsKey(key));
    }

    /**
     * Gets the age.
     * 
     * @param key
     *            the key
     * @return the age
     */
    private long getAge(Object key) {
        Long value = ageMap.get(key);
        if (null == value) {
            return 0;
        }
        return value.longValue();
    }

    /**
     * @return the maxSize
     */
    public int getMaxSize() {
        return maxSize;
    }

    /**
     * @param maxSize
     *            the maxSize to set
     */
    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    /**
     * @return the maxAge in seconds
     */
    public int getMaxAge() {
        return maxAge;
    }

    /**
     * @param maxAge
     *            the maxAge to set in seconds
     */
    public void setMaxAge(int maxAge) {
        this.maxAge = maxAge;
    }

}
