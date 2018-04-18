package com.quadient.dataservices.samples.perftest;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class PerformanceTestState {

    private final AtomicBoolean cancelled;
    private final AtomicInteger requestIdCounter;

    public PerformanceTestState() {
        cancelled = new AtomicBoolean(false);
        requestIdCounter = new AtomicInteger(0);
    }

    public String getNextRequestId() {
        int requestNo = requestIdCounter.incrementAndGet();
        final String requestNoStr = String.format("%04d", requestNo);
        return requestNoStr;
    }

    public boolean isCancelled() {
        return cancelled.get();
    }

    public void onException(RuntimeException e) {
        cancelled.set(true);
    }

}
