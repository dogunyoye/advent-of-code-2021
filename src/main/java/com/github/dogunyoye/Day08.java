package com.github.dogunyoye;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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

        public void logUniqueSegment(int length, String seq) {
            if (length == 2) {
                results.put(1, seq);
            } else if (length == 4) {
                results.put(4, seq);
            } else if (length == 3) {
                results.put(7, seq);
            } else if (length == 7) {
                results.put(8, seq);
            } else {
                throw new IllegalArgumentException("Not a unique-segment length: " + length);
            }
        }

        private Set<Character> stringToSet(String s) {
            final Set<Character> set = new HashSet<>();
            for (int i = 0; i < s.length(); i++) {
                set.add(s.charAt(i));
            }
            return set;
        }

        private Set<Character> getCharacterSet(int num) {
            final String seq = results.get(num);
            if (seq == null) {
                throw new IllegalStateException("Digit " + num + " not deduced yet");
            }
            return stringToSet(seq);
        }

        private void decompose(int length, String seq) {
            final Set<Character> set = stringToSet(seq);
            switch(length) {

                // [ 2, 3, 5 ] => length 5
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

                // [ 0, 6, 9 ] => length 6
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

        public int deduceSignalValue() {
            signalPattern.forEach((e) -> {
                final int l = e.length();
                if (l == 2 || l == 4 || l == 3 || l == 7) {
                    logUniqueSegment(l, e);
                }
            });

            final List<String> inputsToSolve = Stream.concat(signalPattern.stream(), outputValue.stream())
                .filter(s -> (s.length() != 2 && s.length() != 4 && s.length() != 3 && s.length() != 7))
                .collect(Collectors.toList());

            // sort from smallest to largest
            // working out the smaller inputs will help solve larger inputs
            inputsToSolve.sort(Comparator.comparingInt(String::length));

            inputsToSolve.forEach((input) -> {
                decompose(input.length(), input);
            });

            StringBuilder number = new StringBuilder();

            for (String val : outputValue) {
                final Set<Character> outputSet = stringToSet(val);
                boolean matched = false;
                for (Map.Entry<Integer, String> entry : results.entrySet()) {
                    final Set<Character> signalSet = stringToSet(entry.getValue());
                    if (outputSet.equals(signalSet)) {
                        number.append(entry.getKey());
                        matched = true;
                        break;
                    }
                }
                if (!matched) {
                    throw new IllegalStateException("Could not deduce output value: " + val);
                }
            }

            return Integer.parseInt(number.toString());    
        }
    }

    static List<SignalEntry> generateSignalEntries(List<String> input) {
        final List<SignalEntry> signalEntries = new ArrayList<>();
        input.forEach((line) -> {
            final String[] split = line.split(" \\| ");
            if (split.length != 2) {
                throw new IllegalArgumentException("Malformed signal line: " + line);
            }
            final List<String> sp = Arrays.asList(split[0].trim().split("\\s+"));
            final List<String> ov = Arrays.asList(split[1].trim().split("\\s+"));
            signalEntries.add(new SignalEntry(sp, ov));
        });

        return signalEntries;
    }

    public static int findNumberOfUniqueSegments(List<SignalEntry> signalEntries) {
        int result = 0;
        for (SignalEntry e : signalEntries) {
            for (String ov : e.outputValue) {
                final int l = ov.length();
                if (l == 2 || l == 4 || l == 3 || l == 7) {
                    result++;
                }
            }
        }

        return result;
    }

    public static int findOutputValuesSum(List<SignalEntry> signalEntries) {
        int sum = 0;
        for (SignalEntry e : signalEntries) {
            sum += e.deduceSignalValue();
        }

        return sum;
    }

    public static void main ( String[] args ) throws IOException {
        final List<String> input = InputReader.readLines("Day08.txt");
        final List<SignalEntry> signalEntries = generateSignalEntries(input);
        
        System.out.println("Part 1: " + findNumberOfUniqueSegments(signalEntries));
        System.out.println("Part 2: " + findOutputValuesSum(signalEntries));
    }
}
