package com.github.dogunyoye;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Day19 {

    private static class Position {
        private final int x;
        private final int y;
        private final int z;

        private Position(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        @Override
        public String toString() {
            return "Position [x=" + x + ", y=" + y + ", z=" + z + "]";
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
            Position other = (Position) obj;
            if (x != other.x)
                return false;
            if (y != other.y)
                return false;
            if (z != other.z)
                return false;
            return true;
        }
    }

    private static class Edge {
        private final Position v0;
        private final Position v1;

        private Edge(Position v0, Position v1) {
            this.v0 = v0;
            this.v1 = v1;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((v0 == null) ? 0 : v0.hashCode());
            result = prime * result + ((v1 == null) ? 0 : v1.hashCode());
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
            Edge other = (Edge) obj;
            if (v0 == null) {
                if (other.v0 != null)
                    return false;
            } else if (!v0.equals(other.v0))
                return false;
            if (v1 == null) {
                if (other.v1 != null)
                    return false;
            } else if (!v1.equals(other.v1))
                return false;
            return true;
        }
    }

    private static class Overlap {
        private final Scanner s0;
        private final Scanner s1;
        private final Set<Double> intersection;

        private Overlap(Scanner s0, Scanner s1, Set<Double> intersection) {
            this.s0 = s0;
            this.s1 = s1;
            this.intersection = intersection;
        }
    }

    private static class Scanner {
        private Position scannerPosition;
        private Function<Position, Position> rotation;
        private List<Position> beacons;
        private Map<Edge, Double> distancesMap;

        private Scanner(List<Position> beacons, Map<Edge, Double> distancesMap) {
            this.beacons = beacons;
            this.distancesMap = distancesMap;
        }
    }

    private static Map<Edge, Double> buildDistancesMap(List<Position> beacons) {
        final Map<Edge, Double> distances = new HashMap<>();
        for (int i = 0; i < beacons.size() - 1; i++) {
            final Position p0 = beacons.get(i);
            for (int j = i + 1; j < beacons.size(); j++) {
                final Position p1 = beacons.get(j);
                final double distance = Math.sqrt(Math.pow(p0.x - p1.x, 2) + Math.pow(p0.y - p1.y, 2) + Math.pow(p0.z - p1.z, 2));
                distances.put(new Edge(new Position(p0.x, p0.y, p0.z), new Position(p1.x, p1.y, p1.z)), distance);
            }
        }

        return distances;
    }

    private static Edge findEdgeForDistance(Map<Edge, Double> distancesMap, double distance) {
        return distancesMap.entrySet().stream().filter((e) -> e.getValue() == distance).findFirst().get().getKey();
    }

    private static Queue<Overlap> findOverlappingBeacons(List<Scanner> scanners) {
        // https://en.wikipedia.org/wiki/Graph_(discrete_mathematics)#Undirected_graph
        // (n * (n - 1))/2
        final int expectedMatches = (12 * 11)/2;
        final Queue<Overlap> overlaps = new ArrayDeque<>();

        for (int i = 0; i < scanners.size() - 1; i++) {
            final Scanner s0 = scanners.get(i);
            for (int j = i + 1; j < scanners.size(); j++) {
                final Scanner s1 = scanners.get(j);
                final Set<Double> intersection = new HashSet<>(s0.distancesMap.values());
                intersection.retainAll(s1.distancesMap.values());

                if (intersection.size() >= expectedMatches) {
                    overlaps.add(new Overlap(s0, s1, intersection));
                }
            }
        }

        return overlaps;
    }

    // https://www.euclideanspace.com/maths/algebra/matrix/transforms/examples/index.htm
    private static List<Function<Position, Position>> rotationFunctions() {
        final List<Function<Position, Position>> functions = new ArrayList<>();
        functions.add((pos) -> new Position(pos.x, pos.y, pos.z));
        functions.add((pos) -> new Position(pos.y, pos.z, pos.x));
        functions.add((pos) -> new Position(pos.z, pos.x, pos.y));
        functions.add((pos) -> new Position(-pos.x, pos.z, pos.y));
        functions.add((pos) -> new Position(pos.z, pos.y, -pos.x));
        functions.add((pos) -> new Position(pos.y, -pos.x, pos.z));
        functions.add((pos) -> new Position(pos.x, pos.z, -pos.y));
        functions.add((pos) -> new Position(pos.z, -pos.y, pos.x));
        functions.add((pos) -> new Position(-pos.y, pos.x, pos.z));
        functions.add((pos) -> new Position(pos.x, -pos.z, pos.y));
        functions.add((pos) -> new Position(-pos.z, pos.y, pos.x));
        functions.add((pos) -> new Position(pos.y, pos.x, -pos.z));
        functions.add((pos) -> new Position(-pos.x, -pos.y, pos.z));
        functions.add((pos) -> new Position(-pos.y, pos.z, -pos.x));
        functions.add((pos) -> new Position(pos.z, -pos.x, -pos.y));
        functions.add((pos) -> new Position(-pos.x, pos.y, -pos.z));
        functions.add((pos) -> new Position(pos.y, -pos.z, -pos.x));
        functions.add((pos) -> new Position(-pos.z, -pos.x, pos.y));
        functions.add((pos) -> new Position(pos.x, -pos.y, -pos.z));
        functions.add((pos) -> new Position(-pos.y, -pos.z, pos.x));
        functions.add((pos) -> new Position(-pos.z, pos.x, -pos.y));
        functions.add((pos) -> new Position(-pos.x, -pos.z, -pos.y));
        functions.add((pos) -> new Position(-pos.z, -pos.y, -pos.x));
        functions.add((pos) -> new Position(-pos.y, -pos.x, -pos.z));

        return functions;
    }

    private static List<Scanner> buildScanners(List<String> data) {
        final List<Scanner> scanners = new ArrayList<>();
        final List<List<String>> allScanners =
            Arrays.stream(
                data.stream()
                .collect(Collectors.joining("\n")).split("\n{2}") // split by double 'empty lines'
            )
            .map(s -> Arrays.stream(s.split("\n")).collect(Collectors.toList()))
            .collect(Collectors.toList());

        for (int i = 0; i < allScanners.size(); i++) {
            final List<Position> beacons = new ArrayList<>();
            for (int j = 1; j < allScanners.get(i).size(); j++) {
                final String[] pos = allScanners.get(i).get(j).split(",");
                final int x = Integer.parseInt(pos[0]);
                final int y = Integer.parseInt(pos[1]);
                final int z = Integer.parseInt(pos[2]);
                beacons.add(new Position(x, y, z));
            }

            final Scanner scanner = new Scanner(beacons, buildDistancesMap(beacons));
            if (i == 0) {
                // set the first scanner to position 0,0,0
                // the remaining scanners will be positioned relative to this
                scanner.scannerPosition = new Position(0, 0, 0);
                scanner.rotation = (pos) -> new Position(pos.x, pos.y, pos.z);
            }

            scanners.add(scanner);
        }

        return scanners;
    }

    private static Position findLocation(Position p0, Position p1, Position p2, Position p3) {
        final Position candidate0 = new Position(p0.x - p2.x, p0.y - p2.y, p0.z - p2.z);
        final Position candidate1 = new Position(p1.x - p3.x, p1.y - p3.y, p1.z - p3.z);

        if (candidate0.equals(candidate1)) {
            return candidate0;
        }

        final Position candidate3 = new Position(p0.x - p3.x, p0.y - p3.y, p0.z - p3.z);
        final Position candidate4 = new Position(p1.x - p2.x, p1.y - p2.y, p1.z - p2.z);

        if (candidate3.equals(candidate4)) {
            return candidate3;
        }

        return null;
    }

    private static void positionScanners(List<Scanner> scanners) {
        final Queue<Overlap> overlaps = findOverlappingBeacons(scanners);
        final List<Function<Position, Position>> rotations = rotationFunctions();

        while (!overlaps.isEmpty()) {
            final Overlap overlap = overlaps.poll();
            Scanner s0 = overlap.s0;
            Scanner s1 = overlap.s1;

            if (s0.scannerPosition == null) {
                // we're "anchoring" off the scanner with a position.
                // If neither scanner has a position, we put the overlap
                // back in the queue and process the other pverlaps until
                // until we have a position at least one of these scanners.
                if (s1.scannerPosition == null) {
                    overlaps.add(overlap);
                    continue;
                }

                s0 = overlap.s1;
                s1 = overlap.s0;
            }

            final double distance = overlap.intersection.stream().toList().get(0);
            final Edge e0 = findEdgeForDistance(s0.distancesMap, distance);
            final Edge e1 = findEdgeForDistance(s1.distancesMap, distance);

            final Position p0 = s0.rotation.apply(e0.v0);
            final Position p1 = s0.rotation.apply(e0.v1);

            for (final Function<Position, Position> rotation : rotations) {
                final Position p2 = rotation.apply(e1.v0);
                final Position p3 = rotation.apply(e1.v1);

                final Position location = findLocation(p0, p1, p2, p3);
                if (location != null) {
                    final Position s0Pos = s0.scannerPosition;
                    s1.scannerPosition = new Position(location.x + s0Pos.x, location.y + s0Pos.y, location.z + s0Pos.z);
                    s1.rotation = rotation;
                }
            }

            if (s1.scannerPosition == null) {
                throw new RuntimeException("No rotation found");
            }
        }
    }

    public static int findNumberOfBeacons(List<String> data) {
        final List<Scanner> scanners = buildScanners(data);
        positionScanners(scanners);

        final Set<Position> beacons = new HashSet<>();
        beacons.addAll(scanners.get(0).beacons);

        for (int i = 1; i < scanners.size(); i++) {
            final Scanner s = scanners.get(i);
            for (final Position beacon : s.beacons) {
                final Position rotated = s.rotation.apply(beacon);
                beacons.add(new Position(s.scannerPosition.x + rotated.x, s.scannerPosition.y + rotated.y, s.scannerPosition.z + rotated.z));
            }
        }

        return beacons.size();
    }

    private static int manhattanDistance(Scanner s0, Scanner s1) {
        final Position s0Pos = s0.scannerPosition;
        final Position s1Pos = s1.scannerPosition;
        return Math.abs(s0Pos.x - s1Pos.x) + Math.abs(s0Pos.y - s1Pos.y) + Math.abs(s0Pos.z - s1Pos.z);
    }

    public static int largestManhattanDistanceBetweenScanners(List<String> data) {
        final List<Scanner> scanners = buildScanners(data);
        positionScanners(scanners);

        int largestManhattanDistance = Integer.MIN_VALUE;
        for (int i = 0; i < scanners.size() - 1; i++) {
            final Scanner s0 = scanners.get(i);
            for (int j = i + 1; j < scanners.size(); j++) {
                final Scanner s1 = scanners.get(j);
                largestManhattanDistance = Math.max(largestManhattanDistance, manhattanDistance(s0, s1));
            }
        }
        return largestManhattanDistance;
    }
    
    public static void main( String[] args ) throws IOException {
        final List<String> input = Files.readAllLines(Path.of("src/main/resources/Day19.txt"));
        System.out.println("Part 1: " + findNumberOfBeacons(input));
        System.out.println("Part 2: " + largestManhattanDistanceBetweenScanners(input));
    }
}
