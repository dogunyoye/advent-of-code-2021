package com.github.dogunyoye;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.github.dogunyoye.Day13.FoldInstruction;

import org.junit.BeforeClass;
import org.junit.Test;

public class Day13Test {

    private static String[][] paper;
    private static List<FoldInstruction> fInstructions;

    private static int xLimit;
    private static int yLimit;

    @BeforeClass
    public static void setUp() throws IOException {
        final List<String> input = Files.readAllLines(Path.of("src/test/resources/Day13TestInput.txt"));
        final List<String> dots = input.stream().filter(str -> str.contains(",")).toList();
        final List<String> instructions = input.stream().filter(str -> str.contains("fold along")).toList();

        final AtomicInteger xMax = new AtomicInteger();
        final AtomicInteger yMax = new AtomicInteger();

        paper = Day13.generatePaper(dots, xMax, yMax);
        fInstructions = Day13.generateInstructions(instructions);

        xLimit = xMax.get();
        yLimit = yMax.get();
    }

    @Test
    public void testPartOne() {
        assertEquals(17, Day13.findVisibleDots(0, 1, paper, fInstructions, yLimit, xLimit));
    }

    @Test
    public void testPartTwo() {
        // part 2 is visual, you should see an 0
        Day13.findVisibleDots(1, fInstructions.size(), paper, fInstructions, yLimit, xLimit);
        Day13.visualisePaper(paper);
    }  
}
