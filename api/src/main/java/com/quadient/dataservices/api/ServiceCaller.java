package com.quadient.dataservices.api;

import java.io.UncheckedIOException;

import com.quadient.dataservices.exceptions.DataServicesException;

/**
 * A component capable of calling Quadient Data Services.
 */
public interface ServiceCaller {

    /**
     * Executes a Data Services request and returns it's HTTP response.
     * 
     * @param request
     * @return
     * @throws UncheckedIOException
     */
    <T> Response<T> executeSafe(Request<T> request) throws UncheckedIOException;

    /**
     * Executes a Data Services request and returns it's expected response model.
     * 
     * @param request
     * @return
     * @throws DataServicesException
     * @throws UncheckedIOException
     */
    default <T> T execute(Request<T> request) throws DataServicesException, UncheckedIOException {
        return executeSafe(request).getBody();
    }
}
