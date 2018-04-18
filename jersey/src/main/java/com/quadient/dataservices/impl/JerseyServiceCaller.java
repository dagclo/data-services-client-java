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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.quadient.dataservices.api.AccessTokenProvider;
import com.quadient.dataservices.api.Credentials;
import com.quadient.dataservices.api.FailedResponse;
import com.quadient.dataservices.api.HasBaseUri;
import com.quadient.dataservices.api.Headers;
import com.quadient.dataservices.api.ImmutableHeaders;
import com.quadient.dataservices.api.Request;
import com.quadient.dataservices.api.Response;
import com.quadient.dataservices.api.ServiceCaller;
import com.quadient.dataservices.api.SuccessfulResponse;
import com.quadient.dataservices.impl.auth.AccessTokenProviderImpl;

abstract class JerseyServiceCaller implements ServiceCaller, AccessTokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(JerseyServiceCaller.class);

    private static final int RETRY_STOP_AFTER_ATTEMPT = 3;

    private static final String HEADER_CONTENT_TYPE = "Content-Type";
    private static final String HEADER_AUTHORIZATION = "Authorization";

    private final AccessTokenProviderImpl accessTokenProvider;
    private final ClientBuilder clientBuilder;
    private final URI baseUri;

    public JerseyServiceCaller(ClientBuilder clientBuilder, Credentials credentials) {
        this(clientBuilder, new AccessTokenProviderImpl(credentials, clientBuilder), getBaseUri(credentials));
    }

    private static URI getBaseUri(Credentials credentials) {
        if (credentials instanceof HasBaseUri) {
            return ((HasBaseUri) credentials).getBaseUri();
        }
        throw new UnsupportedOperationException("Unsupported credentials type: " + credentials.getClass().getName());
    }

    protected JerseyServiceCaller(ClientBuilder clientBuilder, AccessTokenProviderImpl accessTokenProvider,
            URI baseUri) {
        this.clientBuilder = clientBuilder;
        this.accessTokenProvider = accessTokenProvider;
        this.baseUri = baseUri;
    }

    @Override
    public String getAccessToken() {
        return accessTokenProvider.getAccessToken();
    }

    @Override
    public <T> Response<T> executeSafe(Request<T> request) {
        final Response<T> response = fireRequest(request);
        return response;
    }

    protected <T> Response<T> fireRequest(Request<T> request) {
        return fireRequestInternal(request, 1);
    }

    protected <T> Response<T> fireRequestInternal(Request<T> request, int attemptNo) {
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

            final int statusCode = response.getStatus();

            if (statusCode >= 400) {
                final String responseString = response.readEntity(String.class);
                if (attemptNo < RETRY_STOP_AFTER_ATTEMPT && (statusCode == 502 || statusCode == 503)) {
                    // TODO: Implemented a better retry
                    logger.warn("Failed with HTTP {}, but retrying. Response: {}", statusCode, responseString);
                    try {
                        Thread.sleep(attemptNo * 200);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    return fireRequestInternal(request, attemptNo + 1);
                }
                return new FailedResponse<T>(statusCode, response.getStatusInfo().getReasonPhrase(), responseHeaders,
                        responseString);
            }

            final Class<T> responseBodyClass = request.getResponseBodyClass();
            try {
                response.bufferEntity(); // make entity re-readable in case something goes wrong here
                final T responseBody = response.readEntity(responseBodyClass);
                return new SuccessfulResponse<T>(statusCode, responseHeaders, responseBody);
            } catch (Exception e) {
                final String responseString = response.readEntity(String.class);
                return new FailedResponse<T>(statusCode, response.getStatusInfo().getReasonPhrase(), responseHeaders,
                        responseString, e);
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

    protected AccessTokenProviderImpl getAccessTokenProvider() {
        return accessTokenProvider;
    }
}
