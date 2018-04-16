package com.quadient.dataservices;

import com.quadient.dataservices.api.Client;
import com.quadient.dataservices.api.Credentials;
import com.quadient.dataservices.impl.JerseyClient;

public class ClientFactory {

    public static final Client createClient(Credentials credentials) {
        return new JerseyClient(credentials);
    }

    private ClientFactory() {
        // private constructor - prevent instantiation
    }
}
