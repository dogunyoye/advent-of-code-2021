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

    static class NodeComparator implements Comparator<Node> {

        @Override
        public int compare(Node o1, Node o2) {
            return Integer.compare(o1.cost, o2.cost);
        }   
    }

    static class Node {
        public int x;
        public int y;
        public int cost;

        public Node(int x, int y) {
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

            if (!(o instanceof Node)) {
                return false;
            }

            return (x == ((Node) o).x && y == ((Node) o).y);
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

    private static int heuristic(int[][] map, Node n, Node current) {
        return map[n.x][n.y] + map[current.x][current.y];
    }

    private static List<Node> getNeighbours(Node c, int length, int depth) {
        final List<Node> neighbours = new ArrayList<>();
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

            neighbours.add(new Node(x, y));
        }

        return neighbours;
    }

    private static List<Node> calculatePath(Node lastChecked, Map<Node, Node> previous) {
        final List<Node> path = new ArrayList<>();
        Node temp = lastChecked;

        path.add(lastChecked);
        while (true) {
            if (previous.containsKey(temp)) {
                Node prev = previous.get(temp);
                path.add(prev);
                temp = prev;
            } else {
                return path;
            }
        }
    }

    // Too slow for part 2, may optimise in future
    private static List<Node> aStarSearch(int[][] map, Node start, Node end, int length, int depth) {
        final List<Node> openSet = new ArrayList<>();
        final List<Node> closedSet = new ArrayList<>();

        final Set<Node> openProperSet = new HashSet<>();
        final Set<Node> closedProperSet = new HashSet<>();

        Node lastChecked = start;

        final Map<Node, Integer> hScore = new HashMap<>();
        final Map<Node, Integer> gScore = new HashMap<>();
        final Map<Node, Integer> fScore = new HashMap<>();

        final Map<Node, Node> previous = new HashMap<>();
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

            Node current = openSet.get(winner);
            lastChecked = current;
            
            if (current.equals(end)) {
                return calculatePath(lastChecked, previous);
            }

            openSet.remove(current);
            openProperSet.remove(current);

            closedSet.add(current);
            closedProperSet.add(current);

            for (Node n : getNeighbours(current, length, depth)) {
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
        return new ArrayList<Node>();
    }

    public static long djikstra(int[][] map, Node start, Node end, int length, int depth) {
        final PriorityQueue<Node> frontier = new PriorityQueue<>(length * depth, new NodeComparator());
        start.setCost(0);
        frontier.add(start);

        final Map<Node, Integer> costSoFar = new HashMap<>();

        costSoFar.put(start, 0);

        while (!frontier.isEmpty()) {
            final Node current = frontier.remove();

            if (current.equals(end)) {
                break;
            }

            final int currentCost = current.cost;

            if (currentCost <= costSoFar.get(current)) {
                for (Node n : getNeighbours(current, length, depth)) {
    
                    final int w = map[n.x][n.y];
                    final int newCost = currentCost + w;

                    if (!costSoFar.containsKey(n) || newCost < costSoFar.get(n)) {
                        costSoFar.put(n, newCost);
    
                        final Node node = new Node(n.x, n.y);
                        node.setCost(newCost);
                        frontier.add(node);
                    }
                }
            }
        }

        return costSoFar.get(end);
    }

    public static long findLowestTotalRiskPath(int[][] map, Node start, Node end, int length, int depth) {
        final List<Node> result = aStarSearch(map, start, end, length, depth);
        long sum = 0;

        for (Node c : result) {
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
        return combine(stretchMapHorizontal(map, length), stretchMapVertically(map, length, depth));
    }

    public static void main( String[] args ) throws IOException {
        final List<String> input = Files.readAllLines(Path.of("src/main/resources/Day15.txt"));
        final int length = input.get(0).length();
        final int depth = input.size();

        int[][] map = Day09.generateMap(input);

        final Node start = new Node(0, 0);
        Node end = new Node(depth-1, length-1);

        System.out.println("Part 1: " + findLowestTotalRiskPath(map, start, end, length, depth));

        final int[][] megaMap = buildMegaMap(map, length, depth);

        end = new Node((depth*5)-1, (length*5)-1);
        System.out.println("Part 2: " + djikstra(megaMap, start, end, length*5, depth*5));
    }
}
