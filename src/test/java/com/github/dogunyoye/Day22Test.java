package com.github.dogunyoye;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

public class Day22Test {

    private static List<String> input1;
    private static List<String> input2;

    @BeforeClass
    public static void before() throws IOException {
        input1 = Files.readAllLines(Path.of("src/test/resources/Day22TestInputPart1.txt"));
        input2 = Files.readAllLines(Path.of("src/test/resources/Day22TestInputPart2.txt"));
    }

    @Test
    public void testPartOne() {
        assertEquals(590784, Day22.findOnCubesConstrained(input1));
    }

    @Test
    public void testPartTwo() {
        assertEquals(2758514936282235L, Day22.findOnCubesUnconstrained(input2));
    }
    
}
