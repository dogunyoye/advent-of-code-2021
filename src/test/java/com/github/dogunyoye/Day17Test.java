package com.github.dogunyoye;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

public class Day17Test {

    private static int[] bounds;

    @BeforeClass
    public static void setUp() throws IOException {
        final List<String> input = Files.readAllLines(Path.of("src/test/resources/Day17TestInput.txt"));
        bounds = Day17.getBounds(input);
    }

    @Test
    public void testPartOne() {
        assertEquals(45, Day17.findLargestHeight(bounds));
    }

    @Test
    public void testPartTwo() {
        assertEquals(112, Day17.findDistinctLandedVelocities(bounds));
    }
}
