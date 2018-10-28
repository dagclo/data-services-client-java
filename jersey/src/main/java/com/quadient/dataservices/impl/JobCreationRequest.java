package com.quadient.dataservices.impl;

import java.util.ArrayList;

import com.quadient.dataservices.api.JobCreationDetails;
import com.quadient.dataservices.api.Request;
import com.quadient.dataservices.jobs.model.JobInformationResponse;

final class JobCreationRequest implements Request<JobInformationResponse> {

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
        final com.quadient.dataservices.jobs.model.JobCreationRequest body =
                new com.quadient.dataservices.jobs.model.JobCreationRequest();
        body.parentJob(jobRequestDetails.getParentJobId());
        body.origin(jobRequestDetails.getOrigin());

        if (jobRequestDetails.getExpectedServices() != null && !jobRequestDetails.getExpectedServices().isEmpty()) {
            body.expectedServices(new ArrayList<>(jobRequestDetails.getExpectedServices()));
        }

        if (jobRequestDetails.getExpectedRecordCount() != null) {
            body.expectedRecordCount(jobRequestDetails.getExpectedRecordCount().longValue());
        }

        if (jobRequestDetails.getAdditionalDetails() != null && !jobRequestDetails.getAdditionalDetails().isEmpty()) {
            body.additionalDetails(jobRequestDetails.getAdditionalDetails());
        }

        return body;
    }

    @Override
    public Class<JobInformationResponse> getResponseBodyClass() {
        return JobInformationResponse.class;
    }

}
