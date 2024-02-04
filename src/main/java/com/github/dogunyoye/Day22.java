package com.github.dogunyoye;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.javatuples.Pair;

public class Day22 {

    private static class Cuboid {
        private final int xMin;
        private final int xMax;
        private final int yMin;
        private final int yMax;
        private final int zMin;
        private final int zMax;
        private final String state;

        private Cuboid(int xMin, int xMax, int yMin, int yMax, int zMin, int zMax, String state) {
            this.xMin = xMin;
            this.xMax = xMax;
            this.yMin = yMin;
            this.yMax = yMax;
            this.zMin = zMin;
            this.zMax = zMax;
            this.state = state;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + xMin;
            result = prime * result + xMax;
            result = prime * result + yMin;
            result = prime * result + yMax;
            result = prime * result + zMin;
            result = prime * result + zMax;
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
            Cuboid other = (Cuboid) obj;
            if (xMin != other.xMin)
                return false;
            if (xMax != other.xMax)
                return false;
            if (yMin != other.yMin)
                return false;
            if (yMax != other.yMax)
                return false;
            if (zMin != other.zMin)
                return false;
            if (zMax != other.zMax)
                return false;
            return true;
        }
    }

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

    static Cuboid createCuboid(String line) {
        final String[] split = line.split(" ");
        final String state = split[0];

        final String[] coordsSplit = split[1].split(",");

        final String[] xSplit = coordsSplit[0].replaceAll("x=", "").split("\\.\\.");
        final int xLower = Integer.parseInt(xSplit[0]);
        final int xUpper = Integer.parseInt(xSplit[1]);

        final String[] ySplit = coordsSplit[1].replaceAll("y=", "").split("\\.\\.");
        final int yLower = Integer.parseInt(ySplit[0]);
        final int yUpper = Integer.parseInt(ySplit[1]);

        final String[] zSplit = coordsSplit[2].replaceAll("z=", "").split("\\.\\.");
        final int zLower = Integer.parseInt(zSplit[0]);
        final int zUpper = Integer.parseInt(zSplit[1]);

        return new Cuboid(xLower, xUpper, yLower, yUpper, zLower, zUpper, state);
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

    private static long parseCuboidsPart2(List<String> input) {
        final Map<Cuboid, Integer> count = new HashMap<>();
        final List<Cuboid> cuboids =
            input.stream().map(Day22::createCuboid).toList();

        for (int i = 0; i < cuboids.size(); i++) {
            final Cuboid c0 = cuboids.get(i);
            final int sign = "on".equals(c0.state) ? 1 : -1;
            final Map<Cuboid, Integer> temp = new HashMap<>();

            for (final Entry<Cuboid, Integer> e : count.entrySet()) {
                final Cuboid c1 = e.getKey();

                final int xMin = Math.max(c0.xMin, c1.xMin);
                final int xMax = Math.min(c0.xMax, c1.xMax);
                final int yMin = Math.max(c0.yMin, c1.yMin);
                final int yMax = Math.min(c0.yMax, c1.yMax);
                final int zMin = Math.max(c0.zMin, c1.zMin);
                final int zMax = Math.min(c0.zMax, c1.zMax);
                final Cuboid intersection =
                    new Cuboid(xMin, xMax, yMin, yMax, zMin, zMax, null);

                if ((xMin <= xMax) && (yMin <= yMax) && (zMin <= zMax)) {
                    if (!temp.containsKey(intersection)) {
                        temp.put(intersection, -e.getValue());
                    } else {
                        temp.put(intersection, temp.get(intersection) - e.getValue());
                    }
                }
            }

            if (sign == 1) {
                // an 'on' cuboid
                count.put(c0, sign);
            }

            for (final Entry<Cuboid, Integer> e : temp.entrySet()) {
                if (!count.containsKey(e.getKey())) {
                    count.put(e.getKey(), e.getValue());
                } else {
                    count.put(e.getKey(), count.get(e.getKey()) + e.getValue());
                }
            }
        }

        return count.entrySet()
            .stream()
            .mapToLong((e) -> {
                final Cuboid cuboid = e.getKey();
                return ((long)(cuboid.xMax - cuboid.xMin + 1)) * ((long)(cuboid.yMax - cuboid.yMin + 1)) * ((long)(cuboid.zMax - cuboid.zMin + 1)) * e.getValue();
            })
            .sum();
    }

    public static long findOnCubesConstrained(List<String> input) {
        return parseCuboids(input).size();
    }

    public static long findOnCubesUnconstrained(List<String> input) {
        return parseCuboidsPart2(input);
    }
    
    public static void main( String[] args ) throws IOException {
        final List<String> input = Files.readAllLines(Path.of("src/main/resources/Day22.txt"));
        System.out.println("Part 1: " + findOnCubesConstrained(input));
        System.out.println("Part 2: " + findOnCubesUnconstrained(input));
    }
}
