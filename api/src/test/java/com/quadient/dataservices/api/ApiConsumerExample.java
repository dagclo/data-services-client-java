package com.quadient.dataservices.api;

import com.quadient.dataservices.address.AddressCorrectionRequest;
import com.quadient.dataservices.address.AddressCorrectionResponse;

@SuppressWarnings({ "unused", "null" })
public class ApiConsumerExample {

    // this is not meant to be run, but just meant as a code sample to show API consuming code
    public void exampleWithoutAndWithJobSession() {
        final Credentials creds =
                new UsernamePasswordCredentials(Region.US, "quadient", "johndoe", "secret".toCharArray());
        final Client client = null;

        final AddressCorrectionRequest addrReq1 = new AddressCorrectionRequest();
        final Response<AddressCorrectionResponse> addrResp1 = client.safeCall(addrReq1);
        if (addrResp1.getStatusCode() == 200) {
            System.out.println("Got address correction response 1: " + addrResp1);
        }

        final JobSession jobSession = client.createJob();
        try {
            final AddressCorrectionRequest addrReq2 = new AddressCorrectionRequest();
            final AddressCorrectionResponse addrResp2 = jobSession.call(addrReq2);
            System.out.println("Got address correction response 2: " + addrResp2);

        } finally {
            jobSession.close();
        }
    }
}
