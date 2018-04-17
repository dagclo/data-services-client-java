package com.quadient.dataservices.api;

import com.quadient.dataservices.exceptions.DataServicesException;

public final class SuccessfulResponse<T> implements Response<T> {

    private final int statusCode;
    private final Headers headers;
    private final T body;

    public SuccessfulResponse(int statusCode, Headers headers, T body) {
        this.statusCode = statusCode;
        this.headers = headers;
        this.body = body;
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
        return body;
    }

}
