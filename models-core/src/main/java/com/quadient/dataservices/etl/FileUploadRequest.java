package com.quadient.dataservices.etl;

import java.io.File;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;

import com.quadient.dataservices.api.FormDataRequest;
import com.quadient.dataservices.etl.model.FileInformation;

public class FileUploadRequest implements FormDataRequest<FileInformation> {

    private final File file;

    public FileUploadRequest(File file) {
        this.file = Objects.requireNonNull(file);
    }

    @Override
    public String getPath() {
        return "/etl/v1/files";
    }

    @Override
    public Map<String, Object> getBody() {
        return Collections.singletonMap("upfile", file);
    }

    @Override
    public Class<FileInformation> getResponseBodyClass() {
        return FileInformation.class;
    }

}
