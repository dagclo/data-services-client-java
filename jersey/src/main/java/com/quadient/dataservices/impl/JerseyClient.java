package com.quadient.dataservices.impl;

import javax.ws.rs.client.ClientBuilder;

import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import org.glassfish.jersey.media.multipart.MultiPartFeature;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quadient.dataservices.address.invoker.JSON;
import com.quadient.dataservices.api.AdministrativeCredentials;
import com.quadient.dataservices.api.ApiKeyCredentials;
import com.quadient.dataservices.api.AuthenticatedUser;
import com.quadient.dataservices.api.Client;
import com.quadient.dataservices.api.Credentials;
import com.quadient.dataservices.api.JobCreationDetails;
import com.quadient.dataservices.api.JobSession;
import com.quadient.dataservices.api.QuadientCloudCredentials;
import com.quadient.dataservices.api.Response;
import com.quadient.dataservices.impl.auth.AdministrativeAuthenticatedUser;
import com.quadient.dataservices.impl.auth.AuthorizationHeaderProviderImpl;
import com.quadient.dataservices.impl.auth.OtherAuthenticatedUser;
import com.quadient.dataservices.impl.auth.QuadientCloudAuthenticatedUser;
import com.quadient.dataservices.jobs.model.JobInformationResponse;

public class JerseyClient extends JerseyServiceCaller implements Client {

    public static ClientBuilder defaultClientBuilder() {
        final ObjectMapper objectMapper = new JSON().getContext(null);
        assert objectMapper != null;
        final JacksonJaxbJsonProvider jacksonJsonProvider =
                new JacksonJaxbJsonProvider(objectMapper, JacksonJaxbJsonProvider.DEFAULT_ANNOTATIONS);
        return ClientBuilder.newBuilder().register(jacksonJsonProvider).register(MultiPartFeature.class);
    }

    public JerseyClient(Credentials credentials) {
        this(defaultClientBuilder(), credentials);
    }

    public JerseyClient(ClientBuilder clientBuilder, Credentials credentials) {
        super(clientBuilder, credentials);
    }

    @Override
    public AuthenticatedUser getAuthenticatedUser() {
        final AuthorizationHeaderProviderImpl authorizationHeaderProvider = getAuthorizationHeaderProvider();
        final Credentials credentials = authorizationHeaderProvider.getCredentials();
        if (credentials instanceof QuadientCloudCredentials) {
            return new QuadientCloudAuthenticatedUser(this, authorizationHeaderProvider);
        }
        if (credentials instanceof AdministrativeCredentials || credentials instanceof ApiKeyCredentials) {
            return new AdministrativeAuthenticatedUser(this, authorizationHeaderProvider);
        }
        return new OtherAuthenticatedUser(this, authorizationHeaderProvider, credentials);
    }

    @Override
    public JobSession createJob() {
        return createJob(new JobCreationDetails());
    }

    @Override
    public JobSession resumeJob(String jobId) {
        return new JerseyJobSession(getClientBuilder(), getAuthorizationHeaderProvider(), getBaseUri(), jobId);
    }

    @Override
    public JobSession createJob(JobCreationDetails jobRequestDetails) {
        final Response<JobInformationResponse> response = executeSafe(new JobCreationRequest(jobRequestDetails));
        final JobInformationResponse jobCreationResponse = response.getBody();
        final String jobId = jobCreationResponse.getJobId();
        return new JerseyJobSession(getClientBuilder(), getAuthorizationHeaderProvider(), getBaseUri(), jobId);
    }
}
