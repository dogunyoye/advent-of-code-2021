package com.github.dogunyoye;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Day01 {

    public static int calculateIncreases(int[] depths) {
        int increases = 0;
        for (int i = 0; i < depths.length - 1; i++) {
            if (depths[i] < depths[i+1]) {
                increases++;
            }
        }

        return increases;
    }

    public static int calculateIncreasesSlidingWindow(int[] depths) {
        int increases = 0;
        for (int i = 0; i < depths.length - 2; i++) {
            depths[i] = depths[i] + depths[i+1] + depths[i+2];
            if (i > 0 && depths[i-1] < depths[i]) {
                increases++;
            }
        }

        return increases;
    }

    public static void main( String[] args ) throws IOException {
        final int[] depths = Files.readAllLines(Path.of("src/main/resources/Day01.txt"))
                .stream()
                .mapToInt(num -> Integer.parseInt(num))
                .toArray();

        System.out.println("Part 1: " + calculateIncreases(depths));
        System.out.println("Part 2: " + calculateIncreasesSlidingWindow(depths));
    }
}
