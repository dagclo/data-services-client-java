package com.quadient.dataservices.impl.auth;

import java.net.URI;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import com.quadient.dataservices.api.AccessTokenProvider;
import com.quadient.dataservices.api.AdministrativeCredentials;
import com.quadient.dataservices.api.AuthorizationHeaderProvider;
import com.quadient.dataservices.api.Credentials;
import com.quadient.dataservices.api.QuadientCloudCredentials;
import com.quadient.dataservices.exceptions.AuthenticationException;

public final class AuthorizationHeaderProviderImpl implements AuthorizationHeaderProvider {

    // Default expiration is 10 minutes. To be conservative, renew every 8 minutes.
    private static final long TOKEN_EXPIRATION_MS = 8 * 60 * 1000;

    private static final String QC_AUTH_TOKEN_PATH = "/api/query/Users/AuthTokenByLogin";
    private static final String ADMIN_AUTH_TOKEN_PATH = "/oauth/token";

    private final Credentials credentials;
    private final ClientBuilder clientBuilder;

    private String latestToken;
    private long latestTokenIssueTimestamp = -1;

    private static void validateCredentials(Credentials credentials) {
        if (credentials == null) {
            throw new IllegalArgumentException("Credentials cannot be null");
        }
        if (credentials instanceof QuadientCloudCredentials) {
            final QuadientCloudCredentials usernamePasswordCredentials = (QuadientCloudCredentials) credentials;
            require("Username", usernamePasswordCredentials.getUsername());
            require("Password", new String(usernamePasswordCredentials.getPassword()));
            require("Company", usernamePasswordCredentials.getCompany());
        } else if (credentials instanceof AdministrativeCredentials) {
            final AdministrativeCredentials administrativeCredentials = (AdministrativeCredentials) credentials;
            require("Username", administrativeCredentials.getUsername());
            require("Password", new String(administrativeCredentials.getPassword()));
        } else if (credentials instanceof AccessTokenProvider || credentials instanceof AuthorizationHeaderProvider) {
            // nothing to validate
        } else {
            throw new UnsupportedOperationException(
                    "Unsupported credentials type: " + credentials.getClass().getName());
        }
    }

    private static void require(String label, String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(label + " cannot be null or empty");
        }
    }

    public AuthorizationHeaderProviderImpl(Credentials credentials, ClientBuilder clientBuilder) {
        validateCredentials(credentials);
        this.credentials = credentials;
        this.clientBuilder = clientBuilder;
    }

    @Override
    public String getAuthorizationHeader() {
        if (credentials instanceof AuthorizationHeaderProvider) {
            return ((AuthorizationHeaderProvider) credentials).getAuthorizationHeader();
        }

        if (latestTokenIssueTimestamp + TOKEN_EXPIRATION_MS < System.currentTimeMillis()) {
            final String token;
            if (credentials instanceof QuadientCloudCredentials) {
                final QuadientCloudCredentials usernamePasswordCredentials = (QuadientCloudCredentials) credentials;
                token = requestAccessToken(usernamePasswordCredentials);
            } else if (credentials instanceof AdministrativeCredentials) {
                final AdministrativeCredentials administrativeCredentials = (AdministrativeCredentials) credentials;
                token = requestAccessToken(administrativeCredentials);
            } else if (credentials instanceof AccessTokenProvider) {
                token = ((AccessTokenProvider) credentials).getAccessToken();
            } else {
                throw new UnsupportedOperationException(
                        "Unsupported credentials type: " + credentials.getClass().getName());
            }
            latestTokenIssueTimestamp = System.currentTimeMillis();
            latestToken = token;
        }

        return "Bearer " + latestToken;
    }

    private String requestAccessToken(AdministrativeCredentials administrativeCredentials) {
        final URI baseUri = administrativeCredentials.getBaseUri();
        final UriBuilder uriBuilder = UriBuilder.fromUri(baseUri);
        uriBuilder.path(ADMIN_AUTH_TOKEN_PATH);

        final Form form = new Form();
        form.param("grant_type", "password");
        form.param("username", administrativeCredentials.getUsername());
        form.param("password", new String(administrativeCredentials.getPassword()));
        final Entity<Form> requestEntity = Entity.form(form);

        final Client client = clientBuilder.build();
        try {
            final Response response =
                    client.target(uriBuilder).request(MediaType.APPLICATION_JSON_TYPE).post(requestEntity);

            final String token;

            switch (response.getStatusInfo().getFamily()) {
            case SUCCESSFUL:
                final AdministrativeAuthTokenResponse authTokenResponse =
                        response.readEntity(AdministrativeAuthTokenResponse.class);
                token = authTokenResponse.getAccessToken();
                break;
            default:
                throw new AuthenticationException(response.getStatus(), response.getStatusInfo().getReasonPhrase(),
                        response.readEntity(String.class));
            }
            return token;
        } finally {
            client.close();
        }
    }

    public String requestAccessToken(final QuadientCloudCredentials usernamePasswordCredentials) {
        final URI baseUri = usernamePasswordCredentials.getBaseUri();
        final UriBuilder uriBuilder = UriBuilder.fromUri(baseUri);

        // add company subdomain
        final String dataPrefix = "data.";
        if (baseUri.getHost().startsWith(dataPrefix)) {
            final String hostWithoutPrefix = baseUri.getHost().substring(dataPrefix.length());
            final String newHost = usernamePasswordCredentials.getCompany() + "." + hostWithoutPrefix;
            uriBuilder.host(newHost);
        }
        uriBuilder.path(QC_AUTH_TOKEN_PATH);

        final Entity<QuadientCloudAuthTokenRequest> requestEntity =
                Entity.json(new QuadientCloudAuthTokenRequest(usernamePasswordCredentials.getUsername(),
                        usernamePasswordCredentials.getPassword()));

        final Client client = clientBuilder.build();
        try {
            final Response response =
                    client.target(uriBuilder).request(MediaType.APPLICATION_JSON_TYPE).post(requestEntity);

            final String token;

            switch (response.getStatusInfo().getFamily()) {
            case SUCCESSFUL:
                final QuadientCloudAuthTokenResponse authTokenResponse =
                        response.readEntity(QuadientCloudAuthTokenResponse.class);
                token = authTokenResponse.getToken();
                break;
            default:
                throw new AuthenticationException(response.getStatus(), response.getStatusInfo().getReasonPhrase(),
                        response.readEntity(String.class));
            }
            return token;
        } finally {
            client.close();
        }
    }
}
