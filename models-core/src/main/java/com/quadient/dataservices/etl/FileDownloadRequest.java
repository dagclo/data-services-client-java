package com.quadient.dataservices.etl;

import java.io.InputStream;
import java.util.Collections;
import java.util.Map;

import com.quadient.dataservices.api.Headers;
import com.quadient.dataservices.api.ImmutableHeaders;
import com.quadient.dataservices.api.Request;

public class FileDownloadRequest implements Request<InputStream> {

    private final String fileId;

    public FileDownloadRequest(String fileId) {
        this.fileId = fileId;
    }

    @Override
    public Class<InputStream> getResponseBodyClass() {
        return InputStream.class;
    }

    @Override
    public Headers getHeaders() {
        final Map<String, String> map = Collections.singletonMap("Accept", "application/octet-stream");
        return new ImmutableHeaders(map);
    }

    @Override
    public String getPath() {
        return "/etl/v1/files/" + fileId + "/_download";
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
