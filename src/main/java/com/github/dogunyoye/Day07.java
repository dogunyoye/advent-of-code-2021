package com.github.dogunyoye;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day07 {

    private static int calculate(int[] positions, BiFunction<Integer, Integer, Long> costFunction) {
        if (positions == null || positions.length == 0) {
            throw new IllegalArgumentException("Positions must not be null or empty");
        }
        final int min = Arrays.stream(positions).min().orElseThrow(() -> new IllegalArgumentException("Positions must not be empty"));
        final int max = Arrays.stream(positions).max().orElseThrow(() -> new IllegalArgumentException("Positions must not be empty"));

        final Map<Integer, Long> m = new HashMap<Integer, Long>();
        IntStream.range(min, max + 1).forEach((pos) -> m.put(pos, 0L));

        final int[] keys = m.keySet().stream().mapToInt(x -> x).toArray();
        for (int i = 0; i < keys.length; i++) {
            long sum = 0L;
            final int crabPos = keys[i];

            for (int j = 0; j < positions.length; j++) {
                sum += costFunction.apply(crabPos, positions[j]);
            }

            m.put(crabPos, sum);
        }

        return Math.toIntExact(m.values().stream().min(Long::compareTo).orElseThrow(() -> new IllegalStateException("No fuel costs computed")));
    }

    public static int calculateFuelCost(int[] positions) {
        return calculate(positions,
            (crabPos, otherCrabPos) -> {
                return (long) Math.abs(crabPos - otherCrabPos);
            });
    }

    public static int calculateEnhancedFuelCost(int[] positions) {
        return calculate(positions,
            (crabPos, otherCrabPos) -> {
                final long n = Math.abs((long) crabPos - (long) otherCrabPos);
                return (n * (n + 1)) / 2;
            });
    }

    public static void main ( String[] args ) throws IOException {
        final List<String> input = InputReader.readLines("Day07.txt");
        final int[] positions = Stream.of(input.get(0).split(",")).mapToInt(Integer::parseInt).toArray();

        System.out.println("Part 1: " + calculateFuelCost(positions));
        System.out.println("Part 2: " + calculateEnhancedFuelCost(positions));
    }
}
