package com.github.dogunyoye;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
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

    public static long findLargestModelNumberAcceptedByMONAD(List<String> input) {

        final List<List<String>> subprograms = createSubprograms(input);

        // for (long i = 99999999999999L; i >= 11111111111111L; i--) {
        //     if (runALUProgram(input, i)) {
        //         return i;
        //     }
        // }

        return -1;
    }
    
    public static void main(String[] args) throws IOException {
        final List<String> input = Files.readAllLines(Path.of("src/main/resources/Day24.txt"));
        System.out.println("Part 1: " + findLargestModelNumberAcceptedByMONAD(input));
    }
}