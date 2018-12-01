package com.quadient.dataservices.etl;

import java.util.Objects;

import com.quadient.dataservices.api.Request;
import com.quadient.dataservices.etl.model.TableUpdateRequest;
import com.quadient.dataservices.etl.model.TableUpdateResponse;

public class TableAppendRecordsRequest implements Request<TableUpdateResponse> {

    private final String tableId;
    private final TableUpdateRequest body;

    public TableAppendRecordsRequest(String tableId, TableUpdateRequest body) {
        this.tableId = Objects.requireNonNull(tableId);
        this.body = Objects.requireNonNull(body);
    }

    @Override
    public String getPath() {
        return "/etl/v1/tables/" + tableId;
    }

    @Override
    public Object getBody() {
        return body;
    }

    @Override
    public Class<TableUpdateResponse> getResponseBodyClass() {
        return TableUpdateResponse.class;
    }

}
