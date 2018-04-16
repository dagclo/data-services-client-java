package com.quadient.dataservices.impl;

import com.quadient.dataservices.api.JobCreationDetails;
import com.quadient.dataservices.api.Request;

final class JobCreationRequest implements Request<JobCreationResponse> {

    private final JobCreationDetails jobRequestDetails;

    public JobCreationRequest(JobCreationDetails jobRequestDetails) {
        this.jobRequestDetails = jobRequestDetails;
    }

    @Override
    public String getPath() {
        return "/jobs/v1";
    }

    @Override
    public Object getBody() {
        return new JobCreationRequestBody(jobRequestDetails);
    }

    @Override
    public Class<JobCreationResponse> getResponseBodyClass() {
        return JobCreationResponse.class;
    }

}
