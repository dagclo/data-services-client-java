package com.quadient.dataservices.api;

import com.quadient.dataservices.exceptions.DataServicesException;
import com.quadient.dataservices.exceptions.HttpResponseExceptionImpl;

public final class FailedResponse<T> implements Response<T> {

    private final int statusCode;
    private final Headers headers;
    private final String body;
    private final Exception exception;

    public FailedResponse(int statusCode, Headers headers, String body) {
        this(statusCode, headers, body, null);
    }

    public FailedResponse(int statusCode, Headers headers, String body, Exception exception) {
        this.statusCode = statusCode;
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
        if (exception != null && exception instanceof DataServicesException) {
            throw (DataServicesException) exception;
        }
        throw new HttpResponseExceptionImpl(statusCode, body);
    }

}
