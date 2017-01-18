package com.solanteq.assingment.services.impl;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.core.io.PathResource;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by Roman_Mashchenko on 1/16/2017.
 */
public class FileWriterServiceTest {


    private static Path path;

    @BeforeClass
    public static void setUp() throws Exception {
        File file = File.createTempFile("input", "tmp");
        path = Paths.get(file.toURI());
        Files.write(path, Arrays.asList("1 2", "3 4"));
    }

    @AfterClass
    public static void tearDown() throws Exception {
        Files.delete(path);
    }

    @Test
    public void shouldWriteSuccessfully() throws Exception {
        FileWriterService<Integer> fileWriterService = new FileWriterService<>(new PathResource(path));
        try {
        fileWriterService.open();
        assertEquals(4, fileWriterService.write(Arrays.asList(1, 2, 3, 4)));
        List<String> expected = new ArrayList<>();
        expected.addAll(Arrays.asList("1", "2", "3", "4"));
        assertEquals(expected, Files.readAllLines(path));
        } finally {
            fileWriterService.close();
        }
    }


}