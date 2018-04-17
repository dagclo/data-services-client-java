package com.quadient.dataservices.api;

import java.net.URI;
import java.util.Objects;

public class QuadientCloudCredentials implements Credentials, HasBaseUri {

    private final URI baseUri;
    private final String company;
    private final String username;
    private final char[] password;

    public QuadientCloudCredentials(Region region, String company, String username, String password) {
        this(region, company, username, password.toCharArray());
    }

    public QuadientCloudCredentials(Region region, String company, String username, char[] password) {
        Objects.requireNonNull(region, "Region cannot be null");
        this.baseUri = region.getBaseUri();
        this.company = Objects.requireNonNull(company);
        this.username = Objects.requireNonNull(username);
        this.password = Objects.requireNonNull(password);
    }

    public QuadientCloudCredentials(URI baseUri, String company, String username, char[] password) {
        this.baseUri = Objects.requireNonNull(baseUri, "Base URI cannot be null");
        this.company = Objects.requireNonNull(company);
        this.username = Objects.requireNonNull(username);
        this.password = Objects.requireNonNull(password);
    }

    @Override
    public URI getBaseUri() {
        return baseUri;
    }

    public String getCompany() {
        return company;
    }

    public char[] getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }
}
