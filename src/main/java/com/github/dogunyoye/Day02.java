package com.github.dogunyoye;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class Day02 {

    public enum Command {
        FORWARD,
        DOWN,
        UP;
     }

    public static class Submarine {
        public int horizontalPos;
        public int depth;
        public int aim;
    }

    public static int moveSub(Stream<String> commands, Submarine sub) {
        commands.forEach((command) -> {
            final String[] commandVals = command.split(" ");
            final Command c = Command.valueOf(commandVals[0].toUpperCase());
            final int units = Integer.parseInt(commandVals[1]);

            switch (c) {
                case FORWARD -> sub.horizontalPos += units;
                case DOWN -> sub.depth += units;
                case UP -> sub.depth -= units;
            }
        });

        return sub.depth * sub.horizontalPos;
    }

    public static int moveSubWithAim(Stream<String> commands, Submarine sub) {
        commands.forEach((command) -> {
            final String[] commandVals = command.split(" ");
            final Command c = Command.valueOf(commandVals[0].toUpperCase());
            final int units = Integer.parseInt(commandVals[1]);

            switch (c) {
                case FORWARD -> {
                    sub.horizontalPos += units;
                    sub.depth += sub.aim * units;
                }
                case DOWN -> sub.aim += units;
                case UP -> sub.aim -= units;
            }
        });

        return sub.depth * sub.horizontalPos;
    }
    
    public static void main( String[] args ) throws IOException {
        final List<String> commands = Files.readAllLines(Path.of("src/main/resources/Day02.txt"));
        final Supplier<Stream<String>> streamSupplier = () -> commands.stream();

        System.out.println("Part 1: " + moveSub(streamSupplier.get(), new Submarine()));
        System.out.println("Part 2: " + moveSubWithAim(streamSupplier.get(), new Submarine()));
    }
}
