package com.quadient.dataservices.impl.auth;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Base64;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.quadient.dataservices.api.AuthorizationHeaderProvider;
import com.quadient.dataservices.api.ServiceCaller;

public class QuadientCloudAuthenticatedUser extends AbstractAuthenticatedUser {

    public QuadientCloudAuthenticatedUser(ServiceCaller serviceCaller,
            AuthorizationHeaderProvider authorizationHeaderProvider) {
        super(serviceCaller, authorizationHeaderProvider);
    }

    @Override
    public double getCreditBalance() {
        final String header = getAuthorizationHeaderProvider().getAuthorizationHeader(true);
        final String[] parts = header.split("\\.");
        if (parts.length == 3) {
            final String bodyEncoded = parts[1];
            final byte[] bodyDecoded = Base64.getDecoder().decode(bodyEncoded);
            try {
                final JsonNode creditBalanceNode = new ObjectMapper().readTree(bodyDecoded).get("cb");
                if (creditBalanceNode != null) {
                    // found the credit balance in the JWT
                    return creditBalanceNode.asDouble();
                }
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }
        throw new IllegalStateException("Unexpected access token format, could not determine credit balance.");
    }
}
