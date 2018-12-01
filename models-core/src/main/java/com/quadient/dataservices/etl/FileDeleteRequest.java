package com.quadient.dataservices.etl;

import java.util.Map;

import com.quadient.dataservices.api.Request;

public class FileDeleteRequest implements Request<Map<String, Object>> {

    private final String fileId;

    public FileDeleteRequest(String fileId) {
        this.fileId = fileId;
    }

    @Override
    public String getMethod() {
        return "DELETE";
    }

    @SuppressWarnings("unchecked")
    @Override
    public Class<Map<String, Object>> getResponseBodyClass() {
        final Class<?> cls = Map.class;
        return (Class<Map<String, Object>>) cls;
    }

    @Override
    public String getPath() {
        return "/etl/v1/files/" + fileId;
    }

    @Override
    public Object getBody() {
        return null;
    }
}
