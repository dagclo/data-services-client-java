package com.quadient.dataservices.api;

import com.quadient.dataservices.exceptions.DataServicesException;

public interface Response<T> {

    int getStatusCode();
    
    Headers getHeaders();
    
    T getBody() throws DataServicesException;
}
