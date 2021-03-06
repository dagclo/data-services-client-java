package com.quadient.dataservices.api;

import java.io.Closeable;

public interface JobSession extends ServiceCaller, Closeable {
    
    String getJobId();
    
    void updateJobStatus(JobStatus status);
    
    void close(FiniteJobStatus status);
    
    @Override
    default void close() {
        close(FiniteJobStatus.SUCCESS);
    }
}
