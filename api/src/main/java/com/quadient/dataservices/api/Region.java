package com.quadient.dataservices.api;

import java.net.URI;

public enum Region {

    US("https://quadientcloud.com"),

    EU("https://quadientcloud.eu");

    private final URI baseUri;

    private Region(String baseUri) {
        this.baseUri = URI.create(baseUri);
    }

    public URI getBaseUri() {
        return baseUri;
    }
}
