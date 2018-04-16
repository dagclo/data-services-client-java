package com.quadient.dataservices.exceptions;

/**
 * Interface for exceptions that carry a HTTP response
 */
public interface HttpResponseException {

    public int getHttpStatusCode();

    public String getHttpResponse();
}
