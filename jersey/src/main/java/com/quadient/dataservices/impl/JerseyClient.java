package com.quadient.dataservices.impl;

import javax.ws.rs.client.ClientBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import com.quadient.dataservices.address.invoker.JSON;
import com.quadient.dataservices.api.Client;
import com.quadient.dataservices.api.Credentials;
import com.quadient.dataservices.api.JobCreationDetails;
import com.quadient.dataservices.api.JobSession;
import com.quadient.dataservices.api.Response;

public class JerseyClient extends JerseyServiceCaller implements Client {

    public static ClientBuilder defaultClientBuilder() {
        final ObjectMapper objectMapper = new JSON().getContext(null);
        assert objectMapper != null;
        final JacksonJaxbJsonProvider jacksonJsonProvider =
                new JacksonJaxbJsonProvider(objectMapper, JacksonJaxbJsonProvider.DEFAULT_ANNOTATIONS);
        return ClientBuilder.newBuilder().register(jacksonJsonProvider);
    }

    public JerseyClient(Credentials credentials) {
        this(defaultClientBuilder(), credentials);
    }

    public JerseyClient(ClientBuilder clientBuilder, Credentials credentials) {
        super(clientBuilder, credentials);
    }

    @Override
    public JobSession createJob() {
        return createJob(new JobCreationDetails());
    }

    @Override
    public JobSession createJob(JobCreationDetails jobRequestDetails) {
        final Response<JobCreationResponse> response = executeSafe(new JobCreationRequest(jobRequestDetails));
        final JobCreationResponse jobCreationResponse = response.getBody();
        final String jobId = jobCreationResponse.getJobId();
        return new JerseyJobSession(getClientBuilder(), getAccessTokenProvider(), getBaseUri(), jobId);
    }

}
