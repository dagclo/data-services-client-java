package com.quadient.dataservices.api;

public enum FiniteJobStatus {

    SUCCESS(JobStatus.SUCCESS), FAILURE(JobStatus.FAILURE), CANCELLED(JobStatus.CANCELLED);
    
    private final JobStatus jobStatus;

    private FiniteJobStatus(JobStatus status) {
        this.jobStatus = status;
    }
    
    public JobStatus getJobStatus() {
        return jobStatus;
    }
}
