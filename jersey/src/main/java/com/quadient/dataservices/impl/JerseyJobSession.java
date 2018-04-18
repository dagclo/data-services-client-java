package com.quadient.dataservices.impl;

import java.net.URI;
import java.util.Objects;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation.Builder;

import com.quadient.dataservices.api.FiniteJobStatus;
import com.quadient.dataservices.api.JobSession;
import com.quadient.dataservices.api.JobStatus;
import com.quadient.dataservices.api.Response;
import com.quadient.dataservices.impl.auth.AccessTokenProviderImpl;

final class JerseyJobSession extends JerseyServiceCaller implements JobSession {

    private final String jobId;

    public JerseyJobSession(ClientBuilder clientBuilder, AccessTokenProviderImpl accessTokenProvider, URI baseUri,
            String jobId) {
        super(clientBuilder, accessTokenProvider, baseUri);
        Objects.requireNonNull(jobId);
        this.jobId = jobId;
    }

    @Override
    public String getJobId() {
        return jobId;
    }

    @Override
    protected void interceptRequestBefore(Builder requestBuilder) {
        requestBuilder.header("Job-ID", jobId);
    }

    @Override
    public void updateJobStatus(JobStatus status) {
        final JobStatusUpdateRequest request = new JobStatusUpdateRequest(jobId, status);
        final Response<GenericResponse> response = fireRequest(request);
        response.getBody(); // provoke any exceptions by calling getBody()
    }

    @Override
    public void close(FiniteJobStatus status) {
        updateJobStatus(status.getJobStatus());
    }

}
