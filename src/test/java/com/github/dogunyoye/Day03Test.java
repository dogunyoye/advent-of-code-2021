package com.github.dogunyoye;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Supplier;

import org.junit.BeforeClass;
import org.junit.Test;

public class Day03Test {

        private static Supplier<List<String>> reportSupplier;
    
        @BeforeClass
        public static void setUp() throws IOException {
                final List<String> report = Files.readAllLines(Path.of("src/test/resources/Day03TestInput.txt"));
                reportSupplier = () -> report.stream().toList();
        }

        @Test
        public void testPartOne() {
                assertEquals(198, Day03.findPowerConsumption(reportSupplier.get()));
        }

        @Test
        public void testPartTwo() {
                assertEquals(230, Day03.findLifeSupportRating(reportSupplier.get(), reportSupplier.get()));
        }
}
