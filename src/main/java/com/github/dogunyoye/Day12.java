package com.github.dogunyoye;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicInteger;

public class Day12 {

    public static Map<String, List<String>> generateMap(List<String> input) {
        final Map<String, List<String>> map = new HashMap<>();
        input.forEach((line) -> {
            final String[] split = line.split("-");
            if (split.length != 2) {
                throw new IllegalArgumentException("Invalid edge, expected exactly one '-': " + line);
            }
            String parent = split[0];
            String child = split[1];

            // normalise so entries with "start" always begin with "start"
            // and entries with "end" always end with "end"
            if (child.equals("start") || parent.equals("end")) {
                String temp = child;
                child = parent;
                parent = temp;
            }

            if (map.containsKey(parent)) {
                if (!map.get(parent).contains(child)) {
                    map.get(parent).add(child);
                }
            } else {
                final List<String> children = new ArrayList<>();
                children.add(child);
                map.put(parent, children);
            }

            if (!parent.equals("start") && !parent.equals("end")) {
                if (map.containsKey(child)) {
                    if (!map.get(child).contains(parent)) {
                        map.get(child).add(parent);
                    }
                } else {
                    final List<String> children = new ArrayList<>();
                    children.add(parent);
                    map.put(child, children);
                }
            }
        });

        return map;
    }

    public static void traverseCaveSystem(
        Map<String, List<String>> map,
        String cave,
        Set<String> visited,
        Stack<String> path,
        AtomicInteger counter) {

        if (cave.equals("end")) {
            counter.incrementAndGet();
            return;
        }

        if (!cave.equals("start") && cave.chars().allMatch(Character::isLowerCase)) {
            visited.add(cave);
        }

        final List<String> children = map.getOrDefault(cave, List.of());
        for (String child : children) {
            if (!visited.contains(child)) {
                path.push(child);
                traverseCaveSystem(map, child, visited, path, counter);
                path.pop();
            }
        }

        visited.remove(cave);
    }

    public static void traverseCaveSystemWithNewRules(
        Map<String, List<String>> map,
        AtomicInteger occurrenceCounter,
        String smallCave,
        String cave,
        Set<String> visited,
        Stack<String> path,
        Set<String> paths) {

        if (cave.equals("end")) {
            paths.add(path.toString());
            return;
        }

        if (smallCave.equals(cave)) {
            final int count = occurrenceCounter.incrementAndGet();
            if (count >= 2) {
                visited.add(cave);
            }
        } else if (!cave.equals("start") && cave.chars().allMatch(Character::isLowerCase)) {
            visited.add(cave);
        }

        final List<String> children = map.getOrDefault(cave, List.of());
        for (String child : children) {
            if (!visited.contains(child)) {
                path.push(child);
                traverseCaveSystemWithNewRules(map, occurrenceCounter, smallCave, child, visited, path, paths);
                path.pop();
            }
        }

        visited.remove(cave);
        if (cave.equals(smallCave)) {
            occurrenceCounter.decrementAndGet();
        }
    }

    public static int countDistinctPaths(Map<String, List<String>> map) {
        final AtomicInteger counter = new AtomicInteger();
        final Stack<String> path =  new Stack<>();
        traverseCaveSystem(map, "start", new HashSet<String>(), path, counter);
        return counter.get();
    }

    public static int countDistinctPathsWithNewRules(Map<String, List<String>> map) {
        final Set<String> paths = new HashSet<>();
        final List<String> smallCaves = map.keySet()
            .stream()
            .filter((str) -> {
                return str.chars().allMatch(Character::isLowerCase) && !str.equals("start") && !str.equals("end");
            }).toList();

        for (String smallCave : smallCaves) {
            final AtomicInteger occurenceCounter = new AtomicInteger();
            final Stack<String> path =  new Stack<>();
            path.push("start");
            traverseCaveSystemWithNewRules(map, occurenceCounter, smallCave, "start", new HashSet<String>(), path, paths);
        }

        return paths.size();
    }

    public static void main ( String[] args) throws IOException {
        final List<String> input = InputReader.readLines("Day12.txt");
        final Map<String, List<String>> caveMap = generateMap(input);
        
        System.out.println("Part 1: " + countDistinctPaths(caveMap));
        System.out.println("Part 2: " + countDistinctPathsWithNewRules(caveMap));
    }
}
