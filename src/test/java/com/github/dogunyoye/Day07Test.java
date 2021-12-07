package com.github.dogunyoye;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import org.junit.BeforeClass;
import org.junit.Test;

public class Day07Test {

    private static int[] positions;

    @BeforeClass
    public static void setUp() throws IOException {
        final List<String> input = Files.readAllLines(Path.of("src/test/resources/Day07TestInput.txt"));
        positions = Stream.of(input.get(0).split(",")).mapToInt(Integer::parseInt).toArray();
    }

    @Test
    public void testPartOne() {
        assertEquals(37, Day07.calculateFuelCost(positions));
    }

    @Test
    public void testPartTwo() {
        assertEquals(168, Day07.calculateEnchancedFuelCost(positions));
    }
}
