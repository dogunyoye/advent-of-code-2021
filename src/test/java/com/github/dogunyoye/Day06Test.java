package com.github.dogunyoye;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import org.junit.BeforeClass;
import org.junit.Test;

public class Day06Test {

    private static int[] initialFish;

    @BeforeClass
    public static void setUp() throws IOException {
        final List<String> input = Files.readAllLines(Path.of("src/test/resources/Day06TestInput.txt"));
        initialFish = Stream.of(input.get(0).split(",")).mapToInt(Integer::parseInt).toArray();
    }

    @Test
    public void testPartOne() {
        assertEquals(26, Day06.simulateLanternFish(18, initialFish));
        assertEquals(5934, Day06.simulateLanternFish(80, initialFish));
    }

    @Test
    public void testPartTwo() {
        assertEquals(26984457539L, Day06.simulateLanternFish(256, initialFish));
    }
}
