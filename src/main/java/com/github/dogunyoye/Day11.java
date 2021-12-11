package com.github.dogunyoye;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.github.dogunyoye.Day05.Coordinate;

public class Day11 {

    public enum Direction {
        NORTH,
        NORTH_EAST,
        EAST,
        SOUTH_EAST,
        SOUTH,
        SOUTH_WEST, 
        WEST,
        NORTH_WEST
    }

    private static List<Coordinate> getNeighbours(int[][] map, int length, int depth, int i, int j) {
        final List<Coordinate> neighbours = new ArrayList<>();
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
                
                case NORTH_EAST:
                    x = i - 1;
                    y = j + 1;

                    if (x < 0 || y >= length) {
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
                
                case SOUTH_EAST:
                    x = i + 1;
                    y = j + 1;

                    if (x >= depth || y >= length) {
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
                
                case SOUTH_WEST:
                    x = i + 1;
                    y = j - 1;

                    if (x >= depth || y < 0) {
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

                case NORTH_WEST:
                    x = i - 1;
                    y = j - 1;

                    if (x < 0 || y < 0) {
                        continue;
                    }
                    break;
            }

            neighbours.add(new Coordinate(x, y));
        }

        return neighbours;
    }

    private static void octopusFlash(int[][] map, Set<Coordinate> flashed, int length, int depth, Coordinate c) {
        if (flashed.contains(c)) {
            return;
        }

        flashed.add(c);
        final List<Coordinate> neighbours = getNeighbours(map, length, depth, c.x, c.y);

        for (Coordinate n : neighbours) {
            map[n.x][n.y]++;
            if (map[n.x][n.y] == 10) {
                octopusFlash(map, flashed, length, depth, n);
            }
        }
    }

    public static int simulateDumboOctopusFlashes(int[][] map, int steps, int length, int depth) {
        int sum = 0;
        final Set<Coordinate> flashed = new HashSet<>();

        while(steps != 0) {
            flashed.clear();

            for (int i = 0; i < depth; i++) {
                for (int j = 0; j < length; j++) {
                    map[i][j]++;
                }
            }

            for (int i = 0; i < depth; i++) {
                for (int j = 0; j < length; j++) {
                    if (map[i][j] > 9) {
                        octopusFlash(map, flashed, length, depth, new Coordinate(i, j));
                    }
                }
            }

            for (Coordinate fo : flashed) {
                map[fo.x][fo.y] = 0;
                sum++;
            }

            steps--;
        }

        return sum;
    }

    public static int findSynchronisedFlash(int[][] map, int length, int depth) {
        int steps = 0;
        final Set<Coordinate> flashed = new HashSet<>();

        while(true) {
            flashed.clear();

            for (int i = 0; i < depth; i++) {
                for (int j = 0; j < length; j++) {
                    map[i][j]++;
                }
            }

            for (int i = 0; i < depth; i++) {
                for (int j = 0; j < length; j++) {
                    if (map[i][j] > 9) {
                        octopusFlash(map, flashed, length, depth, new Coordinate(i, j));
                    }
                }
            }

            steps++;

            if (flashed.size() == length * depth) {
                return steps;
            }

            flashed.forEach((fo) -> {
                map[fo.x][fo.y] = 0;
            });
        }
    }

    public static void main ( String[] args) throws IOException {
        final List<String> input = Files.readAllLines(Path.of("src/main/resources/Day11.txt"));
        final int length = input.get(0).length();
        final int depth = input.size();

        System.out.println("Part 1: " + simulateDumboOctopusFlashes(Day09.generateMap(input), 100, length, depth));
        System.out.println("Part 2: " + findSynchronisedFlash(Day09.generateMap(input), length, depth));
    }
}
