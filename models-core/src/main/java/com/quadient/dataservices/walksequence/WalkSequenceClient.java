package com.quadient.dataservices.walksequence;

import java.util.Objects;

import com.quadient.dataservices.api.Client;
import com.quadient.dataservices.api.Headers;
import com.quadient.dataservices.api.Request;
import com.quadient.dataservices.api.SimpleRequest;
import com.quadient.dataservices.walksequence.model.JobSummary;
import com.quadient.dataservices.walksequence.model.JobSummaryCollection;
import com.quadient.dataservices.walksequence.model.RecordPages;
import com.quadient.dataservices.walksequence.model.Records;
import com.quadient.dataservices.walksequence.model.WalkSequenceJob;
import com.quadient.dataservices.walksequence.model.WalkSequenceJobCreationRequest;

public class WalkSequenceClient {

    private static final String BASE_PATH = "/services/walk-sequence/v1";
    private final Client client;

    public WalkSequenceClient(Client client) {
        this.client = client;
    }

    public WalkSequenceJobSession createJob(WalkSequenceJobCreationRequest request) {
        Objects.requireNonNull(request, "Request cannot be null");
        final Request<JobSummary> req =
                SimpleRequest.post(BASE_PATH + "/jobs", getHeaders(), request, JobSummary.class);
        final JobSummary jobSummary = client.execute(req);
        return new WalkSequenceJobSessionImpl(this, jobSummary.getJobId());
    }

    public JobSummaryCollection getJobs() {
        final Request<JobSummaryCollection> req =
                SimpleRequest.get(BASE_PATH + "/jobs", getHeaders(), JobSummaryCollection.class);
        return client.execute(req);
    }

    public WalkSequenceJobSession getJob(String jobId) {
        Objects.requireNonNull(jobId, "Job ID cannot be null");
        validatePathFragment(jobId);
        final Request<WalkSequenceJob> req =
                SimpleRequest.get(BASE_PATH + "/jobs/" + jobId, getHeaders(), WalkSequenceJob.class);
        final WalkSequenceJob response = client.execute(req);
        final String returnedJobId = response.getJobId();
        return new WalkSequenceJobSessionImpl(this, returnedJobId);
    }

    private Headers getHeaders() {
        return Headers.EMPTY;
    }

    protected void uploadRecords(String jobId, Records records) {
        validatePathFragment(jobId);
        Objects.requireNonNull(records, "Records cannot be null");
        final Request<?> req =
                SimpleRequest.post(BASE_PATH + "/jobs/" + jobId + "/records", getHeaders(), records, Object.class);
        client.execute(req);
    }

    protected void run(String jobId) {
        validatePathFragment(jobId);
        final Request<?> req =
                SimpleRequest.post(BASE_PATH + "/jobs/" + jobId + "/_run", getHeaders(), null, Object.class);
        client.execute(req);
    }

    protected JobSummary getJobSummary(String jobId) {
        validatePathFragment(jobId);
        final Request<JobSummary> req = SimpleRequest.get(BASE_PATH + "/jobs/" + jobId, getHeaders(), JobSummary.class);
        return client.execute(req);
    }

    protected RecordPages getRecordPages(String jobId, String kind) {
        validatePathFragment(jobId);
        validatePathFragment(kind);
        final Request<RecordPages> req = SimpleRequest.get(BASE_PATH + "/jobs/" + jobId + "/records/pages?kind=" + kind,
                getHeaders(), RecordPages.class);
        return client.execute(req);
    }

    protected Records downloadRecords(String jobId, String pageId) {
        validatePathFragment(jobId);
        validatePathFragment(pageId);
        final Request<Records> req =
                SimpleRequest.get(BASE_PATH + "/jobs/" + jobId + "/records/" + pageId, getHeaders(), Records.class);
        return client.execute(req);
    }

    private void validatePathFragment(String fragment) {
        Objects.requireNonNull(fragment, "Argument cannot be null");
        if (fragment.contains("/")) {
            throw new IllegalArgumentException("Argument cannot contain slashes");
        }
        if (fragment.contains(" ")) {
            throw new IllegalArgumentException("Argument cannot contain spaces");
        }
        if (fragment.contains("?")) {
            throw new IllegalArgumentException("Argument cannot contain question marks");
        }
    }

}
