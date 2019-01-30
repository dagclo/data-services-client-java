package com.quadient.dataservices.impl.auth;

import java.io.UncheckedIOException;

import com.quadient.dataservices.api.AuthenticatedUser;
import com.quadient.dataservices.api.AuthorizationHeaderProvider;
import com.quadient.dataservices.api.Request;
import com.quadient.dataservices.api.ServiceCaller;
import com.quadient.dataservices.api.SimpleRequest;
import com.quadient.dataservices.exceptions.AuthenticationException;
import com.quadient.dataservices.users.model.UserInformationResponse;

public abstract class AbstractAuthenticatedUser implements AuthenticatedUser {

    private final ServiceCaller serviceCaller;
    private final AuthorizationHeaderProvider authorizationHeaderProvider;
    private UserInformationResponse userInformation;

    public AbstractAuthenticatedUser(ServiceCaller serviceCaller,
            AuthorizationHeaderProvider authorizationHeaderProvider) {
        this.serviceCaller = serviceCaller;
        this.authorizationHeaderProvider = authorizationHeaderProvider;
    }

    public final void validateCredentials() throws AuthenticationException, UncheckedIOException {
        authorizationHeaderProvider.getAuthorizationHeader(true);
    }

    @Override
    public final String getUserId() {
        return getUserInformation().getUserId();
    }
    
    protected AuthorizationHeaderProvider getAuthorizationHeaderProvider() {
        return authorizationHeaderProvider;
    }
    
    protected ServiceCaller getServiceCaller() {
        return serviceCaller;
    }

    protected final UserInformationResponse getUserInformation() {
        if (userInformation == null) {
            final Request<UserInformationResponse> request =
                    SimpleRequest.get("/user/v1", UserInformationResponse.class);
            userInformation = serviceCaller.execute(request);
        }
        return userInformation;
    }
}
