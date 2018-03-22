package com.quadient.dataservices.api;

public interface Response<T> {

    int getStatusCode();
    
    Headers getHeaders();
    
    T getBody() throws UnsuccessfulRequestException;
}
