package com.github.dogunyoye;

import java.io.IOException;

public class Day01 {

    public static int calculateIncreases(int[] depths) {
        if (depths == null || depths.length == 0) {
            return 0;
        }
        int increases = 0;
        for (int i = 0; i < depths.length - 1; i++) {
            if (depths[i] < depths[i+1]) {
                increases++;
            }
        }

        return increases;
    }

    public static int calculateIncreasesSlidingWindow(int[] depths) {
        if (depths == null || depths.length == 0) {
            return 0;
        }
        int increases = 0;
        for (int i = 0; i < depths.length - 3; i++) {
            final int current = depths[i] + depths[i+1] + depths[i+2];
            final int next = depths[i+1] + depths[i+2] + depths[i+3];
            if (current < next) {
                increases++;
            }
        }

        return increases;
    }

    public static void main( String[] args ) throws IOException {
        final int[] depths = InputReader.readLines("Day01.txt")
                .stream()
                .mapToInt(num -> Integer.parseInt(num))
                .toArray();

        System.out.println("Part 1: " + calculateIncreases(depths));
        System.out.println("Part 2: " + calculateIncreasesSlidingWindow(depths));
    }
}
