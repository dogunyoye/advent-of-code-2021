package com.github.dogunyoye;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.github.dogunyoye.Day05.Coordinate;

public class Day13 {

    public enum Fold {
        UP,
        LEFT
    }

    static class FoldInstruction {
        Fold instruction;
        int line;

        public FoldInstruction(Fold instruction, int line) {
            this.instruction = instruction;
            this.line = line;
        }

        @Override
        public String toString() {
            return "Fold:[" + instruction.name() + " line:" + line + "]";
        }
    }

    public static String[][] generatePaper(List<String> dots, AtomicInteger xVal, AtomicInteger yVal) {
        final List<Coordinate> dotCoordinates = new ArrayList<>();
        int maxX = 0;
        int maxY = 0;

        for (String d : dots) {
            final String[] split = d.split(",");
            final int x = Integer.parseInt(split[0]);
            final int y = Integer.parseInt(split[1]);
            maxX = Math.max(maxX, x);
            maxY = Math.max(maxY, y);

            dotCoordinates.add(new Coordinate(x, y));
        }

        maxX++;
        maxY++;

        final String[][] paper = new String[maxY][maxX];
        for (String[] e : paper) {
            Arrays.fill(e, ".");
        }

        dotCoordinates.forEach((c) -> {
            paper[c.y][c.x] = "#";
        });

        xVal.set(maxX);
        yVal.set(maxY);

        return paper;
    }

    public static List<FoldInstruction> generateInstructions(List<String> instructions) {
        final List<FoldInstruction> fInstructions = new ArrayList<>();
        instructions.forEach((ins) -> {
            final String[] split = ins.split(" ")[2].split("=");
            if (split.length != 2) {
                throw new IllegalArgumentException("Invalid fold instruction: " + ins);
            }
            final int line = Integer.parseInt(split[1]);
            if (split[0].equals("y")) {
                fInstructions.add(new FoldInstruction(Fold.UP, line));
            } else if (split[0].equals("x")) {
                fInstructions.add(new FoldInstruction(Fold.LEFT, line));
            } else {
                throw new IllegalArgumentException("Invalid fold axis, only x/y allowed: " + ins);
            }
        });

        return fInstructions;
    }

    public static int findVisibleDots(int start, int instructionLimit, String[][] paper, List<FoldInstruction> instructions, int yBound, int xBound) {
        for (int i = start; i < instructionLimit; i++) {
            final FoldInstruction ins = instructions.get(i);
            switch(ins.instruction) {
            
            case UP:
                for (int yy = ins.line + 1; yy < yBound; yy++) {
                    for (int xx = 0; xx < xBound; xx++) {
                        final int newY = (2 * ins.line) - yy;
                        if (newY < 0) {
                            throw new IllegalArgumentException("Fold reflection out of bounds: newY=" + newY);
                        }
                        if (paper[yy][xx].equals("#")) {
                            paper[newY][xx] = "#";
                            paper[yy][xx] = ".";
                        }
                    }
                }
                break;
            
            case LEFT:
                for (int yy = 0; yy < yBound; yy++) {
                    for (int xx = ins.line + 1; xx < xBound; xx++) {
                        final int newX = (2 * ins.line) - xx;
                        if (newX < 0) {
                            throw new IllegalArgumentException("Fold reflection out of bounds: newX=" + newX);
                        }
                        if (paper[yy][xx].equals("#")) {
                            paper[yy][newX] = "#";
                            paper[yy][xx] = ".";
                        }
                    }
                }
                break;
            }
        }

        int counter = 0;
        for (String[] e : paper) {
            for (String v : e) {
                if (v.equals("#")) {
                    counter++;
                }
            }
        }

        return counter;
    }

    public static void visualisePaper(String[][] paper, int len) {
        for (String[] line : paper) {
            if (Arrays.asList(line).contains("#")) {
                final String[] a = Arrays.copyOf(line, len);
                System.out.println(Arrays.toString(a));
            }
        }
    }

    public static void main ( String[] args ) throws IOException {
        final List<String> input = InputReader.readLines("Day13.txt");
        final List<String> dots = input.stream().filter(str -> str.contains(",")).toList();
        final List<String> instructions = input.stream().filter(str -> str.contains("fold along")).toList();

        final AtomicInteger xLimit = new AtomicInteger();
        final AtomicInteger yLimit = new AtomicInteger();
        final String[][] paper = generatePaper(dots, xLimit, yLimit);
        final List<FoldInstruction> fInstructions = generateInstructions(instructions);

        System.out.println("Part 1: " + findVisibleDots(0, 1, paper, fInstructions, yLimit.get(), xLimit.get()));
        System.out.println("Part 2:"); 
        
        findVisibleDots(1, fInstructions.size(), paper, fInstructions, yLimit.get(), xLimit.get());
        visualisePaper(paper, 45);
    }
}
