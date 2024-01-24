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

    private static int[] findNearestNumbers(int left, int right, String snailFish) {
        int nearestLeftNumber = -1;
        int nearestRightNumber = -1;

        for (int i = left; i >= 0; i--) {
            if (Character.isDigit(snailFish.charAt(i))) {
                nearestLeftNumber = i;
                break;
            }
        }

        for (int i = right; i < snailFish.length(); i++) {
            if (Character.isDigit(snailFish.charAt(i))) {
                nearestRightNumber = i;
                break;
            }
        }

        return new int[]{nearestLeftNumber, nearestRightNumber};
    }

    private static String calculateSnailfishNumber(List<String> data) {
        final Queue<String> numbers = new ArrayDeque<>(data.subList(1, data.size()));
        String result = data.get(0);

        while (!numbers.isEmpty()) {
            result = concatenateSnailfishNumber(result, numbers.poll());
        }

        return result;
    }
    
    public static void main( String[] args ) throws IOException {
        final List<String> input = Files.readAllLines(Path.of("src/main/resources/Day18.txt"));
        System.out.println(calculateSnailfishNumber(input));
    }
}
