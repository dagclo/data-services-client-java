package com.quadient.dataservices.etl;

import java.util.Objects;

import com.quadient.dataservices.api.Request;
import com.quadient.dataservices.etl.model.TableInformation;

public class TableInformationRequest implements Request<TableInformation> {

    private final String tableId;

    public TableInformationRequest(String tableId) {
        this.tableId = Objects.requireNonNull(tableId);
    }

    @Override
    public Class<TableInformation> getResponseBodyClass() {
        return TableInformation.class;
    }

    @Override
    public String getPath() {
        return "/etl/v1/tables/" + tableId;
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
