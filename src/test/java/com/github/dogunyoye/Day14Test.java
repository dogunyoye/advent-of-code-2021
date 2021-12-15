package com.github.dogunyoye;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import com.github.dogunyoye.Day14.Polymer;

import org.junit.BeforeClass;
import org.junit.Test;

public class Day14Test {

    private static Polymer p;

    @BeforeClass
    public static void setUp() throws IOException {
        final List<String> input = Files.readAllLines(Path.of("src/test/resources/Day14TestInput.txt"));
        final String template = input.stream().filter(str -> !str.isEmpty() && !str.contains("->")).findFirst().get();
        final Map<String, String> insertionRules = Day14.generateInsertionRules(input.stream().filter(str -> str.contains("->")).toList());

        p = new Polymer(template, insertionRules);
    }

    @Test
    public void testPartOne() {
        assertEquals(1588, Day14.findPartOne(p));
    }

    @Test
    public void testPartTwo() {
        assertEquals(2188189693529L, Day14.findPartTwo(p));
    }
}
