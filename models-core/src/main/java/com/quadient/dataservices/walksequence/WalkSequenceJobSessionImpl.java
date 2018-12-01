package com.quadient.dataservices.walksequence;

import java.util.List;

import com.quadient.dataservices.etl.EtlServiceClient;
import com.quadient.dataservices.etl.EtlServiceClientImpl;
import com.quadient.dataservices.etl.EtlServiceTable;
import com.quadient.dataservices.walksequence.model.JobSummary;
import com.quadient.dataservices.walksequence.model.RecordPages;
import com.quadient.dataservices.walksequence.model.RecordTable;
import com.quadient.dataservices.walksequence.model.RecordTables;
import com.quadient.dataservices.walksequence.model.Records;

class WalkSequenceJobSessionImpl implements WalkSequenceJobSession {

    private final WalkSequenceClient client;
    private final String jobId;
    private RecordTables recordTables;

    public WalkSequenceJobSessionImpl(WalkSequenceClient walkSequenceClient, String jobId) {
        this.client = walkSequenceClient;
        this.jobId = jobId;
        this.recordTables = null;
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
        if (recordTables == null) {
            recordTables = client.getRecordTables(jobId);
        }
        return recordTables;
    }

    @Override
    public EtlServiceTable getInputTable() {
        return getEtlServiceTable("input");
    }

    @Override
    public EtlServiceTable getOutputTable() {
        return getEtlServiceTable("output");
    }

    private EtlServiceTable getEtlServiceTable(String lowerCaseRole) {
        final List<RecordTable> tables = getRecordTables().getTables();
        for (RecordTable table : tables) {
            final String role = table.getRole();
            if (role != null && role.trim().toLowerCase().equals(lowerCaseRole)) {
                return getEtlServiceClient().getTable(table.getId());
            }
        }
        throw new IllegalStateException("Could not find table with role '" + lowerCaseRole + "'.");
    }

    private EtlServiceClient getEtlServiceClient() {
        return new EtlServiceClientImpl(client.getClient());
    }

}
