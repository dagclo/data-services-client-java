package com.quadient.dataservices.samples.perftest;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.metamodel.csv.CsvConfiguration;
import org.apache.metamodel.csv.CsvDataContext;
import org.apache.metamodel.data.DataSet;
import org.apache.metamodel.data.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.quadient.dataservices.ClientFactory;
import com.quadient.dataservices.api.Client;
import com.quadient.dataservices.api.Credentials;
import com.quadient.dataservices.api.Request;
import com.quadient.dataservices.samples.utils.CommandLineArgs;
import com.quadient.dataservices.sanctionlists.SanctionListCheckRequest;
import com.quadient.dataservices.sanctionlists.model.EntityType;
import com.quadient.dataservices.sanctionlists.model.Gender;
import com.quadient.dataservices.sanctionlists.model.MatchRequest;
import com.quadient.dataservices.sanctionlists.model.MatchRequestAddress;
import com.quadient.dataservices.sanctionlists.model.MatchRequestName;
import com.quadient.dataservices.sanctionlists.model.MatchRequestRecord;

/**
 * Example performance test class for Email validation.
 * 
 * Beware: Running this class may cost you Quadient Cloud credits.
 */
public class SanctionListsPerformanceTest extends AbstractPerformanceTest {

    private static final Logger logger = LoggerFactory.getLogger(SanctionListsPerformanceTest.class);

    private static final String URL_CUSTOMERS_CSV =
            "https://raw.githubusercontent.com/datacleaner/DataCleaner/master/desktop/ui/src/main/resources/datacleaner-home/datastores/customers.csv";

    public static void main(String[] args) throws IOException {
        
        final Credentials credentials = CommandLineArgs.getCredentials(args);
        final Client client = ClientFactory.createClient(credentials);

        final int numThreads = 2;
        final boolean createJob = true;
        final int numRequests = 100;
        final int numRecordsPerRequest = 50;

        final SanctionListsPerformanceTest perfTest =
                new SanctionListsPerformanceTest(client, numThreads, createJob, numRequests, numRecordsPerRequest);
        final PerformanceTestState testState = perfTest.run();

        if (testState.isCancelled()) {
            System.exit(500);
        }

        logger.info("Config:\nThreads: {}\nRequests: {}\nRecords/Request: {}\nRecords total: {}", numThreads,
                numRequests, numRecordsPerRequest, numRecordsPerRequest * numRequests);
    }

    private final int numRequests;
    private final int numRecordsPerRequest;

    public SanctionListsPerformanceTest(Client client, int numThreads, boolean createJob, int numRequests,
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
        return "sanction-lists";
    }

    @Override
    protected Queue<Request<?>> createRequestQueue() {
        final Path customersFile = Paths.get("target/customers-" + LocalDate.now().toString() + ".csv");
        if (!customersFile.toFile().exists()) {
            logger.info("Downloading {}", customersFile);
            try {
                final InputStream in = new URL(URL_CUSTOMERS_CSV).openStream();
                Files.copy(in, customersFile, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }

        final List<MatchRequestRecord> records = new ArrayList<>();

        final CsvDataContext csvDataContext =
                new CsvDataContext(customersFile.toFile(), new CsvConfiguration(1, "UTF-8", ',', '"', '\\'));
        final String[] columnNames = { "given_name", "family_name", "company", "address_line", "post_code", "city",
                "country", "email", "birthdate", "gender" };
        try (DataSet ds = csvDataContext.query().from(csvDataContext.getDefaultSchema().getTable(0)).select(columnNames)
                .execute()) {
            while (ds.next()) {
                final Row row = ds.getRow();
                int i = 0;
                final String givenName = (String) row.getValue(i++);
                final String familyName = (String) row.getValue(i++);
                final String company = (String) row.getValue(i++);
                final String addressLine = (String) row.getValue(i++);
                final String postCode = (String) row.getValue(i++);
                final String city = (String) row.getValue(i++);
                final String country = (String) row.getValue(i++);
                final String email = (String) row.getValue(i++);
                @SuppressWarnings("unused") final String birthDate = (String) row.getValue(i++);
                final String gender = (String) row.getValue(i++);

                if (!Strings.isNullOrEmpty(company)) {
                    // create an org record
                    final MatchRequestRecord orgRecord = new MatchRequestRecord();
                    orgRecord.entityType(EntityType.ORGANIZATION);
                    orgRecord.addNamesItem(new MatchRequestName().organizationName(company));
                    orgRecord.addAddressesItem(new MatchRequestAddress().addAddressLinesItem(addressLine).city(city)
                            .postalCode(postCode).country(country));
                    orgRecord.addEmailAddressesItem(email);
                    records.add(orgRecord);
                }

                if (!Strings.isNullOrEmpty(familyName)) {
                    // create a person record
                    final MatchRequestRecord personRecord = new MatchRequestRecord();
                    personRecord.entityType(EntityType.PERSON);
                    personRecord.addNamesItem(new MatchRequestName().givenName(givenName).familyName(familyName));
                    personRecord.addAddressesItem(new MatchRequestAddress().addAddressLinesItem(addressLine).city(city)
                            .postalCode(postCode).country(country));
                    personRecord.gender(Gender.fromValue(gender));
                    personRecord.addEmailAddressesItem(email);
//                  TODO: personRecord.birthInformation(new MatchRequestBirthInformation().date(birthDate));
                    records.add(personRecord);
                }
            }
        }

        logger.info("Loaded {} records from CSV", records.size());

        final Random r = new Random();
        final ConcurrentLinkedQueue<Request<?>> queue = new ConcurrentLinkedQueue<>();
        for (int i = 0; i < numRequests; i++) {
            queue.add(createRequest(records, r));
        }
        return queue;
    }

    private Request<?> createRequest(List<MatchRequestRecord> records, Random r) {
        final MatchRequest body = new MatchRequest();
        for (int i = 0; i < numRecordsPerRequest; i++) {
            final int recordIndex = r.nextInt(records.size());
            logger.debug("Selected random record index: {}", recordIndex);
            final MatchRequestRecord record = records.get(recordIndex);
            body.addRecordsItem(record);
        }
        return new SanctionListCheckRequest(body);
    }
}
