package com.quadient.dataservices.etl;

import java.util.Objects;

import com.quadient.dataservices.api.Request;
import com.quadient.dataservices.etl.model.TableInformation;

public class TableInformationRequest implements Request<TableInformation> {

    private final String tableId;
    private final boolean retrieveColumns;
    private final boolean retrievePages;

    public TableInformationRequest(String tableId) {
        this(tableId, true, true);
    }

    public TableInformationRequest(String tableId, boolean retrieveColumns, boolean retrievePages) {
        this.tableId = Objects.requireNonNull(tableId);
        this.retrieveColumns = retrieveColumns;
        this.retrievePages = retrievePages;
    }

    @Override
    public Class<TableInformation> getResponseBodyClass() {
        return TableInformation.class;
    }

    @Override
    public String getPath() {
        return "/etl/v1/tables/" + tableId + "?retrieveColumns=" + retrieveColumns + "&retrievePages=" + retrievePages;
    }

    @Override
    public String getMethod() {
        return "GET";
    }

    @Override
    public Object getBody() {
        return null;
    }
}
