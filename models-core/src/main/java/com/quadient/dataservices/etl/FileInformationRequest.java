package com.quadient.dataservices.etl;

import java.util.Objects;

import com.quadient.dataservices.api.Request;
import com.quadient.dataservices.etl.model.FileInformation;

public class FileInformationRequest implements Request<FileInformation> {

    private final String fileId;

    public FileInformationRequest(String fileId) {
        this.fileId = Objects.requireNonNull(fileId);
    }

    @Override
    public Class<FileInformation> getResponseBodyClass() {
        return FileInformation.class;
    }

    @Override
    public String getPath() {
        return "/etl/v1/files/" + fileId;
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
