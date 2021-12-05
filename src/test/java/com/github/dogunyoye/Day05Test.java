package com.github.dogunyoye;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

import com.github.dogunyoye.Day05.LineSegment;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class Day05Test {

    private static Supplier<Stream<String>> streamSupplier;

    private List<LineSegment> segments;

    @BeforeClass
    public static void setUp() throws IOException {
        final List<String> input = Files.readAllLines(Path.of("src/test/resources/Day05TestInput.txt"));
        streamSupplier = () -> input.stream();
    }

    @Before
    public void before() {
        segments = Day05.generateLineSegments(streamSupplier.get());        
    }
    
    @Test
    public void testPartOne() {
        assertEquals(5, Day05.findIntersections(segments, false));
    }

    @Test
    public void testPartTwo() {
        assertEquals(12, Day05.findIntersections(segments, true));
    }
}
