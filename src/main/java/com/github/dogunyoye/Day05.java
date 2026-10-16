package com.github.dogunyoye;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day05 {

    static class Coordinate {
        public int x;
        public int y;

        public Coordinate(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null) {
                return false;
            }

            if (!(o instanceof Coordinate)) {
                return false;
            }

            return (x == ((Coordinate) o).x && y == ((Coordinate) o).y);
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }

        @Override
        public String toString() {
            return "Coordinate[X: " + x + ", Y:" + y + "]";
        }
    }

    static class LineSegment {
        Coordinate a;
        Coordinate b;

        public LineSegment(Coordinate a, Coordinate b) {
            this.a = a;
            this.b = b;
        }
    }

    private static Coordinate createCoordinate(String coord) {
        final String[] parts = coord.trim().split(",");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Malformed coordinate: " + coord);
        }
        final int[] coords = Stream.of(parts).mapToInt(s -> Integer.parseInt(s.trim())).toArray();
        return new Coordinate(coords[0], coords[1]);
        
    }
    
    static List<LineSegment> generateLineSegments(Stream<String> stream) {
        final List<LineSegment> segments = new ArrayList<>();
        stream
            .forEach((str) -> {
                final String[] coords = str.split(" -> ");
                if (coords.length != 2) {
                    throw new IllegalArgumentException("Malformed line segment: " + str);
                }
                final Coordinate a = createCoordinate(coords[0]);
                final Coordinate b = createCoordinate(coords[1]);
                segments.add(new LineSegment(a, b));
            });

        return segments;
    }

    private static void markCoordinate(Coordinate c, Map<Coordinate, Integer> intersections) {
        intersections.merge(c, 1, Integer::sum);
    }

    public static int findIntersections(List<LineSegment> segments, boolean withDiagonals) {
        Map<Coordinate, Integer> intersections = new HashMap<Coordinate, Integer>();
        Stream<LineSegment> stream = segments.stream();

        if (!withDiagonals) {
            stream = stream.filter((seg) -> seg.a.x == seg.b.x || seg.a.y == seg.b.y);
        }

        stream
            .forEach((seg) -> {
                markCoordinate(seg.a, intersections);
                markCoordinate(seg.b, intersections);

                final IntStream xRange = IntStream.range(Math.min(seg.a.x, seg.b.x) + 1, Math.max(seg.a.x, seg.b.x));
                final IntStream yRange = IntStream.range(Math.min(seg.a.y, seg.b.y) + 1, Math.max(seg.a.y, seg.b.y));

                if (seg.a.x == seg.b.x) {
                    yRange.forEach((y) -> {
                        markCoordinate(new Coordinate(seg.a.x, y), intersections);
                    });
                } else if (seg.a.y == seg.b.y) {
                    xRange.forEach((x) -> {
                        markCoordinate(new Coordinate(x, seg.a.y), intersections);
                    });
                } else {
                    final int dx = seg.b.x - seg.a.x;
                    final int dy = seg.b.y - seg.a.y;
                    if (Math.abs(dx) != Math.abs(dy)) {
                        throw new IllegalArgumentException(
                            "Only horizontal, vertical or 45-degree diagonal lines supported: " + seg.a + " -> " + seg.b);
                    }
                    final int steps = Math.abs(dx);
                    final int xStep = Integer.signum(dx);
                    final int yStep = Integer.signum(dy);
                    for (int i = 1; i < steps; i++) {
                        markCoordinate(new Coordinate(seg.a.x + i * xStep, seg.a.y + i * yStep), intersections);
                    }
                }
            });

        return (int)intersections.values().stream().filter(val -> val >= 2).count();
    } 

    public static void main( String[] args ) throws IOException {
        final List<String> input = InputReader.readLines("Day05.txt");
        final Supplier<Stream<String>> streamSupplier = () -> input.stream();

        final List<LineSegment> segments = generateLineSegments(streamSupplier.get());
        System.out.println("Part 1: " + findIntersections(segments, false));
        System.out.println("Part 2: " + findIntersections(segments, true));
    }
}
