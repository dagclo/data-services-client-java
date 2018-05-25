package com.quadient.dataservices.exceptions;

/**
 * Specialized exception type used to communicate that the request payload is
 * too large.
 */
public class RequestTooLargeException extends HttpResponseExceptionImpl {

    private static final long serialVersionUID = 1L;

    public RequestTooLargeException(int statusCode, String reason, String response) {
        super(statusCode, reason, response);
    }
}
