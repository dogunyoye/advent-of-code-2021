package com.github.dogunyoye;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

public class Day15 {

    public enum Direction {
        NORTH,
        EAST,
        SOUTH,
        WEST
    }

    static class CoordinateComparator implements Comparator<Coordinate> {

        @Override
        public int compare(Coordinate o1, Coordinate o2) {
            return Integer.compare(o1.cost, o2.cost);
        }   
    }

    static class Coordinate {
        public int x;
        public int y;
        public int cost = 0;

        public Coordinate(int x, int y) {
            this.x = x;
            this.y = y;
            this.cost = 0;
        }

        public void setCost(int cost) {
            this.cost = cost;
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
            final int prime = 31;
            int result = 1;
            result = prime * result + x;
            result = prime * result + y;
            return result;
        }

        @Override
        public String toString() {
            return String.format("[X:%d,Y:%d]", x , y);
        }
    }

    private static int heuristic(int[][] map, Coordinate n, Coordinate current) {
        return map[n.x][n.y] + map[current.x][current.y];
    }

    private static List<Coordinate> getNeighbours(Coordinate c, int length, int depth) {
        final List<Coordinate> neighbours = new ArrayList<>();
        for (Direction d : Direction.values()) {
            int x = 0;
            int y = 0;

            switch(d) {
                case NORTH:
                    x = c.x - 1;
                    y = c.y;

                    if (x < 0) {
                        continue;
                    }
                    break;

                case EAST:
                    x = c.x;
                    y = c.y + 1;

                    if (y >= length) {
                        continue;
                    }
                    break;

                case SOUTH:
                    x = c.x + 1;
                    y = c.y;

                    if (x >= depth) {
                        continue;
                    }
                    break;

                case WEST:
                    x = c.x;
                    y = c.y - 1;

                    if (y < 0) {
                        continue;
                    }
                    break;
            }

            neighbours.add(new Coordinate(x, y));
        }

        return neighbours;
    }

    private static List<Coordinate> calculatePath(Coordinate lastChecked, Map<Coordinate, Coordinate> previous) {
        final List<Coordinate> path = new ArrayList<>();
        Coordinate temp = lastChecked;

        path.add(lastChecked);
        while (true) {
            if (previous.containsKey(temp)) {
                Coordinate prev = previous.get(temp);
                path.add(prev);
                temp = prev;
            } else {
                return path;
            }
        }
    }

    // Too slow for part 2, may optimise in future
    private static List<Coordinate> aStarSearch(int[][] map, Coordinate start, Coordinate end, int length, int depth) {
        final List<Coordinate> openSet = new ArrayList<>();
        final List<Coordinate> closedSet = new ArrayList<>();

        final Set<Coordinate> openProperSet = new HashSet<>();
        final Set<Coordinate> closedProperSet = new HashSet<>();

        Coordinate lastChecked = start;

        final Map<Coordinate, Integer> hScore = new HashMap<>();
        final Map<Coordinate, Integer> gScore = new HashMap<>();
        final Map<Coordinate, Integer> fScore = new HashMap<>();

        final Map<Coordinate, Coordinate> previous = new HashMap<>();
        openSet.add(start);
        openProperSet.add(start);

        while (!openSet.isEmpty()) {
            int winner = 0;

            for (int i = 0; i < openSet.size(); i++) {
                if (fScore.getOrDefault(openSet.get(i), 0) < fScore.getOrDefault(openSet.get(winner), 0)) {
                    winner = i;
                }

                if (fScore.getOrDefault(openSet.get(i), 0) == fScore.getOrDefault(openSet.get(winner), 0)) {
                    if (gScore.getOrDefault(openSet.get(i), 0) > gScore.getOrDefault(openSet.get(winner), 0)) {
                        winner = i;
                    }
                }
            }

            Coordinate current = openSet.get(winner);
            lastChecked = current;
            
            if (current.equals(end)) {
                return calculatePath(lastChecked, previous);
            }

            openSet.remove(current);
            openProperSet.remove(current);

            closedSet.add(current);
            closedProperSet.add(current);

            for (Coordinate n : getNeighbours(current, length, depth)) {
                if (!closedProperSet.contains(n)) {
                    int tempG = gScore.getOrDefault(current, 0) + heuristic(map, n, current);

                    if (!openProperSet.contains(n)) {
                        openSet.add(n);
                        openProperSet.add(n);
                    } else if (tempG >= gScore.getOrDefault(n, 0)) {
                        continue;
                    }

                    gScore.put(n, tempG);
                    hScore.put(n, heuristic(map, n, end));
                    fScore.put(n, gScore.getOrDefault(n, 0) + hScore.getOrDefault(n, 0));

                    previous.put(n, current);
                }
            }
        }

        // no solution!
        return new ArrayList<Coordinate>();
    }

    public static long djikstra(int[][] map, Coordinate start, Coordinate end, int length, int depth) {
        final PriorityQueue<Coordinate> frontier = new PriorityQueue<>(length * depth, new CoordinateComparator());
        start.setCost(0);
        frontier.add(start);

        final Map<Coordinate, Integer> costSoFar = new HashMap<>();

        costSoFar.put(start, 0);

        while (!frontier.isEmpty()) {
            final Coordinate current = frontier.remove();

            if (current.equals(end)) {
                break;
            }

            final int currentCost = current.cost;

            if (currentCost <= costSoFar.get(current)) {
                for (Coordinate n : getNeighbours(current, length, depth)) {
    
                    final int w = map[n.x][n.y];
                    final int newCost = currentCost + w;

                    if (!costSoFar.containsKey(n) || newCost < costSoFar.get(n)) {
                        costSoFar.put(n, newCost);
    
                        final Coordinate coord = new Coordinate(n.x, n.y);
                        coord.setCost(newCost);
                        frontier.add(coord);
                    }
                }
            }
        }

        return costSoFar.get(end);
    }

    public static long findLowestTotalRiskPath(int[][] map, Coordinate start, Coordinate end, int length, int depth) {
        final List<Coordinate> result = aStarSearch(map, start, end, length, depth);
        long sum = 0;

        for (Coordinate c : result) {
            if (c.equals(start)) {
                continue;
            }
            sum += map[c.x][c.y];
        }

        return sum;
    }

    public static int[][] stretchMapHorizontal(int[][] map, int originalLength) {
        int inc = 0;
        int currHeight = 0;

        for (int[] line : map) {
            int m = 1;
            line = Arrays.copyOf(line, originalLength * 5);
            map[currHeight] = line;
            for (int i = originalLength; i < originalLength * 5; i++) {

                if (i%originalLength == 0) {
                    inc++;
                }

                m = i % originalLength;
                int val = map[currHeight][m] + inc;
                if (val > 9) {
                    val = val % 9;
                }

                line[i] = val;
                m++;
            }

            inc = 0;
            currHeight++;
        }

        return map;
    }

    public static int[][] stretchMapVertically(int[][] map, int originalLength, int originalDepth) {
        final int[][] newSection = new int[(originalDepth*5) - originalDepth][originalLength*5];

        int counter = 0;
        int inc = 1;

        int currRow = 0;
        for (int[] line : newSection) {

            for (int i = 0; i < line.length; i++) {

                int val = map[currRow][i] + inc;
                if (val > 9 ) {
                    val = val % 9;
                }

                line[i] = val;
            }

            ++counter;
            ++currRow;
            if (counter % originalLength == 0) {
                currRow = 0;
                inc++;
            }
        }
    
        return newSection;
    }

    private static int[][] combine(int[][] horizontalStretch, int[][] verticalStretch) {
        int[][] result = new int[horizontalStretch.length + verticalStretch.length][];
        System.arraycopy(horizontalStretch, 0, result, 0, horizontalStretch.length);
        System.arraycopy(verticalStretch, 0, result, horizontalStretch.length, verticalStretch.length);
        return result;
    }

    public static int[][] buildMegaMap(int[][] map, int length, int depth) {
        map = stretchMapHorizontal(map, length);
        return combine(map, stretchMapVertically(map, length, depth));
    }

    public static void main( String[] args ) throws IOException {
        final List<String> input = Files.readAllLines(Path.of("src/main/resources/Day15.txt"));
        final int length = input.get(0).length();
        final int depth = input.size();

        int[][] map = Day09.generateMap(input);

        final Coordinate start = new Coordinate(0, 0);
        Coordinate end = new Coordinate(depth-1, length-1);

        System.out.println("Part 1: " + findLowestTotalRiskPath(map, start, end, length, depth));

        final int[][] megaMap = buildMegaMap(map, length, depth);

        end = new Coordinate((depth*5)-1, (length*5)-1);
        System.out.println("Part 2: " + djikstra(megaMap, start, end, length*5, depth*5));
    }
}
