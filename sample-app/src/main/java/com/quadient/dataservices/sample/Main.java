package com.quadient.dataservices.sample;

import java.net.URI;

import com.quadient.dataservices.ClientFactory;
import com.quadient.dataservices.api.AdministrativeCredentials;
import com.quadient.dataservices.api.Client;
import com.quadient.dataservices.api.Credentials;
import com.quadient.dataservices.api.HasBaseUri;
import com.quadient.dataservices.api.JobSession;
import com.quadient.dataservices.api.QuadientCloudCredentials;
import com.quadient.dataservices.api.Region;
import com.quadient.dataservices.country.CountryStandardizationRequest;
import com.quadient.dataservices.country.model.CountryStandardizationResponse;

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

        CountryStandardizationResponse response;

        // call country standardization without a job session
        response = client.execute(new CountryStandardizationRequest("United States", "United Kingdom"));
        response.getCountries().forEach(c -> {
            System.out.println("Got response country: " + c.getCountry().getIso2());
        });

        // create a job session and call country standardization
        final JobSession job = client.createJob();
        System.out.println("Created job: " + job.getJobId());

        response = client.execute(new CountryStandardizationRequest("Danmark", "Holland"));
        response.getCountries().forEach(c -> {
            System.out.println("Got response country: " + c.getCountry().getIso2());
        });

        job.close();
        System.out.println("Finished job: " + job.getJobId());
    }

    public static HasBaseUri getRegion(String argument) {
        return argument.length() == 2 ? Region.valueOf(argument) : () -> URI.create(argument);
    }
}
