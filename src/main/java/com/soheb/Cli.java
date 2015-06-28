package com.soheb;

import org.apache.commons.cli.*;

/**
 * Created by soheb on 28/06/2015.
 */
public class Cli {

    private boolean loggingAllowed;
    private boolean parallel;
    private boolean showHelp;

    public Cli(String[] args) {
        loggingAllowed = false;
        parallel = true;
        Options options = new Options();
        options.addOption("v", false, "Verbose (Enable logging output)");
        options.addOption("s", false, "Enable single processing of jobs");
        options.addOption("h", "help", false, "Show help");
        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine line = parser.parse(options, args);
            if (line.hasOption("h")) {
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("Main", options);
            }
            loggingAllowed = line.hasOption("v");
            parallel = !line.hasOption("s");
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public boolean isLoggingAllowed() {
        return loggingAllowed;
    }

    public boolean isParallel() {
        return parallel;
    }
}
