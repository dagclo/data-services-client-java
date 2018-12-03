package com.quadient.dataservices.samples.perftest;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.metamodel.util.FileHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Stopwatch;
import com.quadient.dataservices.ClientFactory;
import com.quadient.dataservices.api.Client;
import com.quadient.dataservices.api.Credentials;
import com.quadient.dataservices.etl.EtlServiceClient;
import com.quadient.dataservices.etl.EtlServiceClientImpl;
import com.quadient.dataservices.etl.EtlServiceFile;
import com.quadient.dataservices.etl.EtlServiceTable;
import com.quadient.dataservices.etl.FileInformationRequest;
import com.quadient.dataservices.etl.FileQueryRequest;
import com.quadient.dataservices.etl.TableInformationRequest;
import com.quadient.dataservices.etl.model.FileInformation;
import com.quadient.dataservices.etl.model.QueryResponse;
import com.quadient.dataservices.etl.model.TableInformation;
import com.quadient.dataservices.etl.model.TablePage;
import com.quadient.dataservices.etl.model.TableUpdateResponse;
import com.quadient.dataservices.samples.utils.CommandLineArgs;

/**
 * Example performance test class for ETL service.
 * 
 * Beware: Running this class may cost you Quadient Cloud credits.
 */
public class EtlPerformanceTest {

    private static final Logger logger = LoggerFactory.getLogger(EtlPerformanceTest.class);

    public static void main(String[] args) {
        final Credentials credentials = CommandLineArgs.getCredentials(args);
        final Client client = ClientFactory.createClient(credentials);

        final int numColumns = 10;
        final int numRecords = 10_000;

        final EtlPerformanceTest test = new EtlPerformanceTest(client, numColumns, numRecords);
        test.run();
    }

    private final Client client;
    private final int numColumns;
    private final int numRecords;

    public EtlPerformanceTest(Client client, int numColumns, int numRecords) {
        this.client = client;
        this.numColumns = numColumns;
        this.numRecords = numRecords;
    }

    private void run() {
        final Stopwatch stopwatch = Stopwatch.createStarted();
        final EtlServiceClient etlClient = new EtlServiceClientImpl(client);
        final File inFile = createFile();
        try {
            logger.info("{}: Created file '{}' (size {} bytes)", stopwatch, inFile.getCanonicalPath(), inFile.length());
            final EtlServiceFile file = etlClient.createFile(inFile);
            try {
                final String fileId = file.getFileId();
                logger.info("{}: Uploaded file ID: {}", stopwatch, fileId);

                final FileInformation fileInfo2 = client.execute(new FileInformationRequest(fileId));
                logger.info("{}: Confirmed file ID: {}", stopwatch, fileInfo2.getFileId());

                final QueryResponse queryResponse = client.execute(new FileQueryRequest(fileId, 0));
                logger.info("{}: Queried {} records", stopwatch, queryResponse.getRecords().size());

                final InputStream downloadStream = file.download();
                final File outFile1 = File.createTempFile("quadient_data_services_test_out1", "csv");
                try {
                    FileHelper.copy(downloadStream, FileHelper.getOutputStream(outFile1));

                    logger.info("{}: Downloaded file '{}' (size {} bytes)", stopwatch, outFile1.getCanonicalPath(),
                            outFile1.length());
                } finally {
                    outFile1.delete();
                }

                final EtlServiceTable table = file.transformToTable(false);
                final String tableId = table.getTableId();
                logger.info("{}: Created table (from file) '{}'", stopwatch, tableId);
                try {
                    final TableInformation tableInfo2 = client.execute(new TableInformationRequest(tableId));
                    logger.info("{}: Confirmed table ID '{}', pages: {}", stopwatch, tableInfo2.getTableId(),
                            tableInfo2.getPageIds().size());

                    final String pageId = tableInfo2.getPageIds().get(0);
                    final TablePage page = table.getPage(pageId);
                    logger.info("{}: Got table page 1 with {} records", stopwatch, page.getRecords().size());

                    final TableUpdateResponse tableUpdate1 = table.appendFile(inFile);
                    logger.info("{}: Appended another {} pages to table", stopwatch,
                            tableUpdate1.getNewPageIds().size());

                    final List<String> record = new ArrayList<>(numColumns);
                    for (int i = 0; i < numColumns; i++) {
                        record.add("appended_" + i);
                    }
                    final TableUpdateResponse tableUpdate2 = table.appendRecords(Arrays.asList(record));
                    logger.info("{}: Appended another record to table", stopwatch, tableUpdate2.getNewPageIds().size());

                    final InputStream tableAsCsv = table.downloadAsCsv();
                    final File outFile2 = File.createTempFile("quadient_data_services_test_out1", "csv");
                    try {
                        FileHelper.copy(tableAsCsv, FileHelper.getOutputStream(outFile2));

                        logger.info("{}: Downloaded table as CSV '{}' (size {} bytes)", stopwatch,
                                outFile2.getCanonicalPath(), outFile2.length());
                    } finally {
                        outFile2.delete();
                    }
                } finally {
                    table.deleteTable();
                    logger.info("{}: Deleted table '{}'", stopwatch, tableId);
                }
            } finally {
                file.deleteFile();
                logger.info("{}: Deleted file '{}'", stopwatch, file.getFileId());
            }
        } catch (IOException e) {
            logger.info("{}: Bad stuff happened: {}", stopwatch, e.getMessage());
            throw new UncheckedIOException(e);
        } finally {
            inFile.delete();
            stopwatch.stop();
        }
    }

    private File createFile() {
        final Collector<CharSequence, ?, String> join = Collectors.joining(",");
        final File file;
        try {
            file = File.createTempFile("quadient_data_services_test_in", ".csv");
            try (BufferedWriter writer = FileHelper.getBufferedWriter(file)) {
                // write header
                final IntStream headers = IntStream.range(1, numColumns + 1);
                writer.write(headers.mapToObj(i -> "col" + i).collect(join) + '\n');

                final SecureRandom random = new SecureRandom();

                final IntStream records = IntStream.range(1, numRecords + 1);
                final Iterator<String> lines = records.mapToObj(r -> {
                    final IntStream columns = IntStream.range(1, numColumns + 1);
                    return columns.mapToObj(i -> "value" + random.nextInt(1_000_000)).collect(join) + '\n';
                }).iterator();
                while (lines.hasNext()) {
                    writer.write(lines.next());
                }
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return file;
    }
}
