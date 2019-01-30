package com.quadient.dataservices.samples.example;

import com.quadient.dataservices.ClientFactory;
import com.quadient.dataservices.api.AuthenticatedUser;
import com.quadient.dataservices.api.Client;
import com.quadient.dataservices.api.Credentials;
import com.quadient.dataservices.api.JobSession;
import com.quadient.dataservices.country.CountryStandardizationRequest;
import com.quadient.dataservices.country.model.CountryStandardizationResponse;
import com.quadient.dataservices.samples.utils.CommandLineArgs;

public class CountryStandardizationExample {

    public static void main(String[] args) throws Exception {
        final Credentials credentials = CommandLineArgs.getCredentials(args);

        final Client client = ClientFactory.createClient(credentials);
        
        System.out.println("Authenticated User:");
        final AuthenticatedUser authenticatedUser = client.getAuthenticatedUser();
        System.out.println(" - User ID: " + authenticatedUser.getUserId());
        System.out.println(" - Credit balance: " + authenticatedUser.getCreditBalance());
        
        authenticatedUser.validateCredentials();
        
        CountryStandardizationResponse response;

        // call country standardization without a job session
        response = client.execute(new CountryStandardizationRequest("United States", "United Kingdom"));
        response.getCountries().forEach(c -> {
            System.out.println("Got response country: " + c.getCountry().getIso2());
        });

        // create a job session and call country standardization
        final JobSession job = client.createJob();
        System.out.println("Created job: " + job.getJobId());

        response = job.execute(new CountryStandardizationRequest("Danmark", "Holland"));
        response.getCountries().forEach(c -> {
            System.out.println("Got response country: " + c.getCountry().getIso2());
        });

        job.close();
        System.out.println("Finished job: " + job.getJobId());
    }
}
