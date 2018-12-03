package com.quadient.dataservices.etl;

import java.util.Objects;

import com.quadient.dataservices.api.Request;
import com.quadient.dataservices.etl.model.TablePage;

public class TablePageRequest implements Request<TablePage> {

    private final String tableId;
    private final String pageId;
    
    public TablePageRequest(String tableId, String pageId) {
        this.tableId = Objects.requireNonNull(tableId);
        this.pageId = Objects.requireNonNull(pageId);
    }

    @Override
    public String getPath() {
        return "/etl/v1/tables/" + tableId + "/" + pageId;
    }
    
    @Override
    public String getMethod() {
        return "GET";
    }

    @Override
    public Object getBody() {
        return null;
    }

    @Override
    public Class<TablePage> getResponseBodyClass() {
        return TablePage.class;
    }

}
