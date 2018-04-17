package com.quadient.dataservices.address;

import java.util.List;

import com.quadient.dataservices.address.model.CorrectionRequest;
import com.quadient.dataservices.address.model.CorrectionRequestAddress;
import com.quadient.dataservices.address.model.CorrectionRequestConfiguration;
import com.quadient.dataservices.address.model.CorrectionResponse;
import com.quadient.dataservices.api.Request;

public class AddressCorrectionRequest implements Request<CorrectionResponse> {

    final CorrectionRequest requestBody;

    public AddressCorrectionRequest(CorrectionRequestConfiguration configuration,
            List<CorrectionRequestAddress> addresses) {
        requestBody = new CorrectionRequest();
        requestBody.setConfiguration(configuration);
        requestBody.setAddresses(addresses);
    }

    public AddressCorrectionRequest(List<CorrectionRequestAddress> addresses) {
        requestBody = new CorrectionRequest();
        requestBody.setAddresses(addresses);
    }

    public AddressCorrectionRequest(CorrectionRequest body) {
        this.requestBody = body;
    }

    @Override
    public Class<CorrectionResponse> getResponseBodyClass() {
        return CorrectionResponse.class;
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
