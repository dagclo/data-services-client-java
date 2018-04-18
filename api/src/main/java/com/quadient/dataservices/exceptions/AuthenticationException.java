package com.quadient.dataservices.exceptions;

/**
 * Represents an exception caused by issues in authentication with Quadient Data Services.
 */
public class AuthenticationException extends HttpResponseExceptionImpl {

    private static final long serialVersionUID = 1L;

    public AuthenticationException(int statusCode, String reason, String response) {
        super(statusCode, reason, response);
    }
}
