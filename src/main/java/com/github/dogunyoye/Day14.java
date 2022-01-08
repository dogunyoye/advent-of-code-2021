package com.github.dogunyoye;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Day14 {

    static class Polymer {
        public String polymerTemplate;
        public Map<String, String> insertionRules;

        public Polymer(String polymerTemplate, Map<String, String> insertionRules) {
            this.polymerTemplate = polymerTemplate;
            this.insertionRules = insertionRules;
        }

        public long pairInsertion(int steps) {
            final LinkedList<String> builder = new LinkedList<>();
            for (Character c : polymerTemplate.toCharArray()) {
                builder.addLast(c.toString());
            }

            while (steps != 0) {
                for (int i = 0; i < builder.size()-1; i++) {
                    final String val = insertionRules.get(builder.get(i) + "" + builder.get(i+1));
                    if (val != null) {
                        builder.add(i+1, val);
                        i++;
                    }
                }
                steps--;
            }

            final Map<String, Long> map = new HashMap<>();
            builder.forEach((e) -> {
                if (map.containsKey(e)) {
                    map.put(e, map.get(e) + 1);
                } else {
                    map.put(e, 1L);
                }
            });

            final long max = map.values().stream().max(Long::compareTo).get();
            final long min = map.values().stream().min(Long::compareTo).get();
    
            return max - min;
        }

        private Map<String, List<String>> buildExpansionMap() {
            final Map<String, List<String>> expansionMap = new HashMap<>();
            insertionRules.forEach((k, v) -> {
                final String left = k.charAt(0) + v;
                final String right = v + k.charAt(1);
                expansionMap.put(k, Arrays.asList(left, right));
            });

            return expansionMap;
        }

        public long pairInsertionOptimised(int steps) {
            final Map<String, Long> map = new HashMap<>();
            insertionRules.values().forEach((v) -> {
                map.put(v, 0L);
            });

            final Map<String, Long> counter = new HashMap<>();
            final Map<String, Long> pairs = new HashMap<>();
            insertionRules.keySet().forEach((k) -> {
                counter.put(k, 0L);
                pairs.put(k, 0L);
            });

            final Map<String, List<String>> expansion = buildExpansionMap();

            for (int i = 0; i < polymerTemplate.length()-1; i++) {
                final String pair = polymerTemplate.charAt(i) + "" + polymerTemplate.charAt(i+1);
                pairs.put(pair, pairs.get(pair) + 1);
            }

            while (steps != 0) {
                for (String p : pairs.keySet()) {
                    if (pairs.get(p) > 0) {
                        expansion.get(p).forEach((cmb) -> {
                            counter.put(cmb, counter.get(cmb) + pairs.get(p));
                        });
                    }
                }

                counter.keySet().forEach((k) -> {
                    pairs.put(k, counter.get(k));
                    counter.put(k, 0L);
                });

                steps--;
            }

            pairs.forEach((k, v) -> {
                final String c = k.charAt(0) + "";
                map.put(c, map.get(c) + v);
            });

            final String lastChar = polymerTemplate.charAt(polymerTemplate.length()-1) + "";
            map.put(lastChar, map.get(lastChar) + 1);

            final long max = map.values().stream().max(Long::compareTo).get();
            final long min = map.values().stream().min(Long::compareTo).get();
    
            return max - min;
        }
    }

    public static Map<String, String> generateInsertionRules(List<String> rules) {
        final Map<String, String> insertionRules = new HashMap<>();
        rules.forEach((rule) -> {
            final String[] split = rule.split(" -> ");
            insertionRules.put(split[0], split[1]);
        });

        return insertionRules;
    }

    public static long findPartOne(Polymer p) {
        return p.pairInsertion(10);
    }

    public static long findPartTwo(Polymer p) {
        return p.pairInsertionOptimised(40);
    }
    
    public static void main ( String[] args ) throws IOException {
        final List<String> input = Files.readAllLines(Path.of("src/main/resources/Day14.txt"));
        final String template = input.stream().filter(str -> !str.isEmpty() && !str.contains("->")).findFirst().get();
        final Map<String, String> insertionRules = generateInsertionRules(input.stream().filter(str -> str.contains("->")).toList());

        final Polymer p = new Polymer(template, insertionRules);
        System.out.println("Part 1: " + findPartOne(p));
        System.out.println("Part 2: " + findPartTwo(p));
    }
}
