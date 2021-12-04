package com.github.dogunyoye;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

import com.github.dogunyoye.Day04.BingoCard;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class Day04Test {

    private static int[] numbers;
    private static Supplier<Stream<String>> streamSupplier;

    private List<BingoCard> bingoCards;
    
    @BeforeClass
    public static void setUp() throws IOException {
        final List<String> input = Files.readAllLines(Path.of("src/test/resources/Day04TestInput.txt"));
        
        streamSupplier = () -> input.stream();
        numbers = Stream.of(input.iterator().next().split(",")).mapToInt(Integer::parseInt).toArray();
    }

    @Before
    public void before() {
        final Iterator<String> iter = streamSupplier.get().iterator();
        iter.next();
    
        bingoCards = Day04.generateBingoCards(iter);
    }

    @Test
    public void testPartOne() {
        assertEquals(4512, Day04.playBingo(numbers, bingoCards));
    }

    @Test
    public void testPartTwo() {
        assertEquals(1924, Day04.lastWinner(numbers, bingoCards));
    }
}
