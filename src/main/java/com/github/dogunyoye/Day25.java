package com.github.dogunyoye;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day25 {

    private record Position(int i, int j) { };

    private static char[][] buildMap(List<String> data) {
        final int mapLength = data.get(0).length();
        final int mapDepth = data.size();
        final char[][] map = new char[mapDepth][mapLength];

        for (int i = 0; i < mapDepth; i++) {
            for (int j = 0; j < mapLength; j++) {
                map[i][j] = data.get(i).charAt(j);
            }
        }

        return map;
    }

    private static int moveEastFacingSeaCucumbers(char[][] map) {
        final int mapLength = map[0].length;
        final Set<Position> moved = new HashSet<>();

        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < mapLength; j++) {
                final char seaCucumber = map[i][j];
                if (seaCucumber == '>' && map[i][(j+1)%mapLength] == '.') {
                    moved.add(new Position(i, (j+1)%mapLength));
                }
            }
        }

        moved.forEach((p) -> {
            int prev = p.j - 1;
            if (prev < 0) {
                prev = mapLength - 1;
            }
            map[p.i][prev] = '.';
            map[p.i][p.j] = '>';
        });

        return moved.size();
    }

    private static int moveSouthFacingSeaCucumbers(char[][] map) {
        final int mapDepth = map.length;
        final Set<Position> moved = new HashSet<>();

        for (int i = 0; i < mapDepth; i++) {
            for (int j = 0; j < map[0].length; j++) {
                final char seaCucumber = map[i][j];
                if (seaCucumber == 'v' && map[(i+1)%mapDepth][j] == '.') {
                    moved.add(new Position((i+1)%mapDepth, j));
                }
            }
        }

        moved.forEach((p) -> {
            int prev = p.i - 1;
            if (prev < 0) {
                prev = mapDepth - 1;
            }
            map[prev][p.j] = '.';
            map[p.i][p.j] = 'v';
        });

        return moved.size();
    }

    public static int findNumberOfStepsUntilStationarySeaCucumbers(List<String> data) {
        final char[][] map = buildMap(data);
        int steps = 0;

        while (true) {
            ++steps;
            final int movedEast = moveEastFacingSeaCucumbers(map);
            final int movedSouth = moveSouthFacingSeaCucumbers(map);

            if (movedEast == 0 && movedSouth == 0) {
                return steps;
            }
        }
    }

    public static void main(String[] args) throws IOException {
        final List<String> input = Files.readAllLines(Path.of("src/main/resources/Day25.txt"));
        System.out.println("Part 1: " + findNumberOfStepsUntilStationarySeaCucumbers(input));
    }
}
