package com.soheb;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.List;

/**
 * Set working directory at the root of the project in order to test properly
 * <p>
 * Created by soheb on 29/06/2015.
 */
public class ImageWalkerTest {
    private ImageWalker walker;

    @Before
    public void startup() {
        CliOptionsParser cli = new CliOptionsParser(ImageWalkerTest.class, new String[]{});
        walker = new ImageWalker(cli);
    }

    @Test
    public void testImages() {
        List<File> files = walker.getImageFiles("testfolder");
        Assert.assertEquals(2, files.size());
        for (File file : files) {
            String name = file.getName();
            if (!(name.endsWith(".jpg") || name.endsWith("jpeg"))) {
                Assert.fail("Caught a file that wasn't an image!");
            }
        }
    }
}
