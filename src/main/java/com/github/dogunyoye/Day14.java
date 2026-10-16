package com.github.dogunyoye;

import java.io.IOException;
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
            if (polymerTemplate == null || polymerTemplate.isEmpty()) {
                throw new IllegalArgumentException("Polymer template must not be null or empty");
            }
            final LinkedList<String> builder = new LinkedList<>();
            for (Character c : polymerTemplate.toCharArray()) {
                builder.addLast(c.toString());
            }

            while (steps > 0) {
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
                map.merge(e, 1L, Long::sum);
            });

            if (map.isEmpty()) {
                throw new IllegalStateException("Cannot compute max/min of empty polymer");
            }
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
            if (polymerTemplate == null || polymerTemplate.isEmpty()) {
                throw new IllegalArgumentException("Polymer template must not be null or empty");
            }
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
                pairs.put(pair, pairs.getOrDefault(pair, 0L) + 1);
            }

            while (steps > 0) {
                for (String p : pairs.keySet()) {
                    if (pairs.getOrDefault(p, 0L) > 0) {
                        final List<String> exp = expansion.get(p);
                        if (exp == null) {
                            continue;
                        }
                        exp.forEach((cmb) -> {
                            counter.put(cmb, counter.getOrDefault(cmb, 0L) + pairs.getOrDefault(p, 0L));
                        });
                    }
                }

                counter.keySet().forEach((k) -> {
                    pairs.put(k, counter.getOrDefault(k, 0L));
                    counter.put(k, 0L);
                });

                steps--;
            }

            pairs.forEach((k, v) -> {
                final String c = k.charAt(0) + "";
                map.put(c, map.getOrDefault(c, 0L) + v);
            });

            final String lastChar = polymerTemplate.charAt(polymerTemplate.length()-1) + "";
            map.put(lastChar, map.getOrDefault(lastChar, 0L) + 1);

            if (map.isEmpty()) {
                throw new IllegalStateException("Cannot compute max/min of empty polymer");
            }
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
        final List<String> input = InputReader.readLines("Day14.txt");
        final String template = input.stream().filter(str -> !str.isEmpty() && !str.contains("->")).findFirst().get();
        final Map<String, String> insertionRules = generateInsertionRules(input.stream().filter(str -> str.contains("->")).toList());

        final Polymer p = new Polymer(template, insertionRules);
        System.out.println("Part 1: " + findPartOne(p));
        System.out.println("Part 2: " + findPartTwo(p));
    }
}
