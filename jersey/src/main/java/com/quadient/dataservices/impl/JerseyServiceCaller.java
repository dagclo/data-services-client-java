package com.quadient.dataservices.impl;

import java.net.URI;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriBuilder;

import com.quadient.dataservices.api.Credentials;
import com.quadient.dataservices.api.FailedResponse;
import com.quadient.dataservices.api.HasBaseUri;
import com.quadient.dataservices.api.Headers;
import com.quadient.dataservices.api.ImmutableHeaders;
import com.quadient.dataservices.api.Request;
import com.quadient.dataservices.api.Response;
import com.quadient.dataservices.api.ServiceCaller;
import com.quadient.dataservices.api.SuccessfulResponse;
import com.quadient.dataservices.impl.auth.AccessTokenProvider;

abstract class JerseyServiceCaller implements ServiceCaller {

    private static final String HEADER_CONTENT_TYPE = "Content-Type";
    private static final String HEADER_AUTHORIZATION = "Authorization";

    private final AccessTokenProvider accessTokenProvider;
    private final ClientBuilder clientBuilder;
    private final URI baseUri;

    public JerseyServiceCaller(ClientBuilder clientBuilder, Credentials credentials) {
        this(clientBuilder, new AccessTokenProvider(credentials, clientBuilder), getBaseUri(credentials));
    }

    private static URI getBaseUri(Credentials credentials) {
        if (credentials instanceof HasBaseUri) {
            return ((HasBaseUri) credentials).getBaseUri();
        }
        throw new UnsupportedOperationException("Unsupported credentials type: " + credentials.getClass().getName());
    }

    protected JerseyServiceCaller(ClientBuilder clientBuilder, AccessTokenProvider accessTokenProvider, URI baseUri) {
        this.clientBuilder = clientBuilder;
        this.accessTokenProvider = accessTokenProvider;
        this.baseUri = baseUri;
    }

    @Override
    public <T> Response<T> executeSafe(Request<T> request) {
        final Response<T> response = fireRequest(request);
        return response;
    }

    protected <T> Response<T> fireRequest(Request<T> request) {
        final UriBuilder uriBuilder = UriBuilder.fromUri(getBaseUri()).path(request.getPath());

        final javax.ws.rs.core.Response response;
        final Client client = clientBuilder.build();
        try {
            final WebTarget webTarget = client.target(uriBuilder);
            final Entity<Object> requestEntity = Entity.json(request.getBody());
            final Builder requestBuilder;
            final Headers headers = request.getHeaders();
            if (headers.getValue(HEADER_CONTENT_TYPE) == null) {
                requestBuilder = webTarget.request(MediaType.APPLICATION_JSON_TYPE);
            } else {
                requestBuilder = webTarget.request(headers.getValue(HEADER_CONTENT_TYPE));
            }
            final Collection<String> keys = headers.getKeys();
            for (String key : keys) {
                requestBuilder.header(key, headers.getValue(key));
            }
            if (!keys.contains(HEADER_AUTHORIZATION)) {
                requestBuilder.header(HEADER_AUTHORIZATION, "Bearer " + accessTokenProvider.getAccessToken());
            }
            interceptRequestBefore(requestBuilder);
            response = requestBuilder.method(request.getMethod(), requestEntity);
            final Headers responseHeaders = toImmutableHeaders(response.getStringHeaders());

            if (response.getStatus() >= 400) {
                final String responseString = response.readEntity(String.class);
                return new FailedResponse<T>(response.getStatus(), responseHeaders, responseString);
            }

            final Class<T> responseBodyClass = request.getResponseBodyClass();
            try {
                response.bufferEntity(); // make entity re-readable in case something goes wrong here
                final T responseBody = response.readEntity(responseBodyClass);
                return new SuccessfulResponse<T>(response.getStatus(), responseHeaders, responseBody);
            } catch (Exception e) {
                final String responseString = response.readEntity(String.class);
                return new FailedResponse<T>(response.getStatus(), responseHeaders, responseString, e);
            } finally {
                response.close();
            }
        } finally {
            client.close();
        }
    }

    protected void interceptRequestBefore(Builder requestBuilder) {
        // to be overridden by subclass
    }

    private Headers toImmutableHeaders(MultivaluedMap<String, String> stringHeaders) {
        final Map<String, String> map = new LinkedHashMap<>();
        stringHeaders.keySet().forEach(key -> {
            final String value = stringHeaders.getFirst(key);
            map.put(key, value);
        });
        return new ImmutableHeaders(map);
    }

    protected URI getBaseUri() {
        return baseUri;
    }

    protected ClientBuilder getClientBuilder() {
        return clientBuilder;
    }

    protected AccessTokenProvider getAccessTokenProvider() {
        return accessTokenProvider;
    }
}
