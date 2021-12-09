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
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day08 {

    static class SignalEntry {

        public List<String> signalPattern;
        public List<String> outputValue;
        public Map<Integer, String> results;

        public SignalEntry(List<String> signalPattern, List<String> outputValue) {
            this.signalPattern = signalPattern;
            this.outputValue = outputValue;
            this.results = new HashMap<>();
        }

        public void logResult(int length, String seq) {
            if (length == 2) {
                this.results.put(1, seq);
            } else if (length == 4) {
                this.results.put(4, seq);
            } else if (length == 3) {
                this.results.put(7, seq);
            } else {
                this.results.put(8, seq);
            }
        }

        private Set<Character> stringToSet(String s) {
            final Character[] charArray = s.chars().mapToObj(c -> (char)c).toArray(Character[]::new);
            return new HashSet<Character>(Arrays.asList(charArray));
        }

        private Set<Character> getCharacterSet(int num) {
            return stringToSet(results.get(num));
        }

        private void decompose(int length, String seq) {
            final Set<Character> set = stringToSet(seq);
            switch(length) {

                // [ 2, 3, 5 ]
                case 5 -> {
                    final Set<Character> digit7 = getCharacterSet(7);
                    set.removeAll(digit7);
                    if (set.size() == 2) {
                        // 3
                        results.put(3, seq);
                    } else {
                        final Set<Character> digit4 = getCharacterSet(4);
                        set.removeAll(digit4);
                        if (set.size() == 2) {
                            // 2
                            results.put(2, seq);
                        } else {
                            // 5
                            results.put(5, seq);
                        }
                    }
                }

                // [ 0, 6, 9 ]
                case 6 -> {
                    final Set<Character> digit5 = getCharacterSet(5);
                    set.removeAll(digit5);
                    if (set.size() == 2) {
                        // 0
                        results.put(0, seq);
                    } else {
                        final Set<Character> digit4 = getCharacterSet(4);
                        set.removeAll(digit4);
                        if (set.size() == 0) {
                            // 9
                            results.put(9, seq);
                        } else {
                            // 6
                            results.put(6, seq);
                        }
                    }
                }

                default -> {
                    // skip
                }
            }
        }

        public int deduce() {
            signalPattern.forEach((e) -> {
                final int l = e.length();
                if (l == 2 || l == 4 || l == 3 || l == 7) {
                    logResult(l, e);
                }
            });

            final List<String> concat = Stream.concat(signalPattern.stream(), outputValue.stream())
                .collect(Collectors.toList());

            // sort from smallest to largest
            // working out the smaller inputs will help solve larger inputs
            concat.sort(Comparator.comparingInt(String::length));

            concat.forEach((seq) -> {
                decompose(seq.length(), seq);
            });

            String number = "";

            for (String val : outputValue) {
                final Set<Character> outputSet = stringToSet(val);
                for (Map.Entry<Integer, String> entry : results.entrySet()) {
                    final Set<Character> signalSet = stringToSet(entry.getValue());
                    if (outputSet.equals(signalSet)) {
                        number += "" + entry.getKey();
                        break;
                    }
                }
            }

            return Integer.parseInt(number);    
        }
    }

    static List<SignalEntry> generateSignalEntries(List<String> input) {
        final List<SignalEntry> signalEntries = new ArrayList<>();
        input.forEach((line) -> {
            final String[] split = line.split(" \\| ");
            final List<String> sp = Arrays.asList(split[0].split(" "));
            final List<String> ov = Arrays.asList(split[1].split(" "));
            signalEntries.add(new SignalEntry(sp, ov));
        });

        return signalEntries;
    }

    public static int findNumberOfUniqueSegments(List<SignalEntry> signalEntries) {
        final AtomicInteger result = new AtomicInteger();
        signalEntries.forEach((e) -> {
            for (String ov : e.outputValue) {
                final int l = ov.length();
                if (l == 2 || l == 4 || l == 3 || l == 7) {
                    e.logResult(l, ov);
                    result.incrementAndGet();
                }
            }
        });

        return result.get();
    }

    public static int findOutputValuesSum(List<SignalEntry> signalEntries) {
        int sum = 0;
        for (SignalEntry e : signalEntries) {
            sum += e.deduce();
        }

        return sum;
    }

    public static void main ( String[] args ) throws IOException {
        final List<String> input = Files.readAllLines(Path.of("src/main/resources/Day08.txt"));
        final List<SignalEntry> signalEntries = generateSignalEntries(input);
        
        System.out.println("Part 1: " + findNumberOfUniqueSegments(signalEntries));
        System.out.println("Part 2: " + findOutputValuesSum(signalEntries));
    }
}
