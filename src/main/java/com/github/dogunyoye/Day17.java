package com.github.dogunyoye;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.dogunyoye.Day05.Coordinate;

public class Day17 {

    static class Probe {
        public int x;
        public int y;

        public Probe(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    static class Velocity {
        public int xVel;
        public int yVel;

        public Velocity(int xVel, int yVel) {
            this.xVel = xVel;
            this.yVel = yVel;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + xVel;
            result = prime * result + yVel;
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }

            if (obj == null) {
                return false;
            }

            if (getClass() != obj.getClass()) {
                return false;
            }

            Velocity other = (Velocity) obj;
            if (xVel != other.xVel) {
                return false;
            }

            if (yVel != other.yVel) {
                return false;
            }

            return true;
        }
    }

    private static boolean withinTargetArea(Probe p, int[] bounds) {
        if (p.x >= bounds[0] && p.x <= bounds[1] && p.y >= bounds[2] && p.y <= bounds[3]) {
            return true;
        }

        return false;
    }

    private static void applyVelocityToProbe(int[] bounds, Map<Velocity, List<Coordinate>> landedVelocities, Probe p, Velocity v) {
        final List<Coordinate> steps = new ArrayList<>();
        final int xBound = bounds[1];
        final int yBound = bounds[2];

        final Velocity originalV = new Velocity(v.xVel, v.yVel);

        while (true) {
            // missed the target area
            // we can stop simulating
            if (p.x > xBound || p.y < yBound) {
                break;
            }

            // reached the target area
            // we can stop simulating
            if(withinTargetArea(p, bounds)) {
                landedVelocities.put(originalV, steps);
                break;
            }

            p.x += v.xVel;
            p.y += v.yVel;

            steps.add(new Coordinate(p.x, p.y));

            // apply velocity changes
            if (v.xVel > 0) {
                v.xVel--;
            } else if (v.xVel < 0) {
                v.xVel++;
            }

            v.yVel--;
        }
    }

    private static Map<Velocity, List<Coordinate>> simulateProbeTrajectory(int[] bounds) {

        final Map<Velocity, List<Coordinate>> landedVelocities = new HashMap<>();

        // lovely brute force...
        // smarter solution most likely possible
        for (int x = 1; x <= 1000; x++) {
            for (int y = -1000; y <= 1000; y++) {
                final Probe p = new Probe(0, 0);
                final Velocity v = new Velocity(x, y);
                applyVelocityToProbe(bounds, landedVelocities, p, v);
            }
        }
        return landedVelocities;
    }

    public static int[] getBounds(List<String> input) {
        final List<Integer> bounds = new ArrayList<>();
        final String[] split = input.get(0).replaceAll("target area: ", "").split(", ");
        for (String s : split) {
            final String[] boundSplit = s.replaceAll("x=", "").replaceAll("y=", "").split("\\.\\.");
            for (String v : boundSplit) {
                bounds.add(Integer.parseInt(v));
            }
        }
        return bounds.stream().mapToInt(i->i).toArray();
    }

    public static int findLargestHeight(int[] bounds) {
        final Map<Velocity, List<Coordinate>> landed = simulateProbeTrajectory(bounds);
        int maxHeight = 0;

        for(List<Coordinate> coords : landed.values()) {
            for (Coordinate c : coords) {
                maxHeight = Math.max(maxHeight, c.y);
            }
        }

        return maxHeight;
    }

    public static int findDistinctLandedVelocities(int[] bounds) {
        final Map<Velocity, List<Coordinate>> landed = simulateProbeTrajectory(bounds);
        return landed.keySet().size();
    }
    
    public static void main( String[] args ) throws IOException {
        final List<String> input = Files.readAllLines(Path.of("src/main/resources/Day17.txt"));
        final int[] bounds = getBounds(input);

        System.out.println("Part 1: " + findLargestHeight(bounds));
        System.out.println("Part 2: " + findDistinctLandedVelocities(bounds));
    }
}
