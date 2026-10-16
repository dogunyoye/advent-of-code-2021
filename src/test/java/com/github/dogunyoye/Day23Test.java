package com.github.dogunyoye;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;

public class Day23Test {
    
    @BeforeClass
    public static void before() throws IOException {
        // no code
        // used https://aochelper2021.blob.core.windows.net/day23/index.html
    }

    @Test
    public void testMainDoesNotThrow() {
        // Day23 was solved with an external tool; main is a no-op kept for compat
        Day23.main(new String[0]);
    }

    @Test
    public void testSolveIsExplicitStub() {
        assertNotNull(Day23.class);
        try {
            Day23.solve();
            fail("Expected UnsupportedOperationException from explicit Day23 stub");
        } catch (UnsupportedOperationException expected) {
            // documents that Day23 has no in-repo solver (external solver used)
        }
    }
}
