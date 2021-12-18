package com.github.dogunyoye;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import com.github.dogunyoye.Day16.Packet;

import org.junit.BeforeClass;
import org.junit.Test;

public class Day16Test {

    private static List<Packet> packets;
    private static List<Packet> packets2;

    @BeforeClass
    public static void setUp() throws IOException {
        final List<String> input = Files.readAllLines(Path.of("src/test/resources/Day16TestInput.txt"));
        final String hexString = input.get(0);
        final String hexString2 = input.get(1);

        packets = Day16.parsePackets(hexString);
        packets2 = Day16.parsePackets(hexString2);
    }

    @Test
    public void testPartOne() {
        assertEquals(31, Day16.findSumOfVersionIds(packets));
    }

    @Test
    public void testPartTwo() {
        assertEquals(1, Day16.findResult(packets2));
    }
}
