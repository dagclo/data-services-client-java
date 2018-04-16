package com.quadient.dataservices.example;

import com.quadient.dataservices.api.Request;

public class DummyRequest implements Request<DummyResponse> {

    @Override
    public String getPath() {
        return null;
    }

    @Override
    public Object getBody() {
        return null;
    }

    @Override
    public Class<DummyResponse> getResponseBodyClass() {
        return DummyResponse.class;
    }

}
