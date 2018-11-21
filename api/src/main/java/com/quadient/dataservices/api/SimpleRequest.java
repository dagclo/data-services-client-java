package com.quadient.dataservices.api;

import java.util.Map;

/**
 * Simple implementation of {@link Request} which allows to pass in everything as parameters.
 * 
 * @param <T>
 */
public final class SimpleRequest<T> implements Request<T> {

    private static final String GET = "GET";
    private static final String POST = "POST";
    private static final String PUT = "PUT";
    private static final String DELETE = "DELETE";

    public static <T> Request<T> post(String path, Headers headers, Object body, Class<T> responseBodyClass) {
        return new SimpleRequest<>(GET, path, headers, body, responseBodyClass);
    }

    public static <T> Request<T> post(String path, Object body, Class<T> responseBodyClass) {
        return post(path, Headers.EMPTY, body, responseBodyClass);
    }

    public static Request<Map<String, ?>> postUntyped(String path, Headers headers, Object body) {
        return new SimpleRequest<Map<String, ?>>(POST, path, headers, body, Map.class);
    }

    public static Request<Map<String, ?>> postUntyped(String path, Object body) {
        return postUntyped(path, Headers.EMPTY, body);
    }

    public static <T> Request<T> put(String path, Headers headers, Object body, Class<T> responseBodyClass) {
        return new SimpleRequest<>(PUT, path, headers, body, responseBodyClass);
    }

    public static <T> Request<T> put(String path, Object body, Class<T> responseBodyClass) {
        return put(path, Headers.EMPTY, body, responseBodyClass);
    }

    public static Request<Map<String, ?>> putUntyped(String path, Headers headers, Object body) {
        return new SimpleRequest<Map<String, ?>>(PUT, path, headers, body, Map.class);
    }

    public static Request<Map<String, ?>> putUntyped(String path, Object body) {
        return putUntyped(path, Headers.EMPTY, body);
    }

    public static <T> Request<T> delete(String path, Headers headers, Class<T> responseBodyClass) {
        return new SimpleRequest<>(DELETE, path, headers, null, responseBodyClass);
    }

    public static <T> Request<T> delete(String path, Class<T> responseBodyClass) {
        return delete(path, Headers.EMPTY, responseBodyClass);
    }

    public static Request<Map<String, ?>> deleteUntyped(String path, Headers headers) {
        return new SimpleRequest<Map<String, ?>>(DELETE, path, headers, null, Map.class);
    }

    public static Request<Map<String, ?>> deleteUntyped(String path) {
        return deleteUntyped(path, Headers.EMPTY);
    }

    public static <T> Request<T> get(String path, Headers headers, Class<T> responseBodyClass) {
        return new SimpleRequest<>(GET, path, headers, null, responseBodyClass);
    }

    public static <T> Request<T> get(String path, Class<T> responseBodyClass) {
        return get(path, Headers.EMPTY, responseBodyClass);
    }

    public static Request<Map<String, ?>> getUntyped(String path, Headers headers) {
        return new SimpleRequest<Map<String, ?>>(GET, path, headers, null, Map.class);
    }

    public static Request<Map<String, ?>> getUntyped(String path) {
        return getUntyped(path, Headers.EMPTY);
    }

    private final String method;
    private final String path;
    private final Headers headers;
    private final Object body;
    private final Class<? super T> responseBodyClass;

    public SimpleRequest(String method, String path, Headers headers, Object body, Class<? super T> responseBodyClass) {
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
        // Although this is a wrong cast, it will work at runtime because of type-erasure. We need it to support the
        // "untyped" Map<String,?> classes.
        @SuppressWarnings("unchecked") final Class<T> cls = (Class<T>) responseBodyClass;
        return cls;
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
