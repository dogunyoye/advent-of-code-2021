package com.github.dogunyoye;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import com.github.dogunyoye.Day05.Coordinate;

import org.junit.BeforeClass;
import org.junit.Test;

public class Day09Test {

    private static int[][] map;
    private static int length;
    private static int depth;
    private static List<Coordinate> lowPoints;

    @BeforeClass
    public static void setUp() throws IOException {
        final List<String> input = Files.readAllLines(Path.of("src/test/resources/Day09TestInput.txt"));
        length = input.get(0).length();
        depth = input.size();

        map = Day09.generateMap(input);
        lowPoints = new ArrayList<>();
    }

    @Test
    public void testPartOne() {
        assertEquals(15, Day09.sumOfLowPointRiskLevels(map, length, depth, lowPoints));
    }

    @Test
    public void testPartTwo() {
        assertEquals(1134, Day09.findBasins(map, length, depth, lowPoints));
    }
}
