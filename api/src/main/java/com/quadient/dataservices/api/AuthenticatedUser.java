package com.quadient.dataservices.api;

import java.io.UncheckedIOException;

import com.quadient.dataservices.exceptions.AuthenticationException;

public interface AuthenticatedUser {

    /**
     * Gets the unique ID of the user.
     * 
     * @return
     */
    String getUserId();

    /**
     * (Re)validates the current credentials, potentially throwing exceptions if they don't validate.
     * 
     * @throws AuthenticationException If the credentials where checked and found to be invalid.
     * @throws UncheckedIOException The request failed due to an underlying issue such as network connectivity, DNS
     *             failure, server certificate validation or timeout.
     */
    void validateCredentials() throws AuthenticationException, UncheckedIOException;

    /**
     * Gets the credit balance of the user.
     * 
     * @return
     */
    double getCreditBalance();
}
