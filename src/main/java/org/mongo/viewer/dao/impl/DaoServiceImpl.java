package org.mongo.viewer.dao.impl;

import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mongo.viewer.dao.DAOService;
import org.mongo.viewer.dao.Dao;
import org.mongo.viewer.exception.ServiceException;
import org.mongo.viewer.util.PropertyLoader;
import org.mongo.viewer.util.SimpleCache;

public class DaoServiceImpl implements DAOService {

    private static Log log = LogFactory.getLog(DaoServiceImpl.class);
    
    static SimpleCache<String, Dao<?>> daoCache = new SimpleCache<String, Dao<?>>(
            20, 3000);
    private static Properties dao = PropertyLoader.loadProperties("dao");

    /* (non-Javadoc)
     * @see org.mongo.viewer.dao.DAOService#getDao(java.lang.String)
     */
    @Override
    public Dao<?> getDao(String key) throws ServiceException {
        log.debug("getDao called with key = " + key);
        Dao<?> toReturn = daoCache.get(key);

        if (null == toReturn) {

            try {
                String clazz = dao.getProperty(key);
                toReturn = getInstance(clazz);
            } catch (InstantiationException e) {
                throw new ServiceException("Cannot create dao " + key, e);
            } catch (IllegalAccessException e) {
                throw new ServiceException("Cannot create dao " + key, e);
            } catch (ClassNotFoundException e) {
                throw new ServiceException("Cannot create dao " + key, e);
            }
        }
        return toReturn;
    }

    /**
     * Gets the single instance of DaoServiceImpl.
     *
     * @param clazz the clazz
     * @return single instance of DaoServiceImpl
     * @throws InstantiationException the instantiation exception
     * @throws IllegalAccessException the illegal access exception
     * @throws ClassNotFoundException the class not found exception
     */
    private Dao<?> getInstance(String clazz) throws InstantiationException,
            IllegalAccessException, ClassNotFoundException {
        log.debug("Instantiating class <" + clazz + "> with default constructor" );
        Dao<?> instance = (Dao<?>) Class.forName(clazz).newInstance();
        return instance;
    }

}
