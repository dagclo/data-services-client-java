package com.quadient.dataservices.etl;

import java.io.File;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;

import com.quadient.dataservices.api.FormDataRequest;
import com.quadient.dataservices.etl.model.TableInformation;

public class TableCreationFileUploadRequest implements FormDataRequest<TableInformation> {

    private final File file;

    public TableCreationFileUploadRequest(File file) {
        this.file = Objects.requireNonNull(file);
    }

    @Override
    public String getPath() {
        return "/etl/v1/tables/";
    }

    @Override
    public Map<String, Object> getBody() {
        return Collections.singletonMap("upfile", file);
    }

    @Override
    public Class<TableInformation> getResponseBodyClass() {
        return TableInformation.class;
    }

}
