package com.quadient.dataservices.url;

import java.util.ArrayList;
import java.util.List;

import com.quadient.dataservices.api.Request;
import com.quadient.dataservices.url.model.UrlValidationDepth;
import com.quadient.dataservices.url.model.UrlValidationRequestConfiguration;
import com.quadient.dataservices.url.model.UrlValidationRequestRecord;
import com.quadient.dataservices.url.model.UrlValidationResponse;

public class UrlValidationRequest implements Request<UrlValidationResponse> {

    final com.quadient.dataservices.url.model.UrlValidationRequest requestBody;
    
    public UrlValidationRequest(UrlValidationDepth depth, List<String> urls) {
        this(new UrlValidationRequestConfiguration().validationDepth(depth), urls);
    }

    public UrlValidationRequest(UrlValidationRequestConfiguration configuration, List<String> urls) {
        final List<UrlValidationRequestRecord> records = new ArrayList<>(urls.size());
        for (String url : urls) {
            final UrlValidationRequestRecord record = new UrlValidationRequestRecord().url(url);
            records.add(record);
        }

        requestBody = new com.quadient.dataservices.url.model.UrlValidationRequest();
        requestBody.setConfiguration(configuration);
        requestBody.setRecords(records);
    }

    public UrlValidationRequest(com.quadient.dataservices.url.model.UrlValidationRequest body) {
        this.requestBody = body;
    }

    @Override
    public Class<UrlValidationResponse> getResponseBodyClass() {
        return UrlValidationResponse.class;
    }

    @Override
    public String getPath() {
        return "/services/url-validation/v1/validate";
    }

    @Override
    public Object getBody() {
        return requestBody;
    }
}
