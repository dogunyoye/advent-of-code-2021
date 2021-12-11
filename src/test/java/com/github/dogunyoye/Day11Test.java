package com.github.dogunyoye;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class Day11Test {

    private int[][] map;

    private static List<String> input;
    private static int length;
    private static int depth;

    private static final int STEPS = 100;

    @BeforeClass
    public static void setUp() throws IOException {
        input = Files.readAllLines(Path.of("src/test/resources/Day11TestInput.txt"));
        length = input.get(0).length();
        depth = input.size();
    }

    @Before
    public void before() {
        map = Day09.generateMap(input);
    }

    @Test
    public void testPartOne() {
        assertEquals(1656, Day11.simulateDumboOctopusFlashes(map, STEPS, length, depth));
    }

    @Test
    public void testPartTwo() {
        assertEquals(195, Day11.findSynchronisedFlash(map, length, depth));
    }
}
