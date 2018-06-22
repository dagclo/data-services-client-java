package com.quadient.dataservices.samples.perftest;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.metamodel.csv.CsvDataContext;
import org.apache.metamodel.data.DataSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.quadient.dataservices.ClientFactory;
import com.quadient.dataservices.api.Client;
import com.quadient.dataservices.api.Credentials;
import com.quadient.dataservices.api.Request;
import com.quadient.dataservices.name.NameValidationParseRequest;
import com.quadient.dataservices.name.model.ParseRequest;
import com.quadient.dataservices.name.model.ParseRequestItem;
import com.quadient.dataservices.samples.utils.CommandLineArgs;

/**
 * Example performance test class for Email validation.
 * 
 * Beware: Running this class may cost you Quadient Cloud credits.
 */
public class NameValidationPerformanceTest extends AbstractPerformanceTest {

    private static final Logger logger = LoggerFactory.getLogger(NameValidationPerformanceTest.class);

    public static void main(String[] args) {
        final Credentials credentials = CommandLineArgs.getCredentials(args);
        final Client client = ClientFactory.createClient(credentials);

        final int numThreads = 4;
        final boolean createJob = true;
        final int numRequests = 50;
        final int numRecordsPerRequest = 500;

        final NameValidationPerformanceTest perfTest =
                new NameValidationPerformanceTest(client, numThreads, createJob, numRequests, numRecordsPerRequest);
        final PerformanceTestState testState = perfTest.run();

        if (testState.isCancelled()) {
            System.exit(500);
        }

        logger.info("Config:\nThreads: {}\nRequests: {}\nRecords/Request: {}\nRecords total: {}", numThreads,
                numRequests, numRecordsPerRequest, numRecordsPerRequest * numRequests);
    }

    private final int numRequests;
    private final int numRecordsPerRequest;

    public NameValidationPerformanceTest(Client client, int numThreads, boolean createJob, int numRequests,
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
        return "email-validation";
    }

    @Override
    protected Queue<Request<?>> createRequestQueue() {
        final CsvDataContext dataContext = new CsvDataContext(new File("c:/dev/customers.csv"));
        final List<String> names = new ArrayList<>();
        try (DataSet dataSet = dataContext.query().from(dataContext.getDefaultSchema().getTable(0))
                .select("given_name", "family_name").execute()) {
            while (dataSet.next()) {
                final String name = dataSet.getRow().getValue(0) + " " + dataSet.getRow().getValue(1);
                names.add(name);
            }
        }

        final ConcurrentLinkedQueue<Request<?>> queue = new ConcurrentLinkedQueue<>();
        for (int i = 0; i < numRequests; i++) {
            queue.add(createRequest(names));
        }
        return queue;
    }

    private Request<?> createRequest(List<String> names) {
        final ParseRequest body = new ParseRequest();
        final Random r = new Random();
        for (int i = 0; i < numRecordsPerRequest; i++) {
            final String randomName = names.get(r.nextInt(names.size()));
            final ParseRequestItem record = new ParseRequestItem();
            record.setUnstructuredName(randomName);
            body.addRecordsItem(record);
        }
        return new NameValidationParseRequest(body);
    }
}
