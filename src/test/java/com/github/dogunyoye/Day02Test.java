package com.github.dogunyoye;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import com.github.dogunyoye.Day02.Submarine;

import org.junit.Before;
import org.junit.Test;

public class Day02Test {

    private Stream<String> commands;
    private Submarine sub;

    @Before
    public void before() throws IOException {
        commands = Files.readAllLines(Path.of("src/test/resources/Day02TestInput.txt")).stream();
        sub = new Submarine();
    }

    @Test
    public void testPartOne() throws IOException {
        assertEquals(150, Day02.moveSub(commands, sub));
    }

    @Test
    public void testPartTwo() throws IOException {
        assertEquals(900, Day02.moveSubWithAim(commands, sub));
    }
}
