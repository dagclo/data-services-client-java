package com.quadient.dataservices.name;

import java.util.List;

import com.quadient.dataservices.api.Request;
import com.quadient.dataservices.name.model.ParseRequest;
import com.quadient.dataservices.name.model.ParseRequestConfiguration;
import com.quadient.dataservices.name.model.ParseRequestItem;
import com.quadient.dataservices.name.model.ParseResponse;

public class NameValidationParseRequest implements Request<ParseResponse> {

    final ParseRequest requestBody;

    public NameValidationParseRequest(ParseRequest body) {
        this.requestBody = body;
    }

    public NameValidationParseRequest(List<String> names) {
        this(null, names);
    }

    public NameValidationParseRequest(ParseRequestConfiguration configuration, List<String> names) {
        this.requestBody = new ParseRequest();
        requestBody.setConfiguration(configuration);
        for (String name : names) {
            requestBody.addRecordsItem(new ParseRequestItem().unstructuredName(name));
        }
    }

    @Override
    public Class<ParseResponse> getResponseBodyClass() {
        return ParseResponse.class;
    }

    @Override
    public String getPath() {
        return "/services/name-validation/v1/parse";
    }

    @Override
    public Object getBody() {
        return requestBody;
    }
}
