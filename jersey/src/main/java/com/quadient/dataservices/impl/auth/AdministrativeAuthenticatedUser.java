package com.quadient.dataservices.impl.auth;

import java.util.Map;

import com.quadient.dataservices.api.AuthorizationHeaderProvider;
import com.quadient.dataservices.api.Request;
import com.quadient.dataservices.api.ServiceCaller;
import com.quadient.dataservices.api.SimpleRequest;

public class AdministrativeAuthenticatedUser extends AbstractAuthenticatedUser {

    public AdministrativeAuthenticatedUser(ServiceCaller serviceCaller,
            AuthorizationHeaderProvider authorizationHeaderProvider) {
        super(serviceCaller, authorizationHeaderProvider);
    }

    @Override
    public double getCreditBalance() {
        final Request<Map<String, ?>> request = SimpleRequest.getUntyped("/user/v1/credit_balance");
        final Map<String, ?> response = getServiceCaller().execute(request);
        final Object creditBalance = response.get("credit_balance");
        if (creditBalance instanceof Number) {
            return ((Number) creditBalance).doubleValue();
        }
        return Double.parseDouble(creditBalance.toString());
    }
}
