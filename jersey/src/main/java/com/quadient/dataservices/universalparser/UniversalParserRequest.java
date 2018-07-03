package com.quadient.dataservices.universalparser;

import java.util.List;

import com.quadient.dataservices.api.Request;
import com.quadient.dataservices.universalparser.model.ParseRequestArtifact;
import com.quadient.dataservices.universalparser.model.ParserRequest;
import com.quadient.dataservices.universalparser.model.ParserRequestConfiguration;
import com.quadient.dataservices.universalparser.model.ParserResponse;

public class UniversalParserRequest implements Request<ParserResponse> {

    final ParserRequest requestBody;

    public UniversalParserRequest(boolean emitSpans, List<String> artifactTexts) {
        requestBody = new ParserRequest();
        requestBody.setConfiguration(new ParserRequestConfiguration().emitSpans(emitSpans));
        for (String text : artifactTexts) {
            requestBody.addArtifactsItem(new ParseRequestArtifact().text(text));
        }
    }

    public UniversalParserRequest(boolean emitSpans, String language, List<String> artifactTexts) {
        requestBody = new ParserRequest();
        requestBody.setConfiguration(new ParserRequestConfiguration().emitSpans(emitSpans).defaultLanguage(language));
        for (String text : artifactTexts) {
            requestBody.addArtifactsItem(new ParseRequestArtifact().text(text));
        }
    }

    public UniversalParserRequest(ParserRequest body) {
        this.requestBody = body;
    }

    @Override
    public Class<ParserResponse> getResponseBodyClass() {
        return ParserResponse.class;
    }

    @Override
    public String getPath() {
        return "/services/universal-parser/v1/parse";
    }

    @Override
    public Object getBody() {
        return requestBody;
    }
}
