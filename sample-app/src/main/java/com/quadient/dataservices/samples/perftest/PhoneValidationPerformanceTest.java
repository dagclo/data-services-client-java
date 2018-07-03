package com.quadient.dataservices.samples.perftest;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.quadient.dataservices.ClientFactory;
import com.quadient.dataservices.api.Client;
import com.quadient.dataservices.api.Credentials;
import com.quadient.dataservices.api.Request;
import com.quadient.dataservices.phone.PhoneValidationRequest;
import com.quadient.dataservices.samples.utils.CommandLineArgs;

/**
 * Example performance test class for Phone validation.
 * 
 * Beware: Running this class may cost you Quadient Cloud credits.
 */
public class PhoneValidationPerformanceTest extends AbstractPerformanceTest {

    private static final Logger logger = LoggerFactory.getLogger(PhoneValidationPerformanceTest.class);

    public static void main(String[] args) {
        final Credentials credentials = CommandLineArgs.getCredentials(args);
        final Client client = ClientFactory.createClient(credentials);

        final int numThreads = 4;
        final boolean createJob = true;
        final int numRequests = 500;
        final int numRecordsPerRequest = 2000;

        final PhoneValidationPerformanceTest perfTest =
                new PhoneValidationPerformanceTest(client, numThreads, createJob, numRequests, numRecordsPerRequest);
        final PerformanceTestState testState = perfTest.run();

        if (testState.isCancelled()) {
            System.exit(500);
        }

        logger.info("Config:\nThreads: {}\nRequests: {}\nRecords/Request: {}\nRecords total: {}", numThreads,
                numRequests, numRecordsPerRequest, numRecordsPerRequest * numRequests);
    }

    private final int numRequests;
    private final int numRecordsPerRequest;

    public PhoneValidationPerformanceTest(Client client, int numThreads, boolean createJob, int numRequests,
            int numRecordsPerRequest) {
        super(client, numThreads, createJob);
        this.numRequests = numRequests;
        this.numRecordsPerRequest = numRecordsPerRequest;
    }

    @Override
    protected Integer getExpectedRecordCount() {
        return numRequests * numRecordsPerRequest;
    }

    @Override
    protected String getServiceId() {
        return "phone-validation";
    }

    @Override
    protected Queue<Request<?>> createRequestQueue() {
        final ConcurrentLinkedQueue<Request<?>> queue = new ConcurrentLinkedQueue<>();
        for (int i = 0; i < numRequests; i++) {
            queue.add(createRequest());
        }
        return queue;
    }

    private Request<?> createRequest() {
        final List<String> phoneNumbers = new ArrayList<>();
        final Random r = new Random();
        for (int i = 0; i < numRecordsPerRequest; i++) {
            final StringBuilder phoneBuilder = new StringBuilder();
            for (int j = 0; j < 9; j++) {
                phoneBuilder.append(r.nextInt(9));
            }
            phoneNumbers.add(phoneBuilder.toString());
        }
        return new PhoneValidationRequest("US", phoneNumbers);
    }
}
