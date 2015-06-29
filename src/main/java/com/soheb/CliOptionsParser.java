package com.soheb;

import org.apache.commons.cli.*;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by soheb on 28/06/2015.
 */
public class CliOptionsParser {

    private final Logger logger;
    private String fileOutput;
    private boolean loggingAllowed;
    private boolean parallel;
    private boolean help;
    private CommandLine line;

    public CliOptionsParser(Class clazz, String[] args) {
        logger = Logger.getLogger(clazz.getName());
        Options options = new Options();
        options.addOption("v", false, "Verbose (Enable logging output)");
        options.addOption("s", false, "Enable single processing of jobs");
        options.addOption("f", "file", true, "Write to file");
        options.addOption("h", "help", false, "Show help");
        CommandLineParser parser = new DefaultParser();
        try {
            line = parser.parse(options, args);
            if (line.hasOption("h")) {
                help = true;
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("ImageProcessor", options);
            }
            loggingAllowed = line.hasOption("v");
            parallel = !line.hasOption("s");
            if (line.hasOption("f")) {
                fileOutput = line.getOptionValue("f");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public List<String> getArgs() {
        if (line == null) return new ArrayList<>();

        return line.getArgList();
    }

    public void info(String message) {
        if (loggingAllowed) logger.info(message);
    }

    public void warning(String message) {
        if (loggingAllowed) logger.warning(message);
    }

    public void severe(String message) {
        if (loggingAllowed) logger.severe(message);
    }

    public boolean isParallel() {
        return parallel;
    }

    public String getFileOutput() {
        return fileOutput;
    }

    public boolean isHelp() {
        return help;
    }
}
