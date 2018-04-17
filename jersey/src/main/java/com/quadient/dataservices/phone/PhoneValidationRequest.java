package com.quadient.dataservices.phone;

import java.util.List;

import com.quadient.dataservices.api.Request;
import com.quadient.dataservices.phone.model.PhoneValidationRequestConfiguration;
import com.quadient.dataservices.phone.model.PhoneValidationRequestRecord;
import com.quadient.dataservices.phone.model.PhoneValidationResponse;

public class PhoneValidationRequest implements Request<PhoneValidationResponse> {

    final com.quadient.dataservices.phone.model.PhoneValidationRequest requestBody;

    public PhoneValidationRequest(String country, List<String> phoneNumbers) {
        requestBody = new com.quadient.dataservices.phone.model.PhoneValidationRequest();
        requestBody.setConfiguration(new PhoneValidationRequestConfiguration().defaultCountry(country));
        for (String phoneNumber : phoneNumbers) {
            final PhoneValidationRequestRecord record = new PhoneValidationRequestRecord().phoneNumber(phoneNumber);
            requestBody.getPhoneNumbers().add(record);
        }
    }

    public PhoneValidationRequest(PhoneValidationRequestConfiguration configuration,
            List<PhoneValidationRequestRecord> phoneNumbers) {
        requestBody = new com.quadient.dataservices.phone.model.PhoneValidationRequest();
        requestBody.setConfiguration(configuration);
        requestBody.setPhoneNumbers(phoneNumbers);
    }

    public PhoneValidationRequest(com.quadient.dataservices.phone.model.PhoneValidationRequest body) {
        this.requestBody = body;
    }

    @Override
    public Class<PhoneValidationResponse> getResponseBodyClass() {
        return PhoneValidationResponse.class;
    }

    @Override
    public String getPath() {
        return "/services/phone-validation/v1/validate";
    }

    @Override
    public Object getBody() {
        return requestBody;
    }
}
