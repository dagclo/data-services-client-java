package com.quadient.dataservices.api;

import com.quadient.dataservices.exceptions.DataServicesException;

/**
 * A component capable of calling Quadient Data Services.
 */
public interface ServiceCaller {

    <T> Response<T> executeSafe(Request<T> request);

    default <T> T execute(Request<T> request) throws DataServicesException {
        return executeSafe(request).getBody();
    }
}
