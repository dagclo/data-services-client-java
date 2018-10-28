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
        addParam(sb, "", owner);
        addParam(sb, "", parentId);
        addParam(sb, "", origin);
        addParam(sb, "", originExclude);
        addParam(sb, "", tenantPrefix);
        addParam(sb, "", tenant);
        addParam(sb, "", tenantExclude);
        addParam(sb, "", createdFrom);
        addParam(sb, "", createdTo);
        addParam(sb, "", usageFrom);
        addParam(sb, "", usageTo);
        addParam(sb, "", status);
        addParam(sb, "", statusExclude);
        addParam(sb, "", includeChildJobs);
        addParam(sb, "", limit);
        addParam(sb, "", offset);
        return sb.toString();
    }

    @Override
    public Class<JobListing> getResponseBodyClass() {
        return JobListing.class;
    }
}
