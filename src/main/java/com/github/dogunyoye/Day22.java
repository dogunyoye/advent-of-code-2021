package com.github.dogunyoye;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.javatuples.Pair;

public class Day22 {

    public static class Cube {
        public int x;
        public int y;
        public int z;

        public Cube(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + x;
            result = prime * result + y;
            result = prime * result + z;
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Cube other = (Cube) obj;
            if (x != other.x)
                return false;
            if (y != other.y)
                return false;
            if (z != other.z)
                return false;
            return true;
        }
    }

    private static boolean checkBounds(int lower, int upper) {
        if (lower >= -50 && upper <= 50) {
            return true;
        }

        return false;
    }

    private static Pair<String, Set<Cube>> parse(String line) {
        final String[] split = line.split(" ");
        final String state = split[0];

        final String[] coordsSplit = split[1].split(",");

        final String[] xSplit = coordsSplit[0].replaceAll("x=", "").split("\\.\\.");
        final int xLower = Integer.parseInt(xSplit[0]);
        final int xUpper = Integer.parseInt(xSplit[1]);

        if (!checkBounds(xLower, xUpper)) {
            return null;
        }

        final String[] ySplit = coordsSplit[1].replaceAll("y=", "").split("\\.\\.");
        final int yLower = Integer.parseInt(ySplit[0]);
        final int yUpper = Integer.parseInt(ySplit[1]);

        if (!checkBounds(yLower, yUpper)) {
            return null;
        }

        final String[] zSplit = coordsSplit[2].replaceAll("z=", "").split("\\.\\.");
        final int zLower = Integer.parseInt(zSplit[0]);
        final int zUpper = Integer.parseInt(zSplit[1]);

        if (!checkBounds(zLower, zUpper)) {
            return null;
        }

        final Set<Cube> cubes = new HashSet<>();

        for (int x = xLower; x <= xUpper; x++) {
            for (int y = yLower; y <= yUpper; y++) {
                for (int z = zLower; z <= zUpper; z++) {
                    cubes.add(new Cube(x, y, z));
                }
            }
        }

        return Pair.with(state, cubes);
    }

    private static Set<Cube> parseCuboids(List<String> input) {
        final Set<Cube> cubes = new HashSet<>();

        for (String line : input) {
            final Pair<String, Set<Cube>> c = parse(line);
            if (c == null) {
                continue;
            }

            if (c.getValue0().equals("on")) {
                cubes.addAll(c.getValue1());
            } else {
                cubes.removeAll(c.getValue1());
            }
        }

        return cubes;

    }

    public static long findOnCubes(List<String> input) {
        return parseCuboids(input).size();
    }
    
    public static void main( String[] args ) throws IOException {
        final List<String> input = Files.readAllLines(Path.of("src/main/resources/Day22.txt"));

        System.out.println("Part 1: " + findOnCubes(input));
    }
}
