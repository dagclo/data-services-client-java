package com.quadient.dataservices.samples.utils;

import java.net.URI;

import com.quadient.dataservices.api.AdministrativeCredentials;
import com.quadient.dataservices.api.Credentials;
import com.quadient.dataservices.api.HasBaseUri;
import com.quadient.dataservices.api.PreAuthorizedCredentials;
import com.quadient.dataservices.api.QuadientCloudCredentials;
import com.quadient.dataservices.api.Region;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

public class CommandLineArgs {

    @Option(names = { "-u", "--username" }, description = "system username")
    public String userName;

    @Option(names= {"-p", "--password"}, description = "password to use")
    public String password;

    @Option(names = {"-c", "--company"} , description = "quadient cloud company")
    public String company;

    @Option(names = {"-cj", "--create-job"}, description = "decide whether or not to create job while running", defaultValue = "true")
    public Boolean createJob;

    @Option(names = {"--url"}, description = "target url. overrides region")
    public String url;

    @Option(names = {"-r", "--region"}, description = "which quadient cloud region to use.")
    public String region;

    @Option(names = { "-at", "--access-token"})
    public String accessToken;

    @Option(names = { "-tc", "--number-of-threads"}, defaultValue = "2")
    public int numThreads;

    @Option(names = { "-rpr", "--number-of-records-per-request"}, defaultValue = "50")
    public int numRecordsPerRequest;

    @Option(names = { "-rc", "--number-of-requests"}, defaultValue = "100")
    public int numRequests;

    @Parameters(index = "0..*") public String[] PositionalArguments;

    public boolean isValid(){
        return url != null;
    }

    public CommandLineArgs() {}

    public Credentials getCredentials()
    {
        if(url == null && region == null) throw new IllegalArgumentException("--url or --region should be provided");
        URI uri;
        if(url != null){
            uri = URI.create(url);
        }else{
            uri = getBaseUri(region).getBaseUri();
        }

        if(accessToken != null){
            return new PreAuthorizedCredentials(uri, accessToken);
        }

        if(password == null && userName == null){
            throw new IllegalArgumentException("if not providing accesstoken, username and password are required.");
        }

        if(company != null){
            return new QuadientCloudCredentials(uri, company, userName, password.toCharArray());
        }else{
            return new AdministrativeCredentials(uri, userName, password.toCharArray());
        }
    }

    public static Credentials getCredentials(String[] args) {
        if(args == null) throw new IllegalArgumentException(
            "Please provide the following 4 command line args: <region> <company> <username> <password>");
        if (args.length == 4) {
            final HasBaseUri region = getBaseUri(args[0]);
            final String company = args[1];
            final String username = args[2];
            final String password = args[3];
            return new QuadientCloudCredentials(region.getBaseUri(), company, username, password.toCharArray());
        } else if (args.length == 3) {
            final HasBaseUri region = getBaseUri(args[0]);
            final String username = args[1];
            final String password = args[2];
            return new AdministrativeCredentials(region.getBaseUri(), username, password.toCharArray());
        } else if (args.length == 2) {
            return new PreAuthorizedCredentials(URI.create(args[0]), args[1]);
        } else {
            throw new IllegalArgumentException(
                    "Please provide the following 4 command line args: <region> <company> <username> <password>");
        }
    }

    public static HasBaseUri getBaseUri(String argument) {
        return argument.length() == 2 ? Region.valueOf(argument) : () -> URI.create(argument);
    }

}
