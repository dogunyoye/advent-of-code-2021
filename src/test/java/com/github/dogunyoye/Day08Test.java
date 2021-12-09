package com.github.dogunyoye;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import com.github.dogunyoye.Day08.SignalEntry;

import org.junit.BeforeClass;
import org.junit.Test;

public class Day08Test {

    private static List<SignalEntry> signalEntries;

    @BeforeClass
    public static void setUp() throws IOException {
        final List<String> input = Files.readAllLines(Path.of("src/test/resources/Day08TestInput.txt"));
        signalEntries = Day08.generateSignalEntries(input);
    }

    @Test
    public void testPartOne() {
        assertEquals(26, Day08.findNumberOfUniqueSegments(signalEntries));
    }

    @Test
    public void testPartTwo() {
        assertEquals(61229, Day08.findOutputValuesSum(signalEntries));
    }
    
}
