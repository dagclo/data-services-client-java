package com.quadient.dataservices.api;

/**
 * Simple implementation of {@link Request} which allows to pass in everything as parameters.
 * 
 * @param <T>
 */
public final class SimpleRequest<T> implements Request<T> {

    public static <T> Request<T> post(String path, Headers headers, Object body, Class<T> responseBodyClass) {
        return new SimpleRequest<>("POST", path, headers, body, responseBodyClass);
    }

    public static <T> Request<T> put(String path, Headers headers, Object body, Class<T> responseBodyClass) {
        return new SimpleRequest<>("PUT", path, headers, body, responseBodyClass);
    }

    public static <T> Request<T> delete(String path, Headers headers, Class<T> responseBodyClass) {
        return new SimpleRequest<>("DELETE", path, headers, null, responseBodyClass);
    }

    public static <T> Request<T> get(String path, Headers headers, Class<T> responseBodyClass) {
        return new SimpleRequest<>("GET", path, headers, null, responseBodyClass);
    }

    private final String method;
    private final String path;
    private final Headers headers;
    private final Object body;
    private final Class<T> responseBodyClass;

    public SimpleRequest(String method, String path, Headers headers, Object body, Class<T> responseBodyClass) {
        this.method = method;
        this.path = path;
        this.headers = headers;
        this.body = body;
        this.responseBodyClass = responseBodyClass;
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public Object getBody() {
        return body;
    }

    @Override
    public Class<T> getResponseBodyClass() {
        return responseBodyClass;
    }

    @Override
    public Headers getHeaders() {
        return headers;
    }

    @Override
    public String getMethod() {
        return method;
    }
}
