package com.quadient.dataservices.address;

import java.util.List;

import com.quadient.dataservices.address.model.CorrectionRequest;
import com.quadient.dataservices.address.model.CorrectionRequestAddress;
import com.quadient.dataservices.address.model.CorrectionResponse;
import com.quadient.dataservices.api.Request;

public class AddressCorrectionRequest implements Request<CorrectionResponse> {

    final CorrectionRequest correctionRequest;

    public AddressCorrectionRequest(List<CorrectionRequestAddress> addresses) {
        this.correctionRequest = new CorrectionRequest();
        this.correctionRequest.setAddresses(addresses);
    }

    @Override
    public Class<CorrectionResponse> getResponseBodyClass() {
        return CorrectionResponse.class;
    }

    public AddressCorrectionRequest(CorrectionRequest body) {
        this.correctionRequest = body;
    }

    @Override
    public String getPath() {
        return "/services/address-correction/v1/correct";
    }

    @Override
    public Object getBody() {
        return correctionRequest;
    }
}
