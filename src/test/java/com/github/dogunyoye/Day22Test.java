package com.github.dogunyoye;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

public class Day22Test {

    private static List<String> input;

    @BeforeClass
    public static void before() throws IOException {
        input = Files.readAllLines(Path.of("src/test/resources/Day22TestInput.txt"));
    }

    @Test
    public void testPartOne() {
        assertEquals(590784, Day22.findOnCubes(input));
    }

    @Test
    public void testPartTwo() {
        
    }
    
}
