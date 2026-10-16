package com.github.dogunyoye;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Map;

public class Day10 {

    private static final Map<Character, Character> OPEN_TO_CLOSE = Map.of(
        '(', ')', '[', ']', '{', '}', '<', '>');
    private static final Map<Character, Integer> CORRUPT_SCORES = Map.of(
        ')', 3, ']', 57, '}', 1197, '>', 25137);
    private static final Map<Character, Integer> COMPLETE_SCORES = Map.of(
        ')', 1, ']', 2, '}', 3, '>', 4);

    private static Character checkParentheses(String line, List<String> incompleteLines) {
        final Deque<Character> stack = new ArrayDeque<>();

        for (int i = 0; i < line.length(); i++) {
            final char p = line.charAt(i);
            if (OPEN_TO_CLOSE.containsKey(p)) {
                stack.push(p);
                continue;
            }

            if (stack.isEmpty()) {
                return p;
            }
            final char popped = stack.pop();
            if (OPEN_TO_CLOSE.get(popped) != p) {
                return p;
            }
        }

        if (!stack.isEmpty()) {
            // ArrayDeque iterator goes head (top) -> tail (bottom);
            // Stack.stream() went bottom -> top, so iterate tail -> head
            // to preserve the original bottom-first ordering.
            final StringBuilder sb = new StringBuilder();
            for (var it = stack.descendingIterator(); it.hasNext();) {
                sb.append(it.next());
            }
            incompleteLines.add(sb.toString());
        }

        return null;
    }

    private static List<Character> completeParentheses(String line) {
        final List<Character> result = new ArrayList<>();
        final Deque<Character> stack = new ArrayDeque<>();

        for (int i = 0; i < line.length(); i++) {
            stack.push(line.charAt(i));
        }

        while(!stack.isEmpty()) {
            final char popped = stack.pop();
            final Character closer = OPEN_TO_CLOSE.get(popped);
            if (closer != null) {
                result.add(closer);
            } else {
                throw new IllegalArgumentException("Unexpected closer in incomplete line: " + popped);
            }
        }

        return result;
    }

    private static long getCompletedScore(List<Character> completed) {
        long score = 0;
        for (char c : completed) {
            final Integer points = COMPLETE_SCORES.get(c);
            if (points == null) {
                throw new IllegalArgumentException("Unknown completion char: " + c);
            }
            score = score * 5 + points;
        }

        return score;
    }

    public static int getSyntaxErrorScore(List<String> input, List<String> incompleteLines) {
        int sum = 0;

        for (String line : input) {
            final Character val = checkParentheses(line, incompleteLines);
            if (val != null) {
                final Integer points = CORRUPT_SCORES.get(val);
                if (points != null) {
                    sum += points;
                }
            }
        }

        return sum;
    }

    public static long getScoreForIncompleteLine(List<String> incompleteLines) {
        if (incompleteLines.isEmpty()) {
            throw new IllegalArgumentException("No incomplete lines to score");
        }
        List<Long> completedScores = new ArrayList<>();
        incompleteLines.forEach((line) -> {
            completedScores.add(getCompletedScore(completeParentheses(line)));
        });

        completedScores.sort(Long::compareTo);
        return completedScores.get(completedScores.size()/2);
    }

    public static void main( String[] args) throws IOException {
        final List<String> input = InputReader.readLines("Day10.txt");
        final List<String> incompleteLines = new ArrayList<>();

        System.out.println("Part 1: " + getSyntaxErrorScore(input, incompleteLines));
        System.out.println("Part 2: " + getScoreForIncompleteLine(incompleteLines));
    }
}
