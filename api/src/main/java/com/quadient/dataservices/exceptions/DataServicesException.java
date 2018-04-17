package com.quadient.dataservices.exceptions;

/**
 * Represents one or more requests to the Quadient Data Services that didn't succeed.
 * 
 * Generally these exceptions fall into
 */
public class DataServicesException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public DataServicesException() {
        super();
    }

    public DataServicesException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public DataServicesException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataServicesException(String message) {
        super(message);
    }

    public DataServicesException(Throwable cause) {
        super(cause);
    }

}
