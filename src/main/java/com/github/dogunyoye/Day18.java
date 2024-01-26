package com.github.dogunyoye;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

public class Day18 {

    private static String concatenateSnailfishNumber(String s1, String s2) {
        return "[" + s1 + "," + s2 + "]";
    }

    private static int[][] findNearestNumbers(int left, int right, String snailFish) {
        int nearestLeftNumberStartIdx = -1;
        int nearestLeftNumberEndIdx = -1;

        int nearestRightNumberStartIdx = -1;
        int nearestRightNumberEndIdx = -1;

        for (int i = left; i >= 0; i--) {
            if (Character.isDigit(snailFish.charAt(i))) {
                nearestLeftNumberStartIdx = i;
                nearestLeftNumberEndIdx = i+1;
                while (i >= 0 && Character.isDigit(snailFish.charAt(--i))) {
                    nearestLeftNumberStartIdx = i;
                }
                break;
            }
        }

        for (int i = right; i < snailFish.length(); i++) {
            if (Character.isDigit(snailFish.charAt(i))) {
                nearestRightNumberStartIdx = i;
                nearestRightNumberEndIdx = i+1;
                while (i < snailFish.length() && Character.isDigit(snailFish.charAt(++i))) {
                    nearestRightNumberEndIdx = i+1;
                }
                break;
            }
        }

        return new int[][]{
            new int[]{nearestLeftNumberStartIdx, nearestLeftNumberEndIdx},
            new int[]{nearestRightNumberStartIdx, nearestRightNumberEndIdx}};
    }

    private static String explode(String snailfishNumber) {
        int leftBracketCount = 0;
        int leftPointer = 0;
        int rightPointer = 0;

        final StringBuilder sb = new StringBuilder(snailfishNumber);

        for (int i = 0; i < snailfishNumber.length(); i++) {
            final char c = snailfishNumber.charAt(i);
            if (c == '[') {
                ++leftBracketCount;
                leftPointer = i;
            } else if (c == ']') {
                --leftBracketCount;
                rightPointer = i;

                if (leftBracketCount > 0 && leftBracketCount % 4 == 0) {
                    final int[][] indices = findNearestNumbers(leftPointer, rightPointer, snailfishNumber);
                    final String[] pairValues = snailfishNumber.substring(leftPointer+1, rightPointer).split(",");
                    final int pairLeftValue = Integer.parseInt(pairValues[0]);
                    final int pairRightValue = Integer.parseInt(pairValues[1]);

                    final int leftIdx = indices[0][0];
                    final int rightIdx = indices[1][0];

                    if (rightIdx != -1) {
                        final int newValue = Integer.parseInt(snailfishNumber.substring(indices[1][0], indices[1][1])) + pairRightValue;
                        sb.replace(indices[1][0], indices[1][1], Integer.toString(newValue));
                    }

                    if (leftIdx != -1) {
                        final int current = Integer.parseInt(snailfishNumber.substring(indices[0][0], indices[0][1]));
                        final int newValue = current + pairLeftValue;
                        sb.replace(indices[0][0], indices[0][1], Integer.toString(newValue));
                        if (current < 10 && newValue >= 10) {
                            ++leftPointer;
                            ++rightPointer;
                        }
                    }

                    sb.replace(leftPointer, rightPointer+1, "0");
                    return sb.toString();
                }
            }
        }

        return null;
    }

    private static String split(String snailfishNumber) {
        final StringBuilder sb = new StringBuilder(snailfishNumber);

        for (int i = 0; i < snailfishNumber.length()-1; i++) {
            final char c0 = snailfishNumber.charAt(i);
            final char c1 = snailfishNumber.charAt(i+1);

            if (Character.isDigit(c0) && Character.isDigit(c1)) {
                final int pairValue = Integer.parseInt("" + c0 + c1);
                final String newValue = "[" + Math.floorDiv(pairValue, 2) + "," + (int)Math.ceil((double)pairValue/2) + "]";
                sb.replace(i, i+2, newValue);
                return sb.toString();
            }
        }

        return null;
    }

    private static String calculateSnailfishNumber(List<String> data) {
        final Queue<String> numbers = new ArrayDeque<>(data.subList(1, data.size()));
        String current = data.get(0);
        String result = "";

        while (!numbers.isEmpty()) {
            current = concatenateSnailfishNumber(current, numbers.poll());
            while (true) {
                result = explode(current);
                while (result != null) {
                    current = result;
                    result = explode(result);
                }
    
                result = split(current);
                if (result != null) {
                    current = result;
                    continue;
                }

                break;
            }
        }

        return current;
    }

    private static int calculateMagnitude(String snailfishNumber) {
        int leftPointer = 0;

        String result = snailfishNumber;

        while (result.contains("[") && result.contains("]")) {
            final StringBuilder sb = new StringBuilder(result);
            for (int i = 0; i < result.length(); i++) {
                final char c = result.charAt(i);
                if (c == '[') {
                    leftPointer = i;
                } else if (c == ']') {
                    final String[] numbers = result.substring(leftPointer+1, i).split(",");
                    final int value = (Integer.parseInt(numbers[0]) * 3) + (Integer.parseInt(numbers[1]) * 2);
                    sb.replace(leftPointer, i+1, Integer.toString(value));
                    result = sb.toString();
                    break;
                }
            }
        }

        return Integer.parseInt(result);
    }

    public static int calculateMagnitude(List<String> data) {
        return calculateMagnitude(calculateSnailfishNumber(data));
    }
    
    public static void main( String[] args ) throws IOException {
        final List<String> input = Files.readAllLines(Path.of("src/main/resources/Day18.txt"));
        System.out.println("Part 1: " + calculateMagnitude(input));
    }
}
