/**
 * 
 */
package org.mongo.viewer.exception;

/**
 * @author vvenkatraman
 * 
 */
public class ServiceException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = -5881225937782063674L;

    /**
     * @param message
     * @param cause
     */
    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param message
     */
    public ServiceException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public ServiceException(Throwable cause) {
        super(cause);
    }

}
