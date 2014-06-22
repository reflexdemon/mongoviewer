/**
 * 
 */
package org.mongo.viewer.dao;

import org.mongo.viewer.exception.ServiceException;


/**
 * This is supposed to be a factory service.
 * This will provide the Dao for the application
 * The key needs to be added inside the applocation.properties
 * @author vvenkatraman
 * 
 */
public interface DAOService {

    Dao<?> getDao(String key) throws ServiceException;

}
