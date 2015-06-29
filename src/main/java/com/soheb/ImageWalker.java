package com.soheb;

import javax.activation.MimetypesFileTypeMap;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles walking through a given directory and finding all suitable files inside it
 * <p>
 * Created by Soheb on 29/06/2015.
 */
public class ImageWalker {

    private final CliOptionsParser cli;

    public ImageWalker(CliOptionsParser cli) {
        this.cli = cli;
    }

    public List<File> getImageFiles(String directory) {
        cli.info("Parsing argument '" + directory + "'");
        File location = new File(directory);

        if (!location.exists() || !location.isDirectory()) {
            System.out.println("Argument provided isn't a valid directory: " + directory);
            return null;
        }

        cli.info(String.format("'%s' is a valid directory - checking for image file types", directory));

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
                            cli.info(String.format("Found image file: %s", imageFile.toPath()));
                            files.add(imageFile);
                        }
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            cli.severe(String.format("Failure while trying to traverse %s: %s", location.toPath(), e.getMessage()));
            return null;
        }
        return files;
    }
}
