package com.quadient.dataservices.samples.perftest;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.quadient.dataservices.ClientFactory;
import com.quadient.dataservices.api.Client;
import com.quadient.dataservices.api.Credentials;
import com.quadient.dataservices.api.Request;
import com.quadient.dataservices.samples.utils.CommandLineArgs;
import com.quadient.dataservices.universalparser.UniversalParserRequest;

/**
 * Example performance test class for Universal Parser.
 * 
 * Beware: Running this class may cost you Quadient Cloud credits.
 */
public class UniversalParserPerformanceTest extends AbstractPerformanceTest {

    private static final Logger logger = LoggerFactory.getLogger(UniversalParserPerformanceTest.class);

    public static void main(String[] args) {
        final Credentials credentials = CommandLineArgs.getCredentials(args);
        final Client client = ClientFactory.createClient(credentials);

        final int numThreads = 4;
        final boolean createJob = true;
        final int numRequests = 100;
        final int numRecordsPerRequest = 100;
        
        final UniversalParserPerformanceTest perfTest =
                new UniversalParserPerformanceTest(client, numThreads, createJob, numRequests, numRecordsPerRequest);
        final PerformanceTestState testState = perfTest.run();

        if (testState.isCancelled()) {
            System.exit(500);
        }

        logger.info("Config:\nThreads: {}\nRequests: {}\nRecords/Request: {}\nRecords total: {}", numThreads,
                numRequests, numRecordsPerRequest, numRecordsPerRequest * numRequests);
    }

    private final int numRequests;
    private final int numRecordsPerRequest;

    public UniversalParserPerformanceTest(Client client, int numThreads, boolean createJob, int numRequests,
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
        final List<String> texts = new ArrayList<>();
        for (int i = 0; i < numRecordsPerRequest; i++) {
            texts.add("Kasper Sorensen\nQuadient Data USA\n1301 5th Ave\nSeattle WA 98119\nk.sorensen@quadient.com");
        }
        return new UniversalParserRequest(true, texts);
    }
}
