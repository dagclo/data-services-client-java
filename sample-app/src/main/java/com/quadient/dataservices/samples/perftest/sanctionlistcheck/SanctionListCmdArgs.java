package com.quadient.dataservices.samples.perftest.sanctionlistcheck;

import java.util.List;

import com.quadient.dataservices.samples.utils.CommandLineArgs;

import picocli.CommandLine.Option;

public class SanctionListCmdArgs extends CommandLineArgs {
	@Option(names = { "-l", "--sourceList" }, description = "comma seperated list of source lists", defaultValue = "US_OFAC,EU_EEAS", split = ",")
    public List<String> sourceLists;
}