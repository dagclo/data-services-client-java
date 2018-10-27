package com.quadient.dataservices.jobs;

import com.quadient.dataservices.jobs.model.UsageQueryResponse;

public class UsageQueryRequest extends SimpleGetRequest<UsageQueryResponse> {

    private String groupBy;
    private String owner;
    private String jobId;
    private String serviceId;
    private String usageFrom;
    private String usageTo;
    private String tenant;
    private String origin;

    public UsageQueryRequest() {
    }

    public UsageQueryRequest(String groupBy, String owner, String jobId, String serviceId, String usageFrom,
            String usageTo, String tenant, String origin) {
        this.groupBy = groupBy;
        this.owner = owner;
        this.jobId = jobId;
        this.serviceId = serviceId;
        this.usageFrom = usageFrom;
        this.usageTo = usageTo;
        this.tenant = tenant;
        this.origin = origin;
    }

    @Override
    public String getPath() {
        final StringBuilder sb = new StringBuilder("/jobs/v1/_usage");
        addParam(sb, "group_by", groupBy);
        addParam(sb, "owner", owner);
        addParam(sb, "job_id", jobId);
        addParam(sb, "service_id", serviceId);
        addParam(sb, "usage_from", usageFrom);
        addParam(sb, "usage_to", usageTo);
        addParam(sb, "tenant", tenant);
        addParam(sb, "origin", origin);
        return sb.toString();
    }

    @Override
    public Class<UsageQueryResponse> getResponseBodyClass() {
        return UsageQueryResponse.class;
    }

}
