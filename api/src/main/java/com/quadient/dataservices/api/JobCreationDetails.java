package com.quadient.dataservices.api;

import java.util.Collection;
import java.util.Map;

public class JobCreationDetails {

    private final String parentJobId;
    private final String origin;
    private final Collection<String> expectedServices;
    private final Integer expectedRecordCount;
    private final Map<String, Object> additionalDetails;

    public JobCreationDetails() {
        this(null, null, null);
    }

    public JobCreationDetails(String origin, Collection<String> expectedServices, Integer expectedRecordCount) {
        this(null, origin, expectedServices, expectedRecordCount, null);
    }

    public JobCreationDetails(String parentJobId, String origin, Collection<String> expectedServices,
            Integer expectedRecordCount, Map<String, Object> additionalDetails) {
        this.parentJobId = parentJobId == null || parentJobId.isEmpty() ? null : origin;
        this.origin = origin == null || origin.isEmpty() ? null : origin;
        this.expectedServices = expectedServices == null || expectedServices.isEmpty() ? null : expectedServices;
        this.expectedRecordCount = expectedRecordCount == null || expectedRecordCount == 0 ? null : expectedRecordCount;
        this.additionalDetails = additionalDetails;
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

    public Map<String, Object> getAdditionalDetails() {
        return additionalDetails;
    }
}
