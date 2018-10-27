package com.quadient.dataservices.jobs;

import com.quadient.dataservices.api.Request;

abstract class SimpleGetRequest<T> implements Request<T> {

    @Override
    public final String getMethod() {
        return "GET";
    }

    @Override
    public final Object getBody() {
        return null; // no body for GET
    }

    /**
     * Utility method offered for subclasses to easily build up their path in {@link #getPath()}.
     * 
     * @param sb
     * @param key
     * @param value
     */
    protected void addParam(StringBuilder sb, String key, Object value) {
        if (value == null) {
            return;
        }
        if (sb.indexOf("?") == -1) {
            sb.append('?');
        } else {
            sb.append('&');
        }
        sb.append(key).append('=').append(value);
    }
}
