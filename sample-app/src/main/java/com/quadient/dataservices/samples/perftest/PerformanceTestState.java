package com.quadient.dataservices.samples.perftest;

import java.util.concurrent.atomic.AtomicBoolean;

public class PerformanceTestState {
    
    private final AtomicBoolean cancelled;
    
    public PerformanceTestState() {
        cancelled = new AtomicBoolean(false);
    }

    public boolean isCancelled() {
        return cancelled.get();
    }

    public void onException(RuntimeException e) {
        cancelled.set(true);
    }

}
