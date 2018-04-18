package com.quadient.dataservices.impl;

import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.quadient.dataservices.api.JobCreationDetails;

class JobCreationRequestBody {
    
    @JsonProperty("parent_job")
    private String parentJob;

    @JsonProperty("origin")
    private String origin;

    @JsonProperty("expected_services")
    private Collection<String> expectedServices;

    @JsonProperty("expected_record_count")
    private Integer expectedRecordCount;

    public JobCreationRequestBody(JobCreationDetails details) {
        if (details == null) {
            details = new JobCreationDetails();
        }
        this.parentJob = details.getParentJobId();
        this.origin = details.getOrigin();
        this.expectedServices = details.getExpectedServices();
        this.expectedRecordCount = details.getExpectedRecordCount();
    }

    public String getOrigin() {
        return origin == null ? "data-services-client-java" : origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public Collection<String> getExpectedServices() {
        return expectedServices;
    }

    public void setExpectedServices(Collection<String> expectedServices) {
        this.expectedServices = expectedServices;
    }

    public Integer getExpectedRecordCount() {
        return expectedRecordCount;
    }

    public void setExpectedRecordCount(Integer expectedRecordCount) {
        this.expectedRecordCount = expectedRecordCount;
    }
    
    public String getParentJob() {
        return parentJob;
    }
    
    public void setParentJob(String parentJob) {
        this.parentJob = parentJob;
    }
}
