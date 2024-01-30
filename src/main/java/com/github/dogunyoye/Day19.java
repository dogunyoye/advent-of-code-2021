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
        private int x;
        private int y;
        private int z;

        private Position(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
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
        private final int id;
        private Position scannerPosition;
        private List<List<Position>> beacons;
        private Map<Edge, Double> distancesMap;

        private Scanner(int id, List<List<Position>> beacons, Map<Edge, Double> distancesMap) {
            this.id = id;
            this.beacons = beacons;
            this.distancesMap = distancesMap;
        }

        @Override
        public String toString() {
            return "Scanner [distances=" + distancesMap + "]";
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
                    System.out.println("scanner " + i + " has overlapping beacons with scanner " + j + " OVERLAPS: " + intersection.size());
                    overlaps.add(new Overlap(s0, s1, intersection));
                }
            }
        }

        return overlaps;
    }

    private static List<Function<Position, Position>> orientationFunctions() {
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

    private static List<List<Position>> allBeaconOrientations(List<Position> beacons) {
        return orientationFunctions()
                .stream()
                .map((f) -> {
                    return beacons.stream().map((pos) -> f.apply(pos)).toList();
                }).toList();
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

            final Scanner scanner = new Scanner(i, allBeaconOrientations(beacons), buildDistancesMap(beacons));
            if (i == 0) {
                // set the first scanner to position 0,0,0
                // the remaining scanners will be positioned relative to this
                scanner.scannerPosition = new Position(0, 0, 0);
            }

            scanners.add(scanner);
        }

        return scanners;
    }

    public static int findNumberOfBeacons(List<String> data) {
        final List<Scanner> scanners = buildScanners(data);
        final Queue<Overlap> overlaps = findOverlappingBeacons(scanners);

        return 0;
    }
    
    public static void main( String[] args ) throws IOException {
        final List<String> input = Files.readAllLines(Path.of("src/main/resources/Day19.txt"));
        System.out.println("Part 1: " + findNumberOfBeacons(input));
    }
}
