package com.github.dogunyoye;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Unit test for Day01.
 */
public class Day01Test {

    private static int[] depths;

    @BeforeClass
    public static void setUp() throws IOException {
        depths = Files.readAllLines(Path.of("src/test/resources/Day01TestInput.txt"))
            .stream()
            .mapToInt(num -> Integer.parseInt(num))
            .toArray();
    }

    @Test
    public void testPartOne() throws IOException {
        assertEquals(7, Day01.calculateIncreases(depths));
    }

    @Test
    public void testPartTwo() throws IOException {
        assertEquals(5, Day01.calculateIncreasesSlidingWindow(depths));
    }
}
