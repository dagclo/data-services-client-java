package com.quadient.dataservices.name;

import com.quadient.dataservices.api.Request;
import com.quadient.dataservices.name.model.ProcessRequest;
import com.quadient.dataservices.name.model.ProcessResponse;

public class NameValidationProcessRequest implements Request<ProcessResponse> {

    final ProcessRequest requestBody;

    public NameValidationProcessRequest(ProcessRequest body) {
        this.requestBody = body;
    }

    @Override
    public Class<ProcessResponse> getResponseBodyClass() {
        return ProcessResponse.class;
    }

    @Override
    public String getPath() {
        return "/services/name-validation/v2/process";
    }

    @Override
    public Object getBody() {
        return requestBody;
    }
}
