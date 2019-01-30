package com.quadient.dataservices.impl.auth;

import com.quadient.dataservices.api.AuthorizationHeaderProvider;
import com.quadient.dataservices.api.Credentials;
import com.quadient.dataservices.api.ServiceCaller;

public class OtherAuthenticatedUser extends AbstractAuthenticatedUser {

    private final Credentials credentials;

    public OtherAuthenticatedUser(ServiceCaller serviceCaller, AuthorizationHeaderProvider authorizationHeaderProvider,
            Credentials credentials) {
        super(serviceCaller, authorizationHeaderProvider);
        this.credentials = credentials;
    }

    @Override
    public double getCreditBalance() {
        throw new UnsupportedOperationException(
                "Cannot retrieve credit balance for credentials of type " + credentials.getClass().getName());
    }
}
