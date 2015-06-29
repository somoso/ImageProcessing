package com.soheb;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * Option to write to both stdout and a file
 * <p>
 * Created by Soheb on 29/06/2015.
 */
public class Outputter {

    private final File file;
    private final CliOptionsParser parser;

    public Outputter(CliOptionsParser parser) {
        this.parser = parser;
        if (parser.getFileOutput() != null) {
            file = new File(parser.getFileOutput().replaceFirst("^~", System.getProperty("user.home")));
        } else {
            file = null;
        }
    }

    public void write(String s) {
        System.out.println(s);
        if (file != null) {
            try {
                FileUtils.writeLines(file, Arrays.asList(new String[]{s}), true);
            } catch (IOException e) {
                parser.severe("Failed to write to file: " + e.getMessage());
            }
        }
    }
}
