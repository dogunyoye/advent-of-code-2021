package com.github.dogunyoye;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import com.github.dogunyoye.Day21.Player;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class Day21Test {

    private static List<String> input;
    private static Player[] players;

    @BeforeClass
    public static void setUp() throws IOException {
        input = Files.readAllLines(Path.of("src/test/resources/Day21TestInput.txt"));
    }

    @Before
    public void before() {
        players = Day21.generatePlayers(input);
    }

    @Test
    public void testPartOne() {
        assertEquals(739785, Day21.playDiracDice(players));
    }

    @Test
    public void testPartTwo() {
        assertEquals(444356092776315L, Day21.findWinsInMostUniverses(players));
    }
}
