package org.mongo.viewer.dao.impl;

import java.util.Properties;

import org.mongo.viewer.dao.DAOService;
import org.mongo.viewer.dao.Dao;
import org.mongo.viewer.exception.ServiceException;
import org.mongo.viewer.util.PropertyLoader;
import org.mongo.viewer.util.SimpleCache;

public class DaoServiceImpl implements DAOService {

    static SimpleCache<String, Dao<?>> daoCache = new SimpleCache<String, Dao<?>>(
            20, 3000);
    private static Properties dao = PropertyLoader.loadProperties("dao");

    @Override
    public Dao<?> getDao(String key) throws ServiceException {
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

    private Dao<?> getInstance(String clazz) throws InstantiationException,
            IllegalAccessException, ClassNotFoundException {
        Dao<?> instance = (Dao<?>) Class.forName(clazz).newInstance();
        return instance;
    }

}
