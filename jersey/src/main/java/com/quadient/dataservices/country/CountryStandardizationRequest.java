package com.quadient.dataservices.country;

import java.util.List;

import com.quadient.dataservices.api.Request;
import com.quadient.dataservices.country.model.CountryStandardizationResponse;

public class CountryStandardizationRequest implements Request<CountryStandardizationResponse> {

    final com.quadient.dataservices.country.model.CountryStandardizationRequest requestBody;

    public CountryStandardizationRequest(List<String> countries) {
        requestBody = new com.quadient.dataservices.country.model.CountryStandardizationRequest();
        for (String country : countries) {
            requestBody.addCountriesItem(country);
        }
    }

    public CountryStandardizationRequest(com.quadient.dataservices.country.model.CountryStandardizationRequest body) {
        this.requestBody = body;
    }

    @Override
    public Class<CountryStandardizationResponse> getResponseBodyClass() {
        return CountryStandardizationResponse.class;
    }

    @Override
    public String getPath() {
        return "/services/address-correction/v1/correct";
    }

    @Override
    public Object getBody() {
        return requestBody;
    }
}
