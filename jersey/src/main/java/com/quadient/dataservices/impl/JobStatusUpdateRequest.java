package com.quadient.dataservices.impl;

import java.util.Collections;
import java.util.Objects;

import com.quadient.dataservices.api.JobStatus;
import com.quadient.dataservices.api.Request;

class JobStatusUpdateRequest implements Request<GenericResponse> {

    private final String jobId;
    private final JobStatus status;

    public JobStatusUpdateRequest(String jobId, JobStatus status) {
        Objects.requireNonNull(jobId);
        Objects.requireNonNull(status);
        this.jobId = jobId;
        this.status = status;
    }

    @Override
    public String getMethod() {
        return "PUT";
    }

    @Override
    public String getPath() {
        return "/jobs/v1/" + jobId + "/job_status";
    }

    @Override
    public Object getBody() {
        return Collections.singletonMap("job_status", status.toString());
    }

    @Override
    public Class<GenericResponse> getResponseBodyClass() {
        return GenericResponse.class;
    }

}
