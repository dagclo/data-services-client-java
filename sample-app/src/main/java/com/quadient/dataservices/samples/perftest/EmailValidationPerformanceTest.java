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
import com.quadient.dataservices.email.EmailValidationRequest;
import com.quadient.dataservices.email.model.EmailValidationRequestConfiguration.ProcessingStyleEnum;
import com.quadient.dataservices.samples.utils.CommandLineArgs;

/**
 * Example performance test class for Email validation.
 * 
 * Beware: Running this class may cost you Quadient Cloud credits.
 */
public class EmailValidationPerformanceTest extends AbstractPerformanceTest {

    private static final Logger logger = LoggerFactory.getLogger(EmailValidationPerformanceTest.class);

    public static void main(String[] args) {
        final Credentials credentials = CommandLineArgs.getCredentials(args);
        final Client client = ClientFactory.createClient(credentials);

        final int numThreads = 4;
        final boolean createJob = true;
        final int numRequests = 100;
        final int numRecordsPerRequest = 50;
        final ProcessingStyleEnum processingStyle = ProcessingStyleEnum.FAST;

        final EmailValidationPerformanceTest perfTest = new EmailValidationPerformanceTest(client, numThreads,
                createJob, numRequests, numRecordsPerRequest, processingStyle);
        final PerformanceTestState testState = perfTest.run();

        if (testState.isCancelled()) {
            System.exit(500);
        }

        logger.info("Config:\nThreads: {}\nProcessing style: {}\nRequests: {}\nRecords/Request: {}\nRecords total: {}",
                numThreads, processingStyle, numRequests, numRecordsPerRequest, numRecordsPerRequest * numRequests);
    }

    private final int numRequests;
    private final int numRecordsPerRequest;
    private final ProcessingStyleEnum processingStyle;

    public EmailValidationPerformanceTest(Client client, int numThreads, boolean createJob, int numRequests,
            int numRecordsPerRequest, ProcessingStyleEnum processingStyle) {
        super(client, numThreads, createJob);
        this.numRequests = numRequests;
        this.numRecordsPerRequest = numRecordsPerRequest;
        this.processingStyle = processingStyle;
    }

    @Override
    protected Integer getExpectedRecordCount() {
        return numRequests * numRecordsPerRequest;
    }

    @Override
    protected String getServiceId() {
        return "email-validation";
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
        final List<String> emails = new ArrayList<>(numRecordsPerRequest);
        for (int i = 0; i < numRecordsPerRequest; i++) {
            emails.add(createRecord(random));
        }
        return new EmailValidationRequest(processingStyle, emails);
    }

    private static final String[] RANDOM_USERNAMES = new String[] { "webmaster", "k.sorensen", "info", "r.larson",
            "z.rochler", "s.kao", "s.tun", "k4ljl4kj32l4kj2l4", "n/a" };
    private static final String[] RANDOM_SEPARATORS = new String[] { "@", "@", "@", "@", "@", "@", "@", "@", "@", "@",
            "@", "@", "@", "[at]", "[AT]", "{at}", " " };
    private static final String[] RANDOM_DOMAINS =
            new String[] { "gmail.com", "hotmail.com", "live.com", "quadient.com", "outlook.com", "mailinator.com" };

    private String createRecord(Random r) {
        final String username = RANDOM_USERNAMES[r.nextInt(RANDOM_USERNAMES.length)];
        final String sep = RANDOM_SEPARATORS[r.nextInt(RANDOM_SEPARATORS.length)];
        final String domain = RANDOM_DOMAINS[r.nextInt(RANDOM_DOMAINS.length)];
        return username + sep + domain;
    }
}
