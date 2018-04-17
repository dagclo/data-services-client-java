# Quadient Data Services Client for Java

[![Build Status: Linux](https://travis-ci.org/quadient/data-services-client-java.svg?branch=master)](https://travis-ci.org/quadient/data-services-client-java)

This project provides a client package for [Quadient Data Services](https://www.quadient.com/products/quadient-data-services) for the Java programming language.

The client relies heavily on the following dependencies:

 * Jersey 2 (for HTTP and REST client)
 * Jackson (for JSON serialization and deserialization)
 * Swagger codegen (for model class generation from Swagger files)

## Usage

Here is a simple code snippet demonstrating how to use the client package:

```
import com.quadient.dataservices.api;

...

final Credentials credentials = new QuadientCloudCredentials(US, "company", "username", "password");
final Client client = ClientFactory.createClient(credentials);

response = client.execute(new AddressCorrectionRequest(...));
```

Check out the `sample-app` folder for more comprehensive examples.

## Maven dependency

Use the following dependency:

```
		<dependency>
			<groupId>com.quadient.dataservices</groupId>
			<artifactId>dataservices-client-jersey</artifactId>
		</dependency>
```

In addition, you probably want to add the HK2 dependency which most Jersey clients prefer to use:

```

		<dependency>
			<groupId>org.glassfish.jersey.inject</groupId>
			<artifactId>jersey-hk2</artifactId>
		</dependency>
```
