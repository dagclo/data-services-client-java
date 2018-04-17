package com.quadient.dataservices.example;

import com.quadient.dataservices.api.Client;
import com.quadient.dataservices.api.Credentials;
import com.quadient.dataservices.api.JobSession;
import com.quadient.dataservices.api.Region;
import com.quadient.dataservices.api.Response;
import com.quadient.dataservices.api.QuadientCloudCredentials;

@SuppressWarnings({ "unused", "null" })
public class ApiConsumerExample {

    // this is not meant to be run, but just meant as a code sample to show API consuming code
    public void exampleWithoutAndWithJobSession() {
        final Credentials creds =
                new QuadientCloudCredentials(Region.US, "quadient", "johndoe", "secret".toCharArray());
        final Client client = null;

        final DummyRequest addrReq1 = new DummyRequest();
        final Response<DummyResponse> addrResp1 = client.executeSafe(addrReq1);
        if (addrResp1.getStatusCode() == 200) {
            System.out.println("Got address correction response 1: " + addrResp1);
        }

        final JobSession jobSession = client.createJob();
        try {
            final DummyRequest addrReq2 = new DummyRequest();
            final DummyResponse addrResp2 = jobSession.execute(addrReq2);
            System.out.println("Got address correction response 2: " + addrResp2);

        } finally {
            jobSession.close();
        }
    }
}
