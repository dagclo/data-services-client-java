package com.quadient.dataservices.etl;

import java.util.Objects;

import com.quadient.dataservices.api.Request;
import com.quadient.dataservices.etl.model.QueryRequest;
import com.quadient.dataservices.etl.model.QueryResponse;

public class FileQueryRequest implements Request<QueryResponse> {

    private final String fileId;
    private final int tableIndex;
    private final Integer offset;
    private final Integer limit;

    public FileQueryRequest(String fileId, int tableIndex) {
        this(fileId, tableIndex, null, null);
    }

    public FileQueryRequest(String fileId, int tableIndex, Integer limit, Integer offset) {
        this.fileId = Objects.requireNonNull(fileId);
        this.tableIndex = tableIndex;
        this.limit = limit;
        this.offset = offset;
    }

    @Override
    public Class<QueryResponse> getResponseBodyClass() {
        return QueryResponse.class;
    }

    @Override
    public String getPath() {
        return "/etl/v1/files/" + fileId + "/_query";
    }

    @Override
    public Object getBody() {
        final QueryRequest queryRequest = new QueryRequest();
        queryRequest.setTableIndex(tableIndex);
        queryRequest.setLimit(limit);
        queryRequest.setOffset(offset);
        return queryRequest;
    }
}
