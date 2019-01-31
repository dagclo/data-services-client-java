package com.quadient.dataservices.samples.utils;

import java.net.URI;

import com.quadient.dataservices.api.AdministrativeCredentials;
import com.quadient.dataservices.api.Credentials;
import com.quadient.dataservices.api.HasBaseUri;
import com.quadient.dataservices.api.PreAuthorizedCredentials;
import com.quadient.dataservices.api.QuadientCloudCredentials;
import com.quadient.dataservices.api.Region;
import picocli.CommandLine.Option;

public class CommandLineArgs {

    @Option(names = { "-u", "--username" }, description = "system username")
    public String userName;

    @Option(names= {"-p", "--password"}, description = "password to use")
    public String password;

    @Option(names = {"-c", "--company"} , description = "quadient cloud company")
    public String company;

    

    public static Credentials getCredentials(String[] args) {
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
