package com.quadient.dataservices.samples.perftest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Stopwatch;
import com.quadient.dataservices.api.Client;
import com.quadient.dataservices.api.FiniteJobStatus;
import com.quadient.dataservices.api.JobCreationDetails;
import com.quadient.dataservices.api.JobSession;
import com.quadient.dataservices.api.Request;
import com.quadient.dataservices.api.ServiceCaller;

public abstract class AbstractPerformanceTest {

    private static final Logger logger = LoggerFactory.getLogger(AbstractPerformanceTest.class);

    private final int numThreads;
    private final Client client;
    private final boolean createJob;

    public AbstractPerformanceTest(Client client, int numThreads, boolean createJob) {
        if (numThreads < 1 || numThreads > 20) {
            throw new IllegalArgumentException("numThreads must be a positive number between 1 and 20.");
        }
        this.client = client;
        this.numThreads = numThreads;
        this.createJob = createJob;
    }

    protected abstract String getServiceId();

    protected abstract Integer getExpectedRecordCount();

    public PerformanceTestState run() {
        final PerformanceTestState testState = new PerformanceTestState();

        if (createJob) {
            final JobSession job = createJob();
            try {
                runInternal(job, job.getJobId(), testState);
            } finally {
                if (testState.isCancelled()) {
                    job.close(FiniteJobStatus.FAILURE);
                    logger.error("Job {} marked as FAILURE", job.getJobId());
                } else {
                    job.close();
                    logger.info("Job {} marked as SUCCESS", job.getJobId());
                }
            }
        } else {
            runInternal(client, "n/a", testState);
        }

        return testState;
    }

    private void runInternal(ServiceCaller serviceCaller, String jobId, PerformanceTestState testState) {
        final PerformanceTestThread[] threads = createThreads(serviceCaller, testState);
        logger.info("{} threads created, starting job '{}'...", numThreads, jobId);
        final Stopwatch stopwatch = Stopwatch.createStarted();
        startAndAwaitThreads(threads);
        stopwatch.stop();
        final long millis = stopwatch.elapsed(TimeUnit.MILLISECONDS);
        logger.info("{} threads finished. {} ms elapsed in total.", numThreads, millis);

        if (!testState.isCancelled()) {
            final DescriptiveStatistics stats = generateStats(threads);
            logger.info("Request timing stats: {}", stats);
        }
    }

    private JobSession createJob() {
        final String origin = "performance-test";
        final Map<String, Object> additionalDetails = new HashMap<>();
        additionalDetails.put("performance-test-class", getClass().getSimpleName());
        final JobCreationDetails jobCreationDetails = new JobCreationDetails(null, origin,
                Arrays.asList(getServiceId()), getExpectedRecordCount(), additionalDetails);
        final JobSession job = client.createJob(jobCreationDetails);
        return job;
    }

    private DescriptiveStatistics generateStats(final PerformanceTestThread[] threads) {
        final DescriptiveStatistics stats = new DescriptiveStatistics();
        for (PerformanceTestThread thread : threads) {
            final List<Long> requestTimings = thread.getRequestTimings();
            requestTimings.forEach(millisAsLong -> {
                final double millisAsDouble = millisAsLong.doubleValue();
                stats.addValue(millisAsDouble);
            });
        }
        return stats;
    }

    private PerformanceTestThread[] createThreads(final ServiceCaller serviceCaller, PerformanceTestState testState) {
        final Queue<Request<?>> queue = createRequestQueue();
        final Supplier<Request<?>> requestSupplier = () -> queue.poll();
        final PerformanceTestThread[] threads = new PerformanceTestThread[numThreads];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new PerformanceTestThread(i, serviceCaller, requestSupplier, testState);
        }
        return threads;
    }

    private void startAndAwaitThreads(final PerformanceTestThread[] threads) {
        for (int i = 0; i < threads.length; i++) {
            threads[i].start();
        }
        for (int i = 0; i < threads.length; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    protected abstract Queue<Request<?>> createRequestQueue();
}
