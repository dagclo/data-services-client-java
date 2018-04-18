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
import com.quadient.dataservices.country.CountryStandardizationRequest;
import com.quadient.dataservices.samples.utils.CommandLineArgs;

/**
 * Performance test class that uses Country standardization.
 */
public class CountryStandardizationPerformanceTest extends AbstractPerformanceTest {

    private static final Logger logger = LoggerFactory.getLogger(CountryStandardizationPerformanceTest.class);

    public static void main(String[] args) {
        final Credentials credentials = CommandLineArgs.getCredentials(args);
        final Client client = ClientFactory.createClient(credentials);

        final int numThreads = 6;
        final int numRequests = 500;
        final int numRecordsPerRequest = 50;

        final CountryStandardizationPerformanceTest perfTest =
                new CountryStandardizationPerformanceTest(client, numThreads, numRequests, numRecordsPerRequest);
        final PerformanceTestState testState = perfTest.run();

        if (testState.isCancelled()) {
            System.exit(500);
        }

        logger.info("Config:\nThreads: {}\nRequests: {}\nRecords/Request: {}\nRecords total: {}", numThreads,
                numRequests, numRecordsPerRequest, numRecordsPerRequest * numRequests);
    }

    private final int numRequests;
    private final int numRecordsPerRequest;

    public CountryStandardizationPerformanceTest(Client client, int numThreads, int numRequests,
            int numRecordsPerRequest) {
        super(client, numThreads);
        this.numRequests = numRequests;
        this.numRecordsPerRequest = numRecordsPerRequest;
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
        final Random random = new Random();
        final List<String> countries = new ArrayList<>(numRecordsPerRequest);
        for (int i = 0; i < numRecordsPerRequest; i++) {
            countries.add(createRecord(random));
        }
        return new CountryStandardizationRequest(countries);
    }

    private static final String[] RANDOM_VALUES =
            new String[] { "United States", "United Kingdom", "Danmark", "Holland", "Sverige", "Norge", "Nederlands",
                    "France", "Germany", "Deutschland", "Italien", "Espana", "Spain", "Mexico", "Canada" };

    private String createRecord(Random r) {
        final int i = r.nextInt(RANDOM_VALUES.length);
        return RANDOM_VALUES[i];
    }
}
