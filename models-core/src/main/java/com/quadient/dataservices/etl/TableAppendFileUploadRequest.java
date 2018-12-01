package com.quadient.dataservices.etl;

import java.io.File;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;

import com.quadient.dataservices.api.FormDataRequest;
import com.quadient.dataservices.etl.model.TableUpdateResponse;

public class TableAppendFileUploadRequest implements FormDataRequest<TableUpdateResponse> {

    private final File file;
    private final String tableId;

    public TableAppendFileUploadRequest(String tableId, File file) {
        this.tableId = Objects.requireNonNull(tableId);
        this.file = Objects.requireNonNull(file);
    }

    @Override
    public String getPath() {
        return "/etl/v1/tables/" + tableId;
    }

    @Override
    public Map<String, Object> getBody() {
        return Collections.singletonMap("upfile", file);
    }

    @Override
    public Class<TableUpdateResponse> getResponseBodyClass() {
        return TableUpdateResponse.class;
    }

}
