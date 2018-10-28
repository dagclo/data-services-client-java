package com.quadient.dataservices.sanctionlists;

import com.quadient.dataservices.api.Request;
import com.quadient.dataservices.sanctionlists.model.MatchRequest;
import com.quadient.dataservices.sanctionlists.model.MatchResponse;

public class SanctionListCheckRequest implements Request<MatchResponse> {

    private final MatchRequest body;
    
    public SanctionListCheckRequest(MatchRequest body) {
        this.body = body;
    }

    @Override
    public String getPath() {
        return "/services/sanction-lists/v1/match";
    }

    @Override
    public MatchRequest getBody() {
        return body;
    }

    @Override
    public Class<MatchResponse> getResponseBodyClass() {
        return MatchResponse.class;
    }

}
