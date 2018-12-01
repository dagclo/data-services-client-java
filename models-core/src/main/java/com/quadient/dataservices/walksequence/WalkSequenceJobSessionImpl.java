package com.quadient.dataservices.walksequence;

import com.quadient.dataservices.walksequence.model.JobSummary;
import com.quadient.dataservices.walksequence.model.RecordPages;
import com.quadient.dataservices.walksequence.model.RecordTables;
import com.quadient.dataservices.walksequence.model.Records;

class WalkSequenceJobSessionImpl implements WalkSequenceJobSession {

    private final WalkSequenceClient client;
    private final String jobId;

    public WalkSequenceJobSessionImpl(WalkSequenceClient walkSequenceClient, String jobId) {
        this.client = walkSequenceClient;
        this.jobId = jobId;
    }

    @Override
    public String getJobId() {
        return jobId;
    }

    @Override
    public void uploadRecords(Records records) {
        client.uploadRecords(jobId, records);
    }

    @Override
    public void run() {
        client.run(jobId);
    }

    @Override
    public JobSummary getJobSummary() {
        return client.getJobSummary(jobId);
    }

    @Override
    public RecordPages getInputRecordPages() {
        return client.getRecordPages(jobId, "input");
    }

    @Override
    public RecordPages getOutputRecordPages() {
        return client.getRecordPages(jobId, "output");
    }

    @Override
    public Records downloadRecords(String pageId) {
        return client.downloadRecords(jobId, pageId);
    }

    @Override
    public RecordTables getRecordTables() {
        return client.getRecordTables(jobId);
    }
}
