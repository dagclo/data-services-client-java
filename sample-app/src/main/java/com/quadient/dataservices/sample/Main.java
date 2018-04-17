package com.quadient.dataservices.sample;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.quadient.dataservices.ClientFactory;
import com.quadient.dataservices.address.AddressCorrectionRequest;
import com.quadient.dataservices.address.model.CorrectionRequestAddress;
import com.quadient.dataservices.address.model.CorrectionResponse;
import com.quadient.dataservices.address.model.CorrectionResponseAddress;
import com.quadient.dataservices.address.model.CorrectionResponseRecord;
import com.quadient.dataservices.api.AdministrativeCredentials;
import com.quadient.dataservices.api.Client;
import com.quadient.dataservices.api.Credentials;
import com.quadient.dataservices.api.HasBaseUri;
import com.quadient.dataservices.api.JobSession;
import com.quadient.dataservices.api.QuadientCloudCredentials;
import com.quadient.dataservices.api.Region;

public class Main {

    public static void main(String[] args) throws Exception {
        final Credentials credentials;
        if (args.length == 4) {
            final HasBaseUri region = getRegion(args[0]);
            final String company = args[1];
            final String username = args[2];
            final String password = args[3];
            credentials = new QuadientCloudCredentials(region.getBaseUri(), company, username, password.toCharArray());
        } else if (args.length == 3) {
            final HasBaseUri region = getRegion(args[0]);
            final String username = args[1];
            final String password = args[2];
            credentials = new AdministrativeCredentials(region.getBaseUri(), username, password.toCharArray());
        } else {
            throw new IllegalArgumentException(
                    "Please provide the following 4 command line args: <region> <company> <username> <password>");
        }

        final Client client = ClientFactory.createClient(credentials);

        CorrectionResponse response;

        // call address correction without a job session
        response = client.execute(new AddressCorrectionRequest(getSampleAddresses()));
        response.getAddresses().forEach(addr -> {
            System.out.println("Got response address: " + toOutput(addr));
        });

        // create a job session and call address correction
        final JobSession job = client.createJob();
        System.out.println("Created job: " + job.getJobId());

        response = job.execute(new AddressCorrectionRequest(getSampleAddresses()));
        response.getAddresses().forEach(addr -> {
            System.out.println("Got response address: " + toOutput(addr));
        });

        job.close();
        System.out.println("Finished job: " + job.getJobId());
    }

    public static HasBaseUri getRegion(String argument) {
        return argument.length() == 2 ? Region.valueOf(argument) : () -> URI.create(argument);
    }

    private static String toOutput(CorrectionResponseRecord record) {
        final StringBuilder sb = new StringBuilder();
        sb.append(record.getOutcome().getCategory());
        sb.append(" (");
        final Set<String> codes = record.getOutcome().getCodes().keySet();
        sb.append(codes.stream().collect(Collectors.joining(",")));
        sb.append("): ");
        final CorrectionResponseAddress address = record.getAddress();
        if (address.getOrganization() != null && !address.getOrganization().isEmpty()) {
            sb.append(address.getOrganization() + ". ");
        }
        address.getAddressLines().forEach(line -> {
            sb.append(line + ". ");
        });
        address.getRegionalLines().forEach(line -> {
            sb.append(line + ". ");
        });
        sb.append(address.getCountry());
        return sb.toString();
    }

    private static List<CorrectionRequestAddress> getSampleAddresses() {
        final CorrectionRequestAddress address = new CorrectionRequestAddress();
        address.setAddressLines(Arrays.asList("1301 5th Ave", "Suite 2200"));
        address.setProvinceOrState("WA");
        address.setCity("Seattle");
        address.setPostalCode("98101");
        address.setCountry("US");

        final List<CorrectionRequestAddress> addresses = new ArrayList<>();
        addresses.add(address);
        return addresses;
    }
}
