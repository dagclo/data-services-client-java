package com.quadient.dataservices.api;

import java.io.UncheckedIOException;

import com.quadient.dataservices.exceptions.DataServicesException;

/**
 * Represents a client of Quadient Data Services
 */
public interface Client extends ServiceCaller {

    /**
     * Creates a job in Quadient Data Services.
     * 
     * @return a {@link JobSession} which allows for calling services as part of a job.
     * @throws DataServicesException
     * @throws UncheckedIOException
     */
    JobSession createJob() throws DataServicesException, UncheckedIOException;

    /**
     * Creates a job in Quadient Data Services.
     * 
     * @param jobRequestDetails details of the job to create
     * @return a {@link JobSession} which allows for calling services as part of a job.
     * @throws DataServicesException
     * @throws UncheckedIOException
     */
    JobSession createJob(JobCreationDetails jobRequestDetails) throws DataServicesException, UncheckedIOException;

    /**
     * Resumes a job that already exists in Quadient Data Services.
     * 
     * @param jobId the ID of the previously created job
     * @return a {@link JobSession} which allows for calling services as part of a job.
     * @throws DataServicesException
     * @throws UncheckedIOException
     */
    JobSession resumeJob(String jobId) throws DataServicesException, UncheckedIOException;
}
