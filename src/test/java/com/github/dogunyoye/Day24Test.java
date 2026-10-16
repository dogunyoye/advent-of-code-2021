package com.github.dogunyoye;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;

public class Day24Test {

    @Test
    public void testClassLoads() {
        assertNotNull(Day24.class);
    }

    @Test
    public void testInvalidInputSizeThrows() {
        try {
            Day24.findModelNumbersAcceptedByMONAD(Collections.singletonList("inp w"));
            fail("Expected IllegalArgumentException for input size not a multiple of 18");
        } catch (IllegalArgumentException expected) {
            // expected: no Day24TestInput.txt exists, so validate the guard directly
        }
    }

    @Test
    public void testEmptyInputThrows() {
        try {
            Day24.findModelNumbersAcceptedByMONAD(Collections.emptyList());
            fail("Expected IllegalArgumentException for empty input");
        } catch (IllegalArgumentException expected) {
            // expected
        }
    }

    @Test
    public void testRaggedSubprogramSizeThrows() {
        try {
            Day24.findModelNumbersAcceptedByMONAD(Arrays.asList("inp w", "add x 1"));
            fail("Expected IllegalArgumentException for input size not a multiple of 18");
        } catch (IllegalArgumentException expected) {
            // expected
        }
    }
}
