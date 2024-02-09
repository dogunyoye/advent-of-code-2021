package com.github.dogunyoye;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day24 {

    private static class ALU {
        private int w;
        private int x;
        private int y;
        private int z;

        private ALU(int w, int x, int y, int z) {
            this.w = w;
            this.x = x;
            this.y = y;
            this.z = z;
        }

        private int getValue(String variable) {
            if (variable.matches("[wxyz]")) {
                return switch(variable) {
                    case "w" -> w;
                    case "x" -> x;
                    case "y" -> y;
                    case "z" -> z;
                    default -> throw new RuntimeException("Unknown reg: " + variable);
                };
            }
            
            return Integer.parseInt(variable);
        }

        private void inp(char reg, int inp) {
            switch(reg) {
                case 'w' -> w = inp;
                case 'x' -> x = inp;
                case 'y' -> y = inp;
                case 'z' -> z = inp;
                default -> throw new RuntimeException("Unknown reg: " + reg);
            }
        }

        private void add(char reg, String variable) {
            final int inp = getValue(variable);
            switch(reg) {
                case 'w' -> w += inp;
                case 'x' -> x += inp;
                case 'y' -> y += inp;
                case 'z' -> z += inp;
                default -> throw new RuntimeException("Unknown reg: " + reg);
            }
        }

        private void mul(char reg, String variable) {
            final int inp = getValue(variable);
            switch(reg) {
                case 'w' -> w *= inp;
                case 'x' -> x *= inp;
                case 'y' -> y *= inp;
                case 'z' -> z *= inp;
                default -> throw new RuntimeException("Unknown reg: " + reg);
            }
        }

        private void div(char reg, String variable) {
            final int inp = getValue(variable);
            if (inp == 0) {
                throw new ArithmeticException("Cannot divide by 0");
            }

            switch(reg) {
                case 'w' -> w /= inp;
                case 'x' -> x /= inp;
                case 'y' -> y /= inp;
                case 'z' -> z /= inp;
                default -> throw new RuntimeException("Unknown reg: " + reg);
            }
        }

        private void mod(char reg, String variable) {
            final int inp = getValue(variable);
            if (inp <= 0) {
                throw new ArithmeticException("Cannot mod by value: " + inp);
            }

            switch(reg) {
                case 'w' -> {
                    if (w < 0) {
                        throw new ArithmeticException("Cannot mod by value at w: " + w);
                    }
                    w %= inp;
                }

                case 'x' -> {
                    if (x < 0) {
                        throw new ArithmeticException("Cannot mod by value at x: " + w);
                    }
                    x %= inp;
                }

                case 'y' -> {
                    if (y < 0) {
                        throw new ArithmeticException("Cannot mod by value at y: " + w);
                    }
                    y %= inp;
                }

                case 'z' -> {
                    if (z < 0) {
                        throw new ArithmeticException("Cannot mod by value at z: " + w);
                    }
                    z %= inp;
                }

                default -> throw new RuntimeException("Unknown reg: " + reg);
            }
        }

        private void eql(char reg, String variable) {
            final int inp = getValue(variable);
            switch(reg) {
                case 'w' -> w = w == inp ? 1 : 0;
                case 'x' -> x = x == inp ? 1 : 0;
                case 'y' -> y = y == inp ? 1 : 0;
                case 'z' -> z = z == inp ? 1 : 0;
                default -> throw new RuntimeException("Unknown reg: " + reg);
            }
        }

        @Override
        public String toString() {
            return "ALU [w=" + w + ", x=" + x + ", y=" + y + ", z=" + z + "]";
        }
    }

    private static List<List<String>> createSubprograms(List<String> input) {
        final List<List<String>> subPrograms = new ArrayList<>();
        for (int i = 0; i < input.size(); i+=18) {
            subPrograms.add(input.subList(i, i+18));
        }
        return subPrograms;
    }

    // For debugging and to test candidate numbers
    @SuppressWarnings("unused")
    private static boolean runALUProgram(List<String> input, long modelNumber) {
        final String modelNumberString = Long.toString(modelNumber);
        if (modelNumberString.contains("0")) {
            return false;
        }

        final ALU alu = new ALU(0, 0, 0, 0);
        int idx = 0;

        for (final String instruction : input) {
            final String[] parts = instruction.split(" ");
            final char reg = parts[1].charAt(0);

            final String variable;
            if (parts.length == 2) {
                variable = Character.toString(modelNumberString.charAt(idx++));
            } else {
                variable = parts[2];
            }

            switch(parts[0]) {
                case "inp" -> alu.inp(reg, Integer.parseInt(variable));
                case "add" -> alu.add(reg, variable);
                case "mul" -> alu.mul(reg, variable);
                case "div" -> alu.div(reg, variable);
                case "mod" -> alu.mod(reg, variable);
                case "eql" -> alu.eql(reg, variable);
                default -> throw new RuntimeException("Unknown instruction: " + instruction);
            }
        }

        System.out.println(alu + " " + modelNumber);
        return alu.z == 0;
    }

    private static int[] divAndIncrement(List<String> subprogram) {
        final int div = Integer.parseInt(subprogram.get(4).split(" ")[2]); // 1 or 26
        final int increment0 = Integer.parseInt(subprogram.get(5).split(" ")[2]);
        final int increment1 = Integer.parseInt(subprogram.get(15).split(" ")[2]);
        return new int[]{div, increment0, increment1};
    }

    // mostly inspired from: https://www.youtube.com/watch?v=Eswmo7Y7C4U
    public static List<Long> findModelNumbersAcceptedByMONAD(List<String> input) {
        final List<List<String>> subprograms = createSubprograms(input);
        final List<int[]> divAndIncrements = subprograms.stream().map(Day24::divAndIncrement).toList();
        final List<Long> accepted = new ArrayList<>();

        for (int i = 9999999; i >= 1111111; i--) {
            final int[] result = new int[14];
            final String modelNumberString = Integer.toString(i);
            if (modelNumberString.contains("0")) {
                continue;
            }

            boolean stopProcessing = false;
            int z = 0;
            int idx = 0;

            for (int j = 0; j < divAndIncrements.size(); j++) {
                final int[] divAndIncrement = divAndIncrements.get(j);

                switch(divAndIncrement[0]) {
                    case 1:
                        final int digit = Integer.parseInt(Character.toString(modelNumberString.charAt(idx++)));
                        z = z * 26 + digit + divAndIncrement[2];
                        result[j] = digit;
                        break;
                    
                    case 26:
                        result[j] = (z % 26) + divAndIncrement[1];
                        z /= 26;
                        if (result[j] < 1 || result[j] > 9) {
                            stopProcessing = true;
                        }
                        break;
                    
                    default:
                        throw new RuntimeException("Unexpected div number: " + divAndIncrement[0]);
                }

                if (stopProcessing) {
                    break;
                }
            }

            if (!stopProcessing) {
                accepted.add(Long.parseLong(Arrays.stream(result).boxed().map((r) -> Integer.toString(r)).reduce("", String::concat)));
            }
        }

        return accepted;
    }
    
    public static void main(String[] args) throws IOException {
        final List<String> input = Files.readAllLines(Path.of("src/main/resources/Day24.txt"));
        final List<Long> acceptedNumbers = findModelNumbersAcceptedByMONAD(input);
        System.out.println("Part 1: " + acceptedNumbers.get(0));
        System.out.println("Part 2: " + acceptedNumbers.get(acceptedNumbers.size() - 1));
    }
}
