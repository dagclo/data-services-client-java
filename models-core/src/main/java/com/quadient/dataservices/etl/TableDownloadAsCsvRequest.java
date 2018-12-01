package com.quadient.dataservices.etl;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;

import com.quadient.dataservices.api.Headers;
import com.quadient.dataservices.api.ImmutableHeaders;
import com.quadient.dataservices.api.Request;

public class TableDownloadAsCsvRequest implements Request<InputStream> {

    private final String tableId;
    private final boolean includeHeader;
    private final char separator;
    private final char quote;
    private final char escape;

    public TableDownloadAsCsvRequest(String tableId) {
        this(tableId, true, ',', '"', '\\');
    }

    public TableDownloadAsCsvRequest(String tableId, boolean includeHeader, char separator, char quote, char escape) {
        this.tableId = Objects.requireNonNull(tableId);
        this.includeHeader = includeHeader;
        this.separator = separator;
        this.quote = quote;
        this.escape = escape;
    }

    @Override
    public Class<InputStream> getResponseBodyClass() {
        return InputStream.class;
    }

    @Override
    public Headers getHeaders() {
        final Map<String, String> map = Collections.singletonMap("Accept", "text/csv");
        return new ImmutableHeaders(map);
    }

    @Override
    public String getPath() {
        String s = encodeParam(separator);
        String q = encodeParam(quote);
        String e = encodeParam(escape);
        return "/etl/v1/tables/" + tableId + "/_csv?includeHeader=" + includeHeader + "&separator=" + s + "&quote=" + q
                + "&escape=" + e;
    }

    private String encodeParam(char c) {
        try {
            return URLEncoder.encode("" + c, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
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
