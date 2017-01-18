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

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Created by Roman_Mashchenko on 1/16/2017.
 */
public class FileReaderServiceTest {


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
    public void shouldReadSuccessfully() throws Exception {

         FileReaderService<int[]> fileReaderService = new FileReaderService<>(new PathResource(path), (line, lineNumber) -> {
            int[] ints = new int[2];
            String[] input = line.split(" ", 2);
            for (int i = 0; i < ints.length; i++) {
                ints[i] = Integer.parseInt(input[i]);
            }
            return ints;
        }
        );
        fileReaderService.open();
        List<int[]> result = new ArrayList<>();
        int[] input;
        while (null != (input = fileReaderService.read())) {
            result.add(input);
        }
        assertEquals(2, result.size());
        assertArrayEquals(new int[]{1,2}, result.get(0));
        assertArrayEquals(new int[]{3,4}, result.get(1));
        fileReaderService.close();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionNotOpened() throws Exception {
        FileReaderService<int[]> fileReaderService = new FileReaderService<>(new PathResource(path), (line, lineNumber) -> null);
        fileReaderService.read();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfPathNotSet() throws Exception {
        FileReaderService<int[]> fileReaderService = new FileReaderService<>(null, (line, lineNumber) -> null);
        fileReaderService.open();
        fileReaderService.read();
    }

    @Test()
    public void shoulNotTrowExceptionOnCloseIfNotOpened() throws Exception {
        FileReaderService<int[]> fileReaderService = new FileReaderService<>(new PathResource(path), (line, lineNumber) -> null);
        fileReaderService.close();
    }

    @Test()
    public void shoulNotTrowExceptionOnCloseIfClosedTwice() throws Exception {
        FileReaderService<int[]> fileReaderService = new FileReaderService<>(new PathResource(path), (line, lineNumber) -> null);
        fileReaderService.open();
        fileReaderService.close();
    }

}