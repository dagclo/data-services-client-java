package com.quadient.dataservices.api;

import java.net.URI;
import java.util.Objects;

public class AdministrativeCredentials implements Credentials, HasBaseUri {

    private final URI baseUri;
    private final String username;
    private final char[] password;

    public AdministrativeCredentials(Region region, String username, String password) {
        this(region.getBaseUri(), username, password.toCharArray());
    }

    public AdministrativeCredentials(Region region, String username, char[] password) {
        this(region.getBaseUri(), username, password);
    }

    public AdministrativeCredentials(URI baseUri, String username, char[] password) {
        this.baseUri = Objects.requireNonNull(baseUri, "Base URI cannot be null");
        this.username = Objects.requireNonNull(username);
        this.password = Objects.requireNonNull(password);
    }

    @Override
    public URI getBaseUri() {
        return baseUri;
    }

    public char[] getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

}
