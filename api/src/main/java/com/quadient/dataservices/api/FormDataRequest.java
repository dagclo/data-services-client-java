package com.quadient.dataservices.api;

import java.util.Map;

public interface FormDataRequest<T> extends Request<T> {

    @Override
    Map<String, Object> getBody();
}
