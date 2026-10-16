package com.github.dogunyoye;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class Day06 {

    public static long simulateLanternFish(int days, int[] initial) {
        if (days < 0) {
            throw new IllegalArgumentException("Days must be >= 0, got " + days);
        }
        if (initial == null || initial.length == 0) {
            throw new IllegalArgumentException("Initial fish population must not be null or empty");
        }
        final long[] fishes = new long[9];

        for (int n : initial) {
            if (n < 0 || n > 8) {
                throw new IllegalArgumentException("Fish timer must be in range 0..8, got " + n);
            }
            fishes[n]++;
        }

        while (days > 0) {
            final long newFishes = fishes[0];

            for (int i = 1; i < fishes.length; i++) {
                fishes[i-1] = fishes[i];
            }

            fishes[6] += newFishes;
            fishes[8] = newFishes;

            days--;
        }

        return Arrays.stream(fishes).sum();
    }
    

    public static void main( String[] args ) throws IOException {
        final List<String> input = InputReader.readLines("Day06.txt");
        final int[] initialFish = Stream.of(input.get(0).split(",")).mapToInt(Integer::parseInt).toArray();

        System.out.println("Part 1: " + simulateLanternFish(80, initialFish));
        System.out.println("Part 2: " + simulateLanternFish(256, initialFish));
    }
}
