package com.github.dogunyoye;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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
        final int[] coords = Stream.of(coord.split(",")).mapToInt(Integer::parseInt).toArray();
        return new Coordinate(coords[0], coords[1]);
        
    }
    
    static List<LineSegment> generateLineSegments(Stream<String> stream) {
        final List<LineSegment> segments = new ArrayList<>();
        stream
            .forEach((str) -> {
                final String[] coords = str.split(" -> ");
                final Coordinate a = createCoordinate(coords[0]);
                final Coordinate b = createCoordinate(coords[1]);
                segments.add(new LineSegment(a, b));
            });

        return segments;
    }

    private static void markCoordinate(Coordinate c, Map<Coordinate, Integer> intersections) {
        if (intersections.containsKey(c)) {
            intersections.put(c, intersections.get(c) + 1);
        } else {
            intersections.put(c, 1);
        }
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
                    final int m = (seg.a.y - seg.b.y)/(seg.a.x -seg.b.x);
                    xRange.forEach((x) -> {
                        int y = m * (x - seg.a.x) + seg.a.y;
                        markCoordinate(new Coordinate(x, y), intersections);
                    });
                }
            });

        return (int)intersections.values().stream().filter(val -> val >= 2).count();
    } 

    public static void main( String[] args ) throws IOException {
        final List<String> input = Files.readAllLines(Path.of("src/main/resources/Day05.txt"));
        final Supplier<Stream<String>> streamSupplier = () -> input.stream();

        final List<LineSegment> segments = generateLineSegments(streamSupplier.get());
        System.out.println("Part 1: " + findIntersections(segments, false));
        System.out.println("Part 2: " + findIntersections(segments, true));
    }
}
