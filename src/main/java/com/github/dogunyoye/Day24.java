package com.github.dogunyoye;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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

        private void inp(char reg, int inp) {
            switch(reg) {
                case 'w' -> w = inp;
                case 'x' -> x = inp;
                case 'y' -> y = inp;
                case 'z' -> z = inp;
                default -> throw new RuntimeException("Unknown reg: " + reg);
            }
        }

        private void add(char reg, int inp) {
            switch(reg) {
                case 'w' -> w += inp;
                case 'x' -> x += inp;
                case 'y' -> y += inp;
                case 'z' -> z += inp;
                default -> throw new RuntimeException("Unknown reg: " + reg);
            }
        }

        private void mul(char reg, int inp) {
            switch(reg) {
                case 'w' -> w *= inp;
                case 'x' -> x *= inp;
                case 'y' -> y *= inp;
                case 'z' -> z *= inp;
                default -> throw new RuntimeException("Unknown reg: " + reg);
            }
        }

        private void div(char reg, int inp) {
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

        private void mod(char reg, int inp) {
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
            int inp;
            if (variable.matches("[wxyz]")) {
                switch(variable) {
                    case "w" -> inp = w;
                    case "x" -> inp = x;
                    case "y" -> inp = y;
                    case "z" -> inp = z;
                    default -> throw new RuntimeException("Unknown reg: " + variable);
                }
            } else {
                inp = Integer.parseInt(variable);
            }

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

            switch(parts[0]) {
                case "inp" -> alu.inp(reg, Integer.parseInt("" + modelNumberString.charAt(idx++)));
                case "add" -> alu.add(reg, Integer.parseInt(parts[2]));
                case "mul" -> alu.mul(reg, Integer.parseInt(parts[2]));
                case "div" -> alu.div(reg, Integer.parseInt(parts[2]));
                case "mod" -> alu.mod(reg, Integer.parseInt(parts[2]));
                case "eql" -> alu.eql(reg, parts[2]);
                default -> throw new RuntimeException("Unknown instruction: " + instruction);
            }
        }

        //System.out.println(alu);
        return alu.z == 0;
    }

    public static long findLargestModelNumberAcceptedByMONAD(List<String> input) {

        for (long i = 99999999999999L; i >= 11111111111111L; i--) {
            if (runALUProgram(input, i)) {
                return i;
            }
        }

        return -1;
    }
    
    public static void main(String[] args) throws IOException {
        final List<String> input = Files.readAllLines(Path.of("src/main/resources/Day24.txt"));
        System.out.println("Part 1: " + findLargestModelNumberAcceptedByMONAD(input));
    }
}
