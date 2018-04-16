package com.quadient.dataservices.exceptions;

public class HttpResponseExceptionImpl extends DataServicesException implements HttpResponseException {

    private static final long serialVersionUID = 1L;

    private final int statusCode;
    private final String response;

    public HttpResponseExceptionImpl(int statusCode, String response) {
        super("HTTP " + statusCode + ":\n" + response);
        this.statusCode = statusCode;
        this.response = response;
    }

    @Override
    public int getHttpStatusCode() {
        return statusCode;
    }

    @Override
    public String getHttpResponse() {
        return response;
    }

}
