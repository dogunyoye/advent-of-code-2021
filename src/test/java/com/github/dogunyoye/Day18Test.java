package com.github.dogunyoye;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

public class Day18Test {

    private static List<String> input;

    @BeforeClass
    public static void setUp() throws IOException {
        input = Files.readAllLines(Path.of("src/test/resources/Day18TestInput.txt"));
    }

    @Test
    public void testPartOne() {
        assertEquals(4140, Day18.calculateMagnitude(input));
    }

    @Test
    public void testPartTwo() {
        assertEquals(3993, Day18.findHighestMagnitudeOfTwoUniqueSnailfishNumbers(input));
    }
}
