package com.github.dogunyoye;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import com.github.dogunyoye.Day05.Coordinate;

import org.junit.BeforeClass;
import org.junit.Test;

public class Day20Test {

    private static List<String> input;
    private static char[] imageAlgorithm;
    private static int idx;

    @BeforeClass
    public static void setUp() throws IOException {
        input = Files.readAllLines(Path.of("src/test/resources/Day20TestInput.txt"));
        idx = input.size() - 1;
        imageAlgorithm = Day20.generateImageAlgorithm(input);
    }

    @Test
    public void testPartOne() {
        final Map<Coordinate, Character> initialImage = Day20.generateInitialInputImage(input, 5);
        assertEquals(35, Day20.findNumberOfLitPixels(imageAlgorithm, initialImage, 5, idx, 2));
    }
}
