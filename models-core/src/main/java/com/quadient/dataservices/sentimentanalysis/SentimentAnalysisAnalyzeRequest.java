package com.quadient.dataservices.sentimentanalysis;

import java.util.List;

import com.quadient.dataservices.api.Request;
import com.quadient.dataservices.sentimentanalysis.model.TransactionalArtifactRequest;
import com.quadient.dataservices.sentimentanalysis.model.TransactionalConfiguration;
import com.quadient.dataservices.sentimentanalysis.model.TransactionalRequest;
import com.quadient.dataservices.sentimentanalysis.model.TransactionalResponse;

public class SentimentAnalysisAnalyzeRequest implements Request<TransactionalResponse> {

    private final TransactionalRequest requestBody;

    public SentimentAnalysisAnalyzeRequest(boolean suggestTopics, String language, List<String> artifactTexts) {
        requestBody = new TransactionalRequest();
        requestBody.setConfiguration(
                new TransactionalConfiguration().defaultLanguage(language).suggestTopics(suggestTopics));
        for (String text : artifactTexts) {
            requestBody.addArtifactsItem(new TransactionalArtifactRequest().text(text));
        }
    }

    public SentimentAnalysisAnalyzeRequest(TransactionalRequest body) {
        requestBody = body;
    }

    @Override
    public String getPath() {
        return "/services/sentiment-analysis/v1/_analyze";
    }

    @Override
    public Object getBody() {
        return requestBody;
    }

    @Override
    public Class<TransactionalResponse> getResponseBodyClass() {
        return TransactionalResponse.class;
    }

}
