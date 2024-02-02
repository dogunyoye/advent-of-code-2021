package com.github.dogunyoye;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

public class Day19Test {

    private static List<String> input;

    @BeforeClass
    public static void setUp() throws IOException {
        input = Files.readAllLines(Path.of("src/test/resources/Day19TestInput.txt"));
    }

    @Test
    public void testPartOne() {
        assertEquals(79, Day19.findNumberOfBeacons(input));
    }

    @Test
    public void testPartTwo() {
        assertEquals(3621, Day19.largestManhattanDistanceBetweenScanners(input));
    }
}
