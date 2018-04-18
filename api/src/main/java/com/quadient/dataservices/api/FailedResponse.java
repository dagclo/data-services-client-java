package com.quadient.dataservices.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.quadient.dataservices.exceptions.DataServicesException;
import com.quadient.dataservices.exceptions.HttpResponseExceptionImpl;

public final class FailedResponse<T> implements Response<T> {

    private static final Logger logger = LoggerFactory.getLogger(FailedResponse.class);

    private final int statusCode;
    private final Headers headers;
    private final String reason;
    private final String body;
    private final Exception exception;

    public FailedResponse(int statusCode, String reason, Headers headers, String body) {
        this(statusCode, reason, headers, body, null);
    }

    public FailedResponse(int statusCode, String reason, Headers headers, String body, Exception exception) {
        this.statusCode = statusCode;
        this.reason = reason;
        this.headers = headers;
        this.body = body;
        this.exception = exception;
    }

    @Override
    public int getStatusCode() {
        return statusCode;
    }

    @Override
    public Headers getHeaders() {
        return headers;
    }

    @Override
    public T getBody() throws DataServicesException {
        if (exception != null) {
            if (exception instanceof DataServicesException) {
                throw (DataServicesException) exception;
            }
            if (statusCode >= 200 && statusCode < 300) {
                logger.error("Failed to handle successful HTTP {} response: \n{}", statusCode, body);
                // something went wrong client-side, so rethrow the exception
                if (exception instanceof RuntimeException) {
                    throw (RuntimeException) exception;
                }
                throw new DataServicesException(exception);
            }
        }
        throw new HttpResponseExceptionImpl(statusCode, reason, body);
    }

}
