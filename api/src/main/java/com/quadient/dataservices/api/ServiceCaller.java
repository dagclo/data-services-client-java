package com.quadient.dataservices.api;

public interface ServiceCaller {

    <T> Response<T> safeCall(Request<T> request);

    default <T> T call(Request<T> request) throws UnsuccessfulRequestException {
        return safeCall(request).getBody();
    }
}
