package com.soheb;

import javax.activation.MimetypesFileTypeMap;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Main {

    private static final Logger log = Logger.getLogger(Main.class.getName());
    private static Cli cli;

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("You need to give directories as arguments.");
            return;
        }

        cli = new Cli(args);

        Map<File, String> mainMap = new HashMap<>();

        for (String arg : args) {
            if (cli.isLoggingAllowed()) log.info("Parsing argument '" + arg + "'");
            File location = new File(arg);

            if (!location.exists() || !location.isDirectory()) {
                System.out.println("Argument provided isn't a valid directory: " + arg);
                continue;
            }

            if (cli.isLoggingAllowed())
                log.info(String.format("'%s' is a valid directory - checking for image file types", arg));

            final Path path = Paths.get(location.toURI());
            final List<File> files = new ArrayList<>();
            try {
                Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                        if (attrs.isRegularFile()) {
                            File imageFile = file.toFile();
                            String mineType = new MimetypesFileTypeMap().getContentType(imageFile);
                            String type = mineType.split("/")[0];
                            if (type.equals("image")) {
                                if (cli.isLoggingAllowed())
                                    log.info(String.format("Found image file: %s", imageFile.toPath()));
                                files.add(imageFile);
                            }
                        }
                        return FileVisitResult.CONTINUE;
                    }
                });
            } catch (IOException e) {
                if (cli.isLoggingAllowed())
                    log.severe(String.format("Failure while trying to traverse %s: %s", location.toPath(), e.getMessage()));
                continue;
            }

            if (cli.isLoggingAllowed()) log.info(String.format("File tree found with %d files found", files.size()));

            if (cli.isParallel()) {
                mainMap.putAll(files.parallelStream().collect(Collectors.toMap(file -> file, file -> {
                    try {
                        return FrequentColourImage.getFrequentColourHex(file);
                    } catch (IOException e1) {
                        return "[No colours found]";
                    }
                })));
            } else {
                mainMap.putAll(files.stream().collect(Collectors.toMap(file -> file, file -> {
                    try {
                        return FrequentColourImage.getFrequentColourHex(file);
                    } catch (IOException e1) {
                        return "[No colours found]";
                    }
                })));
            }
        }

        if (cli.isLoggingAllowed()) log.info("Data collected, printing results:");

        System.out.println(mainMap);
    }
}
