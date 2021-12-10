package com.github.dogunyoye;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

public class Day10Test {

    private static List<String> input;
    private static List<String> incompleteLines;

    @BeforeClass
    public static void setUp() throws IOException {
        input = Files.readAllLines(Path.of("src/test/resources/Day10TestInput.txt"));
        incompleteLines = new ArrayList<>();
    }

    @Test
    public void testPartOne() {
        assertEquals(26397, Day10.getSyntaxErrorScore(input, incompleteLines));
    }

    @Test
    public void testPartTwo() {
        assertEquals(288957L, Day10.getScoreForIncompleteLine(incompleteLines));
    }
}
