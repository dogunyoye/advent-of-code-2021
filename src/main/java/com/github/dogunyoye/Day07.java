package com.github.dogunyoye;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day07 {

    private static int calculate(int[] positions, BiFunction<Integer, Integer, Integer> costFunction) {
        final int min = Arrays.stream(positions).boxed().min((i, j) -> i.compareTo(j)).get();
        final int max = Arrays.stream(positions).boxed().max((i, j) -> i.compareTo(j)).get();

        final Map<Integer, Integer> m = new HashMap<Integer, Integer>();
        IntStream.range(min, max + 1).forEach((pos) -> m.put(pos, 0));

        final int[] keys = m.keySet().stream().mapToInt(x -> x).toArray();
        for (int i = 0; i < keys.length; i++) {
            int sum = 0;
            final int crabPos = keys[i];

            for (int j = 0; j < positions.length; j++) {
                sum += costFunction.apply(crabPos, positions[j]);
            }

            m.put(crabPos, sum);
        }

        return m.values().stream().min((i, j) -> i.compareTo(j)).get();
    }

    public static int calculateFuelCost(int[] positions) {
        return calculate(positions,
            (crabPos, otherCrabPos) -> {
                return Math.abs(crabPos - otherCrabPos);
            });
    }

    public static int calculateEnchancedFuelCost(int[] positions) {
        return calculate(positions,
            (crabPos, otherCrabPos) -> {
                final int n = Math.abs(crabPos - otherCrabPos);
                return (n * (n + 1))/2;
            });
    }

    public static void main ( String[] args ) throws IOException {
        final List<String> input = Files.readAllLines(Path.of("src/main/resources/Day07.txt"));
        final int[] positions = Stream.of(input.get(0).split(",")).mapToInt(Integer::parseInt).toArray();

        System.out.println("Part 1: " + calculateFuelCost(positions));
        System.out.println("Part 2: " + calculateEnchancedFuelCost(positions));
    }
}
