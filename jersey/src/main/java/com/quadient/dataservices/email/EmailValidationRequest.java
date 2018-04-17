package com.quadient.dataservices.email;

import java.util.List;

import com.quadient.dataservices.api.Request;
import com.quadient.dataservices.email.model.EmailAddress;
import com.quadient.dataservices.email.model.EmailValidationRequestConfiguration;
import com.quadient.dataservices.email.model.EmailValidationRequestConfiguration.ProcessingStyleEnum;
import com.quadient.dataservices.phone.model.PhoneValidationResponse;

public class EmailValidationRequest implements Request<PhoneValidationResponse> {

    final com.quadient.dataservices.email.model.EmailValidationRequest requestBody;

    public EmailValidationRequest(com.quadient.dataservices.email.model.EmailValidationRequest body) {
        this.requestBody = body;
    }

    public EmailValidationRequest(ProcessingStyleEnum processingStyle, List<String> emails) {
        requestBody = new com.quadient.dataservices.email.model.EmailValidationRequest();
        requestBody.setConfiguration(new EmailValidationRequestConfiguration().processingStyle(processingStyle));
        for (String email : emails) {
            final EmailAddress record = new EmailAddress().email(email);
            requestBody.addEmailRecordsItem(record);
        }
    }

    @Override
    public Class<PhoneValidationResponse> getResponseBodyClass() {
        return PhoneValidationResponse.class;
    }

    @Override
    public String getPath() {
        return "/services/email-validation/v1/validate";
    }

    @Override
    public Object getBody() {
        return requestBody;
    }
}
