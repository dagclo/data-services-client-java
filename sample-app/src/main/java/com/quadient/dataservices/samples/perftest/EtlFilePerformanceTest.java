package com.quadient.dataservices.samples.perftest;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.security.SecureRandom;
import java.util.Iterator;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.metamodel.util.FileHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.quadient.dataservices.ClientFactory;
import com.quadient.dataservices.api.Client;
import com.quadient.dataservices.api.Credentials;
import com.quadient.dataservices.etl.FileDeleteRequest;
import com.quadient.dataservices.etl.FileDownloadRequest;
import com.quadient.dataservices.etl.FileInformationRequest;
import com.quadient.dataservices.etl.FileUploadRequest;
import com.quadient.dataservices.etl.model.FileInformation;
import com.quadient.dataservices.samples.utils.CommandLineArgs;

/**
 * Example performance test class for ETL service.
 * 
 * Beware: Running this class may cost you Quadient Cloud credits.
 */
public class EtlFilePerformanceTest {

    private static final Logger logger = LoggerFactory.getLogger(EtlFilePerformanceTest.class);

    public static void main(String[] args) {
        final Credentials credentials = CommandLineArgs.getCredentials(args);
        final Client client = ClientFactory.createClient(credentials);

        final int numColumns = 10;
        final int numRecords = 1_000;

        final EtlFilePerformanceTest test = new EtlFilePerformanceTest(client, numColumns, numRecords);
        test.run();
    }

    private final Client client;
    private final int numColumns;
    private final int numRecords;

    public EtlFilePerformanceTest(Client client, int numColumns, int numRecords) {
        this.client = client;
        this.numColumns = numColumns;
        this.numRecords = numRecords;
    }

    private void run() {
        final File inFile = createFile();
        try {
            logger.info("Created file '{}' (size {} bytes)", inFile.getCanonicalPath(), inFile.length());
            final FileInformation fileInfo1 = client.execute(new FileUploadRequest(inFile));
            try {
                final String fileId = fileInfo1.getFileId();
                logger.info("Uploaded file ID: {}", fileId);

                final FileInformation fileInfo2 = client.execute(new FileInformationRequest(fileId));
                logger.info("Confirmed file ID: {}", fileInfo2.getFileId());

                final InputStream downloadStream = client.execute(new FileDownloadRequest(fileId));
                final File outFile = File.createTempFile("quadient_data_services_test_out", "csv");
                try {
                    FileHelper.copy(downloadStream, FileHelper.getOutputStream(outFile));

                    logger.info("Downloaded file '{}' (size {} bytes)", outFile.getCanonicalPath(), outFile.length());
                } finally {
                    outFile.delete();
                }
            } finally {
                client.execute(new FileDeleteRequest(fileInfo1.getFileId()));
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } finally {
            inFile.delete();
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
