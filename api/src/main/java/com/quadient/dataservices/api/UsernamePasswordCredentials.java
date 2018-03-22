package com.quadient.dataservices.api;

import java.net.URI;

public class UsernamePasswordCredentials implements Credentials {

    private final URI baseUri;
    private final String company;
    private final String username;
    private final char[] password;

    public UsernamePasswordCredentials(Region region, String company, String username, char[] password) {
        this.baseUri = region.getBaseUri();
        this.company = company;
        this.username = username;
        this.password = password;
    }

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
