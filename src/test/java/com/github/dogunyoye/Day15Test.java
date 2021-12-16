package com.github.dogunyoye;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import com.github.dogunyoye.Day15.Coordinate;

import org.junit.BeforeClass;
import org.junit.Test;

public class Day15Test {

    private static int[][] map;
    private static int[][] megaMap;

    private static final  int LENGTH = 10;
    private static final int DEPTH = 10;

    private static final Coordinate START = new Coordinate(0,0);
    private static Coordinate end = new Coordinate(DEPTH-1, LENGTH-1);

    @BeforeClass
    public static void setUp() throws IOException {
        final List<String> input = Files.readAllLines(Path.of("src/test/resources/Day15TestInput.txt"));

        map = Day09.generateMap(input);
        megaMap = Day15.buildMegaMap(map, LENGTH, DEPTH);
    }

    @Test
    public void testPartOne() {
        assertEquals(40, Day15.findLowestTotalRiskPath(map, START, end, LENGTH, DEPTH));
    }

    @Test
    public void testPartTwo() {
        end = new Coordinate((DEPTH * 5) - 1, (LENGTH * 5) - 1);
        assertEquals(315, Day15.djikstra(megaMap, START, end, LENGTH * 5, DEPTH * 5));
    }
}
