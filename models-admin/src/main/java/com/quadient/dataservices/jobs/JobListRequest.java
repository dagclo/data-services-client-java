package com.quadient.dataservices.jobs;

import com.quadient.dataservices.jobs.model.JobListing;

public class JobListRequest extends SimpleGetRequest<JobListing> {

    private String owner;
    private String parentId;
    private String origin;
    private String originExclude;
    private String tenantPrefix;
    private String tenant;
    private String tenantExclude;
    private String createdFrom;
    private String createdTo;
    private String usageFrom;
    private String usageTo;
    private String status;
    private String statusExclude;
    private Boolean includeChildJobs;
    private Integer limit;
    private Integer offset;

    public JobListRequest() {
    }

    public JobListRequest(String owner, String parentId, String origin, String originExclude, String tenantPrefix,
            String tenant, String tenantExclude, String createdFrom, String createdTo, String usageFrom, String usageTo,
            String status, String statusExclude, Boolean includeChildJobs, Integer limit, Integer offset) {
        this.owner = owner;
        this.parentId = parentId;
        this.origin = origin;
        this.originExclude = originExclude;
        this.tenantPrefix = tenantPrefix;
        this.tenant = tenant;
        this.tenantExclude = tenantExclude;
        this.createdFrom = createdFrom;
        this.createdTo = createdTo;
        this.usageFrom = usageFrom;
        this.usageTo = usageTo;
        this.status = status;
        this.statusExclude = statusExclude;
        this.includeChildJobs = includeChildJobs;
        this.limit = limit;
        this.offset = offset;
    }

    @Override
    public String getPath() {
        final StringBuilder sb = new StringBuilder("/jobs/v1");
        addParam(sb, "owner", owner);
        addParam(sb, "parent_id", parentId);
        addParam(sb, "origin", origin);
        addParam(sb, "origin_exclude", originExclude);
        addParam(sb, "tenant_prefix", tenantPrefix);
        addParam(sb, "tenant", tenant);
        addParam(sb, "tenant_exclude", tenantExclude);
        addParam(sb, "created_from", createdFrom);
        addParam(sb, "created_to", createdTo);
        addParam(sb, "usage_from", usageFrom);
        addParam(sb, "usage_to", usageTo);
        addParam(sb, "status", status);
        addParam(sb, "status_exclude", statusExclude);
        addParam(sb, "include_child_jobs", includeChildJobs);
        addParam(sb, "limit", limit);
        addParam(sb, "offset", offset);
        return sb.toString();
    }

    @Override
    public Class<JobListing> getResponseBodyClass() {
        return JobListing.class;
    }
}
