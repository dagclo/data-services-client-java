package com.quadient.dataservices.samples.perftest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Stopwatch;
import com.quadient.dataservices.api.Request;
import com.quadient.dataservices.api.ServiceCaller;

public class PerformanceTestThread extends Thread {

    private static final Logger logger = LoggerFactory.getLogger(PerformanceTestThread.class);

    private final PerformanceTestState testState;
    private final ServiceCaller serviceCaller;
    private final Supplier<Request<?>> requestSupplier;
    private final List<Long> requestTimings;

    public PerformanceTestThread(int index, ServiceCaller serviceCaller, Supplier<Request<?>> requestSupplier,
            PerformanceTestState testState) {
        super("PerfTest" + String.format("%02d", index));
        this.serviceCaller = serviceCaller;
        this.testState = testState;
        this.requestSupplier = requestSupplier;
        this.requestTimings = new ArrayList<>();
    }

    @Override
    public void run() {
        final Stopwatch stopwatch = Stopwatch.createUnstarted();
        int threadRequestCounter = 0;
        while (!testState.isCancelled()) {
            final Request<?> req = requestSupplier.get();
            if (req == null) {
                // we're done
                break;
            }

            threadRequestCounter++;

            final String requestId = testState.getNextRequestId();

            logger.debug("Req {} - Sending", requestId);
            logger.trace("Req {} - Request body:\n{}", requestId, req);
            stopwatch.start();
            final Object resp;
            try {
                resp = serviceCaller.execute(req);
            } catch (RuntimeException e) {
                final long millis = stopwatch.elapsed(TimeUnit.MILLISECONDS);
                logger.error("Req {} - Exception after {} ms", requestId, millis, e);
                testState.onException(e);
                break;
            }
            stopwatch.stop();
            final long millis = stopwatch.elapsed(TimeUnit.MILLISECONDS);
            stopwatch.reset();
            requestTimings.add(millis);
            logger.info("Req {} - Received after {} ms", requestId, millis);
            logger.trace("Req {} - Response body:\n{}", requestId, resp);

            if (threadRequestCounter == 1 && logger.isInfoEnabled()) {
                // log a sample response for manual validation
                try {
                    final String respAsString = new ObjectMapper().writeValueAsString(resp);
                    logger.info("First response (sample): {}", respAsString);
                } catch (Exception e) {
                    logger.error("Failed to json-encode response for verification: {}", e.getMessage());
                }
            }
        }

        final String requestNoStr = String.format("%04d", threadRequestCounter);
        logger.info("Done. Processed {} requests.", requestNoStr);
    }

    public List<Long> getRequestTimings() {
        return requestTimings;
    }
}
