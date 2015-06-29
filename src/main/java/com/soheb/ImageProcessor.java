package com.soheb;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ImageProcessor {

    private static CliOptionsParser cli;

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("You need to give directories as arguments.");
            return;
        }

        cli = new CliOptionsParser(ImageProcessor.class, args);

        if (cli.isHelp()) return;

        Map<File, String> mainMap = new HashMap<>();

        ImageWalker walker = new ImageWalker(cli);

        for (String arg : cli.getArgs()) {
            final List<File> files = walker.getImageFiles(arg);
            if (!(files != null && !files.isEmpty())) continue;

            cli.info(String.format("File tree found with %d files found", files.size()));

            processImages(mainMap, files);
        }

        cli.info("Data collected, printing results:");

        Outputter out = new Outputter(cli);

        out.write("Directories: " + StringUtils.join(Arrays.asList(args), ", "));
        out.write("------------------------------------------------------------");
        for (Map.Entry<File, String> entry : mainMap.entrySet()) {
            out.write(String.format("File: '%s'\tCommon Color (hex): %s", entry.getKey().getName(), entry.getValue()));
        }
        out.write("------------------------------------------------------------");
        out.write("Total files processed: " + mainMap.keySet().size());
    }

    private static void processImages(Map<File, String> mainMap, List<File> files) {
        if (cli.isParallel()) {
            processImages(mainMap, files.parallelStream());
        } else {
            processImages(mainMap, files.stream());
        }
    }

    private static void processImages(Map<File, String> mainMap, Stream<File> stream) {
        mainMap.putAll(stream.collect(Collectors.toMap(file -> file, ImageProcessor::getHexColour)));
    }

    private static String getHexColour(File file) {
        try {
            return FrequentColourImage.getFrequentColourHex(file);
        } catch (IOException e1) {
            return "[No colours found]";
        }
    }


}
