package com.quadient.dataservices.exceptions;

/**
 * {@link HttpResponseException} used to communicate that the user was denied a
 * request because of a credit balance overrun.
 */
public class CreditBalanceException extends HttpResponseExceptionImpl {

    private static final long serialVersionUID = 1L;

    public CreditBalanceException(int statusCode, String reason, String response) {
        super(statusCode, reason, response);
    }

}
