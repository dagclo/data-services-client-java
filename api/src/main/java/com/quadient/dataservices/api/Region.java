package com.quadient.dataservices.api;

import java.net.URI;

public enum Region implements HasBaseUri {

    US("https://data.quadientcloud.com"),

    EU("https://data.quadientcloud.eu");

    private final URI baseUri;

    private Region(String baseUri) {
        this.baseUri = URI.create(baseUri);
    }

    @Override
    public URI getBaseUri() {
        return baseUri;
    }
}
