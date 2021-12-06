package com.github.dogunyoye;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public class Day06 {

    public static long simulateLanternFish(int days, int[] initial) {
        final long[] fishes = new long[9];

        for (int n : initial) {
            fishes[n]++;
        }

        while (days != 0) {
            long newFishes = fishes[0];

            for (int i = 1; i < fishes.length; i++) {
                fishes[i-1] = fishes[i];
                fishes[i] = 0;
            }

            fishes[6] += newFishes;
            fishes[8] += newFishes;

            days--;
        }

        long sum = 0;
        for (long n : fishes) {
            sum += n;
        }

        return sum;
    }
    

    public static void main( String[] args ) throws IOException {
        final List<String> input = Files.readAllLines(Path.of("src/main/resources/Day06.txt"));
        final int[] initialFish = Stream.of(input.get(0).split(",")).mapToInt(Integer::parseInt).toArray();

        System.out.println("Part 1: " + simulateLanternFish(80, initialFish));
        System.out.println("Part 2: " + simulateLanternFish(256, initialFish));
    }
}
