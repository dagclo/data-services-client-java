package com.quadient.dataservices.impl;

import java.io.IOException;
import java.io.UncheckedIOException;
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

import com.quadient.dataservices.api.AuthorizationHeaderProvider;
import com.quadient.dataservices.api.Credentials;
import com.quadient.dataservices.api.FailedResponse;
import com.quadient.dataservices.api.HasBaseUri;
import com.quadient.dataservices.api.Headers;
import com.quadient.dataservices.api.ImmutableHeaders;
import com.quadient.dataservices.api.Request;
import com.quadient.dataservices.api.Response;
import com.quadient.dataservices.api.ServiceCaller;
import com.quadient.dataservices.api.SuccessfulResponse;
import com.quadient.dataservices.exceptions.CreditBalanceException;
import com.quadient.dataservices.exceptions.RequestTooLargeException;
import com.quadient.dataservices.impl.auth.AuthorizationHeaderProviderImpl;

abstract class JerseyServiceCaller implements ServiceCaller, AuthorizationHeaderProvider {

    private static final Logger logger = LoggerFactory.getLogger(JerseyServiceCaller.class);

    private static final int RETRY_STOP_AFTER_ATTEMPT = 5;
    private static final int[] RETRY_WAIT_ON_429 = new int[] { 500, 1500, 5000, 10000, 20000 };

    private static final String HEADER_CONTENT_TYPE = "Content-Type";
    private static final String HEADER_AUTHORIZATION = "Authorization";

    private final AuthorizationHeaderProviderImpl authoritzationHeaderProvider;
    private final ClientBuilder clientBuilder;
    private final URI baseUri;

    public JerseyServiceCaller(ClientBuilder clientBuilder, Credentials credentials) {
        this(clientBuilder, new AuthorizationHeaderProviderImpl(credentials, clientBuilder), getBaseUri(credentials));
    }

    private static URI getBaseUri(Credentials credentials) {
        if (credentials instanceof HasBaseUri) {
            return ((HasBaseUri) credentials).getBaseUri();
        }
        throw new UnsupportedOperationException("Unsupported credentials type: " + credentials.getClass().getName());
    }

    protected JerseyServiceCaller(ClientBuilder clientBuilder,
            AuthorizationHeaderProviderImpl authorizationHeaderProvider, URI baseUri) {
        this.clientBuilder = clientBuilder;
        this.authoritzationHeaderProvider = authorizationHeaderProvider;
        this.baseUri = baseUri;
    }

    @Override
    public String getAuthorizationHeader() {
        return authoritzationHeaderProvider.getAuthorizationHeader();
    }

    @Override
    public <T> Response<T> executeSafe(Request<T> request) throws UncheckedIOException {
        final Response<T> response = fireRequest(request);
        return response;
    }

    protected <T> Response<T> fireRequest(Request<T> request) {
        return fireRequestInternal(request, 1);
    }

    protected <T> Response<T> fireRequestInternal(Request<T> request, int attemptNo) {
        final UriBuilder uriBuilder = UriBuilder.fromUri(getBaseUri() + request.getPath());

        final javax.ws.rs.core.Response response;
        final Client client = clientBuilder.build();
        try {
            final WebTarget webTarget = client.target(uriBuilder);
            final Object bodyObject = request.getBody();
            final Entity<Object> requestEntity = bodyObject == null ? null : Entity.json(bodyObject);
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
                requestBuilder.header(HEADER_AUTHORIZATION, getAuthorizationHeader());
            }
            interceptRequestBefore(requestBuilder);

            try {
                if (requestEntity == null) {
                    response = requestBuilder.method(request.getMethod());
                } else {
                    response = requestBuilder.method(request.getMethod(), requestEntity);
                }
            } catch (UncheckedIOException e) {
                throw e;
            } catch (RuntimeException e) {
                // catch unexpected exceptions, check if they're caused by IOExceptions, and if
                // so, throw that (to be as
                // specific as possible and conform with the advertised method signatures).
                final Throwable cause = e.getCause();
                if (cause instanceof IOException) {
                    throw new UncheckedIOException((IOException) cause);
                }
                throw e;
            }

            final Headers responseHeaders = toImmutableHeaders(response.getStringHeaders());

            final int statusCode = response.getStatus();

            if (statusCode >= 400) {
                final String responseString = response.readEntity(String.class);
                if (attemptNo < RETRY_STOP_AFTER_ATTEMPT) {
                    // sleep a varying amount of time, depending on the status code
                    switch (statusCode) {
                    case 503:
                        // Service temporarily unavailable (wait x seconds)
                        sleep(attemptNo * 2000);
                    case 429:
                        // Client is too busy/throttling the service
                        // (penalize sub-second with exponential backoff by waiting)
                        sleep(RETRY_WAIT_ON_429[attemptNo - 1]);
                    case 502:
                        // Bad gateway (rare, but could be a very short-lived issue, retry)
                        sleep(attemptNo * 180);
                    case 500:
                    case 504:
                        sleep(attemptNo * 20);
                        logger.warn("{} {} failed with HTTP {} (attempt no. {}), but retrying. Response: {}",
                                request.getMethod(), request.getPath(), statusCode, attemptNo, responseString);
                        return fireRequestInternal(request, attemptNo + 1);
                    case 413:
                        throw new RequestTooLargeException(statusCode, response.getStatusInfo().getReasonPhrase(),
                                responseString);
                    case 403:
                        // check for out-of-credits scenario
                        if (responseString.toLowerCase().contains("credit balance")) {
                            throw new CreditBalanceException(statusCode, response.getStatusInfo().getReasonPhrase(),
                                    responseString);
                        }
                    default:
                        logger.warn(
                                "{} {} failed with HTTP {} (attempt no. {}). No retry-logic defined for this HTTP code. Response: {}",
                                request.getMethod(), request.getPath(), statusCode, attemptNo, responseString);
                    }
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
                String responseString = null;
                try {
                    responseString = response.readEntity(String.class);
                } catch (Exception e2) {
                    logger.warn("{} {} failed to re-read failed response entity as a string: {}", request.getMethod(),
                            request.getPath(), e2.getMessage());
                }
                return new FailedResponse<T>(statusCode, response.getStatusInfo().getReasonPhrase(), responseHeaders,
                        responseString, e);
            } finally {
                response.close();
            }
        } finally

        {
            client.close();
        }
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
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

    protected AuthorizationHeaderProviderImpl getAccessTokenProvider() {
        return authoritzationHeaderProvider;
    }
}
