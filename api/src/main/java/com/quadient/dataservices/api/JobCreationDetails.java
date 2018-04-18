package com.quadient.dataservices.api;

import java.util.Collection;

public class JobCreationDetails {

    private final String parentJobId;
    private final String origin;
    private final Collection<String> expectedServices;
    private final Integer expectedRecordCount;

    public JobCreationDetails() {
        this(null, null, null);
    }

    public JobCreationDetails(String origin, Collection<String> expectedServices, Integer expectedRecordCount) {
        this(null, origin, expectedServices, expectedRecordCount);
    }

    public JobCreationDetails(String parentJobId, String origin, Collection<String> expectedServices,
            Integer expectedRecordCount) {
        this.parentJobId = parentJobId == null || parentJobId.isEmpty() ? null : origin;
        this.origin = origin == null || origin.isEmpty() ? null : origin;
        this.expectedServices = expectedServices == null || expectedServices.isEmpty() ? null : expectedServices;
        this.expectedRecordCount = expectedRecordCount == null || expectedRecordCount == 0 ? null : expectedRecordCount;
    }

    public String getOrigin() {
        return origin;
    }

    public Collection<String> getExpectedServices() {
        return expectedServices;
    }

    public Integer getExpectedRecordCount() {
        return expectedRecordCount;
    }

    public String getParentJobId() {
        return parentJobId;
    }
}
