package com.quadient.dataservices.etl;

import java.util.Map;
import java.util.Objects;

import com.quadient.dataservices.api.Request;

public class TablePageDeleteRequest implements Request<Map<String, Object>> {

    private final String tableId;
    private final String pageId;

    public TablePageDeleteRequest(String tableId, String pageId) {
        this.tableId = Objects.requireNonNull(tableId);
        this.pageId = Objects.requireNonNull(pageId);
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
        return "/etl/v1/tables/" + tableId + "/" + pageId;
    }

    @Override
    public Object getBody() {
        return null;
    }
}
