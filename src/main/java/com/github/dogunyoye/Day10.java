package com.github.dogunyoye;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Day10 {

    private static String checkParentheses(String line, List<String> incompleteLines) {
        final String[] split = line.split("");
        final Stack<String> stack = new Stack<String>();

        for (String p : split) {
            if (!p.equals(")") && !p.equals("]") && !p.equals("}") && !p.equals(">")) {
                // an open parentheses
                stack.push(p);
                continue;
            }

            final String popped = stack.pop();
            switch(p) {
                case ")":
                    if (!popped.equals("(")) {
                        return p;
                    }
                    break;
                case "]":
                    if (!popped.equals("[")) {
                        return p;
                    }
                    break;
                case "}":
                    if (!popped.equals("{")) {
                        return p;
                    }
                    break;
                case ">":
                    if (!popped.equals("<")) {
                        return p;
                    }
                    break;
            }
        }

        if (!stack.isEmpty()) {
            String s = "";
            for (String p : stack) {
                s += p;
            }

            incompleteLines.add(s);
        }

        return "";
    }

    private static List<String> completeParentheses(String line) {
        final List<String> result = new ArrayList<>();
        final Stack<String> stack = new Stack<String>();
        final String[] split = line.split("");

        for (String p : split) {
            stack.add(p);
        }

        while(!stack.isEmpty()) {
            final String popped = stack.pop();
            switch(popped) {
                case "(":
                    result.add(")");
                    break;
                case "[":
                    result.add("]");
                    break;
                case "{":
                    result.add("}");
                    break;
                case "<":
                    result.add(">");
                    break;
            }
        }

        return result;
    }

    private static long getCompletedScore(List<String> completed) {
        long score = 0;
        for (String c : completed) {
            score *= 5;
            switch(c) {
                case ")":
                    score += 1;
                    break;
                case "]":
                    score += 2;
                    break;
                case "}":
                    score += 3;
                    break;
                case ">":
                    score += 4;
                    break;
            }
        }

        return score;
    }

    public static int getSyntaxErrorScore(List<String> input, List<String> incompleteLines) {
        int sum = 0;

        for (String line : input) {
            final String val = checkParentheses(line, incompleteLines);
            switch(val) {
                case ")":
                    sum += 3;
                    break;
                case "]":
                    sum += 57;
                    break;
                case "}":
                    sum += 1197;
                    break;
                case ">":
                    sum += 25137;
                    break;
                default:
                    // do nothing, incomplete or valid
            }
        }

        return sum;
    }

    public static long getScoreForIncompleteLine(List<String> incompleteLines) {
        List<Long> completedScores = new ArrayList<>();
        incompleteLines.forEach((line) -> {
            completedScores.add(getCompletedScore(completeParentheses(line)));
        });

        completedScores.sort(Long::compareTo);
        return completedScores.get(completedScores.size()/2);
    }

    public static void main( String[] args) throws IOException {
        final List<String> input = Files.readAllLines(Path.of("src/main/resources/Day10.txt"));
        final List<String> incompleteLines = new ArrayList<>();

        System.out.println("Part 1: " + getSyntaxErrorScore(input, incompleteLines));
        System.out.println("Part 2: " + getScoreForIncompleteLine(incompleteLines));
    }
}
