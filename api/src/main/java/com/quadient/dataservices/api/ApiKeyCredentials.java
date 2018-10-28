package com.quadient.dataservices.api;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Objects;

public class ApiKeyCredentials implements Credentials, HasBaseUri, AuthorizationHeaderProvider {

    private URI baseUri;
    private String apiKey;
    private char[] apiSecret;

    public ApiKeyCredentials(Region region, String apiKey, String apiSecret) {
        this(region.getBaseUri(), apiKey, apiSecret.toCharArray());
    }

    public ApiKeyCredentials(Region region, String apiKey, char[] apiSecret) {
        this(region.getBaseUri(), apiKey, apiSecret);
    }

    public ApiKeyCredentials(URI baseUri, String apiKey, char[] apiSecret) {
        this.baseUri = Objects.requireNonNull(baseUri, "Base URI cannot be null");
        this.apiKey = Objects.requireNonNull(apiKey);
        this.apiSecret = Objects.requireNonNull(apiSecret);
    }

    @Override
    public URI getBaseUri() {
        return baseUri;
    }

    public String getApiKey() {
        return apiKey;
    }

    public char[] getApiSecret() {
        return apiSecret;
    }

    @Override
    public String getAuthorizationHeader() {
        final StringBuilder authBuilder = new StringBuilder();
        authBuilder.append(apiKey);
        authBuilder.append(':');
        authBuilder.append(apiSecret);
        final byte[] encodedAuth =
                Base64.getEncoder().encode(authBuilder.toString().getBytes(StandardCharsets.ISO_8859_1));
        return "Basic " + new String(encodedAuth);
    }
}
