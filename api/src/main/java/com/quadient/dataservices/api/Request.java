package com.quadient.dataservices.api;

public interface Request<T> {

    /**
     * Gets the path of the request, for instance "/services/address-correction/v1/correct".
     * 
     * @return
     */
    String getPath();

    /**
     * Gets request headers to apply to the request.
     * 
     * @return
     */
    default Headers getHeaders() {
        return Headers.EMPTY;
    };

    default String getMethod() {
        return "POST";
    }

    /**
     * Gets the request body (represented as an object) to send.
     * 
     * @return
     */
    Object getBody();

    Class<T> getResponseBodyClass();
}
