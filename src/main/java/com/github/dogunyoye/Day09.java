package com.github.dogunyoye;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import com.github.dogunyoye.Day05.Coordinate;

public class Day09 {

    public enum Direction {
        NORTH,
        EAST,
        SOUTH, 
        WEST;
    }

    private static boolean isSmallest(int[][] map, int length, int depth, int i, int j) {
        final int current = map[i][j];

        for (Direction d : Direction.values()) {
            int x = 0;
            int y = 0;

            switch(d) {
                case NORTH:
                    x = i - 1;
                    y = j;

                    if (x < 0) {
                        continue;
                    }
                    break;

                case EAST:
                    x = i;
                    y = j + 1;

                    if (y >= length) {
                        continue;
                    }
                    break;

                case SOUTH:
                    x = i + 1;
                    y = j;

                    if (x >= depth) {
                        continue;
                    }
                    break;

                case WEST:
                    x = i;
                    y = j - 1;

                    if (y < 0) {
                        continue;
                    }
                    break;
            }

            if (current == map[x][y]) {
                return false;
            }

            final int min = Math.min(current, map[x][y]);
            if (min != current) {
                return false;
            }
        }

        return true;
    }

    private static void recurseBasin(int[][] map, Set<Coordinate> visited, int length, int depth, int i, int j, AtomicInteger counter) {

        for (Direction d : Direction.values()) {
            int x = 0;
            int y = 0;

            switch(d) {

                case NORTH:
                    x = i - 1;
                    y = j;

                    if (x < 0) {
                        continue;
                    }
                    break;

                case EAST:
                    x = i;
                    y = j + 1;

                    if (y >= length) {
                        continue;
                    }
                    break;

                case SOUTH:
                    x = i + 1;
                    y = j;

                    if (x >= depth) {
                        continue;
                    }
                    break;

                case WEST:
                    x = i;
                    y = j - 1;

                    if (y < 0) {
                        continue;
                    }
                    break;
            }

            final int current = map[x][y];
            if (current != 9 && !visited.contains(new Coordinate(x, y))) {
                counter.incrementAndGet();
                visited.add(new Coordinate(x, y));
                recurseBasin(map, visited, length, depth, x, y, counter);
            }
        }
    }

    public static int[][] generateMap(List<String> input) {
        int currentHeight = 0;
        final int length = input.get(0).length();

        final int[][] map = new int[input.size()][length];
        for (String line : input) {
            final String[] split = line.split("");
            for (int i = 0; i < split.length; i++) {
                map[currentHeight][i] = Integer.parseInt(split[i]);
            }

            currentHeight++;
        }

        return map;
    }

    public static int sumOfLowPointRiskLevels(int[][] map, int length, int depth, List<Coordinate> lowPoints) {

        int sum = 0;
        for (int i = 0; i < depth; i++) {
            for (int j = 0; j < length; j++) {
                if (isSmallest(map, length, depth, i, j)) {
                    sum += map[i][j] + 1;
                    lowPoints.add(new Coordinate(i, j));
                }
            }
        }

        return sum;
    }

    public static int findBasins(int[][] map, int length, int depth, List<Coordinate> lowPoints) {

        final List<Integer> basinSizes = new ArrayList<>();
        final Set<Coordinate> visited = new HashSet<>(lowPoints);

        for (Coordinate c : lowPoints) {
            final AtomicInteger counter = new AtomicInteger();
            counter.incrementAndGet();
            recurseBasin(map, visited, length, depth, c.x, c.y, counter);
            basinSizes.add(counter.get());
        }

        basinSizes.sort(Integer::compareTo);
        return basinSizes.get(basinSizes.size()-1) * basinSizes.get(basinSizes.size()-2) * basinSizes.get(basinSizes.size()-3);
    }

    public static void main( String[] args) throws IOException {
        final List<String> input = Files.readAllLines(Path.of("src/main/resources/Day09.txt"));
        final int length = input.get(0).length();
        final int depth = input.size();

        final int[][] map = generateMap(input);
        final List<Coordinate> lowPoints = new ArrayList<>();

        System.out.println("Part 1: " + sumOfLowPointRiskLevels(map, length, depth, lowPoints));
        System.out.println("Part 2: " + findBasins(map, length, depth, lowPoints));
    }
}
