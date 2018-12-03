package com.quadient.dataservices.etl;

import java.util.List;
import java.util.Objects;

import com.quadient.dataservices.api.Request;
import com.quadient.dataservices.etl.model.TableCreationRequest;
import com.quadient.dataservices.etl.model.TableInformation;

public class TableCreationSchemaRequest implements Request<TableInformation> {

    private final TableCreationRequest body;

    public TableCreationSchemaRequest(TableCreationRequest body) {
        this.body = Objects.requireNonNull(body);
    }

    public TableCreationSchemaRequest(List<String> columnNames) {
        Objects.requireNonNull(columnNames);
        this.body = new TableCreationRequest();
        this.body.setColumnNames(columnNames);
    }

    @Override
    public String getPath() {
        return "/etl/v1/tables";
    }

    @Override
    public Object getBody() {
        return body;
    }

    @Override
    public Class<TableInformation> getResponseBodyClass() {
        return TableInformation.class;
    }

}
