package com.github.dogunyoye;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

public class Day12Test {

    private static Map<String, List<String>> caveMap;

    @BeforeClass
    public static void setUp() throws IOException {
        final List<String> input = Files.readAllLines(Path.of("src/test/resources/Day12TestInput.txt"));
        caveMap = Day12.generateMap(input);
    }

    @Test
    public void testPartOne() {
        assertEquals(10, Day12.countDistinctPaths(caveMap));
    }

    @Test
    public void testPartTwo() {
        assertEquals(36, Day12.countDistinctPathsWithNewRules(caveMap));
    }
}
