package com.quadient.dataservices.api;

/**
 * Represents a client of Quadient Data Services
 */
public interface Client extends ServiceCaller {

    JobSession createJob();

    JobSession createJob(JobCreationDetails jobRequestDetails);
}
