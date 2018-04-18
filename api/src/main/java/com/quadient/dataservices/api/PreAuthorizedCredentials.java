package com.quadient.dataservices.api;

import java.net.URI;
import java.util.Objects;

/**
 * Specialized {@link Credentials} object for cases where the client has already authorized and can provide an access
 * token on its own.
 * 
 * Note that this implementation cannot re-generate an access token, so consequently it will only last as long as the
 * access token hasn't expired.
 */
public class PreAuthorizedCredentials implements HasBaseUri, AccessTokenProvider {

    private final URI baseUri;
    private final String accessToken;

    public PreAuthorizedCredentials(URI baseUri, String accessToken) {
        this.baseUri = Objects.requireNonNull(baseUri);
        this.accessToken = Objects.requireNonNull(accessToken);
    }

    @Override
    public String getAccessToken() {
        return accessToken;
    }

    @Override
    public URI getBaseUri() {
        return baseUri;
    }

}
