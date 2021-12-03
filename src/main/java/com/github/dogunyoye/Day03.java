package com.github.dogunyoye;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public class Day03 {

    @FunctionalInterface
    interface ReportFilter<A, B, C, D> {
        public List<String> apply(A zeros, B ones, C idx, D report);
    }

    public static int findPowerConsumption(List<String> report) {

        final int length = report.get(0).length();
        final StringBuilder gammaString = new StringBuilder(String.join("", Collections.nCopies(length, "0")));
        final StringBuilder epsilonString = new StringBuilder(String.join("", Collections.nCopies(length, "0")));

        for (int i = 0; i < length; i++) {
            int countZeros = 0;
            int countOnes = 0;

            for (int j = 0; j < report.size(); j++) {
                if (report.get(j).charAt(i) == '1') {
                    countOnes++;
                } else {
                    countZeros++;
                }
            }

            if (countZeros < countOnes) {
                gammaString.setCharAt(i, '1');
            } else {
                epsilonString.setCharAt(i, '1');
            }
        }

        return Integer.parseInt(gammaString.toString(), 2) * Integer.parseInt(epsilonString.toString(), 2);
    }

    private static int findRating(List<String> report, ReportFilter<Integer, Integer, Integer, List<String>> filter) {

        final int length = report.get(0).length();
        boolean terminate = false;

        while (true) {
            for (int i = 0; i < length; i++) {
                int countZeros = 0;
                int countOnes = 0;

                for (int j = 0; j < report.size(); j++) {
                    if (report.get(j).charAt(i) == '1') {
                        countOnes++;
                    } else {
                        countZeros++;
                    }
                }
            
                report = filter.apply(countZeros, countOnes, i, report);

                if (report.size() == 1) {
                    terminate = true;
                    break;
                }
            }

            if (terminate) {
                break;
            }
        }

        return Integer.parseInt(report.get(0), 2);
    }

    public static int findLifeSupportRating(List<String> report1, List<String> report2) {

        final ReportFilter<Integer, Integer, Integer, List<String>> oxygenGeneratorRatingFilter = (zeroes, ones, idx, report) -> {
            if (zeroes < ones || zeroes == ones) {
                return report.stream().filter(num -> num.charAt(idx) == '1').toList();
            } else if (zeroes > ones) {
                return report.stream().filter(num -> num.charAt(idx) == '0').toList();
            }

            return null;
        };

        final ReportFilter<Integer, Integer, Integer, List<String>> c02ScrubberRatingFilter = (zeroes, ones, idx, report) -> {
            if (zeroes < ones || zeroes == ones) {
                return report.stream().filter(num -> num.charAt(idx) == '0').toList();
            } else if (zeroes > ones) {
                return report.stream().filter(num -> num.charAt(idx) == '1').toList();
            }

            return null;
        };

        return findRating(report1, oxygenGeneratorRatingFilter) * findRating(report2, c02ScrubberRatingFilter);
    }

    public static void main( String[] args ) throws IOException {
        final List<String> report = Files.readAllLines(Path.of("src/main/resources/Day03.txt"));
        final Supplier<List<String>> reportSupplier = () -> report.stream().toList();

        System.out.println("Part 1: " + findPowerConsumption(reportSupplier.get()));
        System.out.println("Part 2: " + findLifeSupportRating(reportSupplier.get(), reportSupplier.get()));
    }
}
