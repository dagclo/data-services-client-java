package com.quadient.dataservices.jobs;

import com.quadient.dataservices.jobs.model.JobDetailedResponse;

public class JobDetailsRequest extends SimpleGetRequest<JobDetailedResponse> {

    private final String jobId;

    public JobDetailsRequest(String jobId) {
        this.jobId = jobId;
    }

    @Override
    public String getPath() {
        return "/jobs/v1/" + jobId;
    }

    @Override
    public Class<JobDetailedResponse> getResponseBodyClass() {
        return JobDetailedResponse.class;
    }

}
