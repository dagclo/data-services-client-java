package com.quadient.dataservices.etl;

import java.util.Objects;

import com.quadient.dataservices.api.Request;
import com.quadient.dataservices.etl.model.TransformRequest;
import com.quadient.dataservices.etl.model.TransformResponse;

public class FileTransformToTableRequest implements Request<TransformResponse> {

    private final TransformRequest body;
    private final String fileId;

    public FileTransformToTableRequest(String fileId) {
        this(fileId, new TransformRequest());
    }

    public FileTransformToTableRequest(String fileId, TransformRequest body) {
        this.fileId = Objects.requireNonNull(fileId);
        this.body = Objects.requireNonNull(body);
    }

    @Override
    public String getPath() {
        return "/etl/v1/files/" + fileId + "/_transform";
    }

    @Override
    public Object getBody() {
        if (body.getSrcTableIndex() == null) {
            body.setSrcTableIndex(0);
        }
        return body;
    }

    @Override
    public Class<TransformResponse> getResponseBodyClass() {
        return TransformResponse.class;
    }

}
