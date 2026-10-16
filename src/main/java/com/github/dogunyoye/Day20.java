package com.github.dogunyoye;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.dogunyoye.Day05.Coordinate;

public class Day20 {

    public enum Direction {
        NORTH_WEST,
        NORTH,
        NORTH_EAST,
        WEST,
        EAST,
        SOUTH_WEST,
        SOUTH,
        SOUTH_EAST
    }

    private static int binaryStringToNumber(String binary) {
        return Integer.parseInt(binary, 2);
    }

    private static String generateBinaryString(Map<Coordinate, Character> map, int i, int j, int min, int max, char boundState) {
        final char[] pixels = new char[9];
        pixels[4] = map.get(new Coordinate(i, j));

        for (Direction d : Direction.values()) {
            int x = 0;
            int y = 0;

            switch(d) {
                case NORTH:
                    y = i - 1;
                    x = j;

                    if (y < min) {
                        pixels[1] = boundState;
                        break;
                    }

                    pixels[1] = map.get(new Coordinate(y, x));
                    break;
                
                case NORTH_EAST:
                    y = i - 1;
                    x = j + 1;

                    if (y < min || x > max) {
                        pixels[2] = boundState;
                        break;
                    }

                    pixels[2] = map.get(new Coordinate(y, x));
                    break;

                case EAST:
                    y = i;
                    x = j + 1;

                    if (x > max) {
                        pixels[5] = boundState;
                        break;
                    }

                    pixels[5] = map.get(new Coordinate(y, x));
                    break;
                
                case SOUTH_EAST:
                    y = i + 1;
                    x = j + 1;

                    if (y > max || x > max) {
                        pixels[8] = boundState;
                        break;
                    }

                    pixels[8] = map.get(new Coordinate(y, x));
                    break;

                case SOUTH:
                    y = i + 1;
                    x = j;

                    if (y > max) {
                        pixels[7] = boundState;
                        break;
                    }

                    pixels[7] = map.get(new Coordinate(y, x));
                    break;
                
                case SOUTH_WEST:
                    y = i + 1;
                    x = j - 1;

                    if (y > max || x < min) {
                        pixels[6] = boundState;
                        break;
                    }

                    pixels[6] = map.get(new Coordinate(y, x));
                    break;

                case WEST:
                    y = i;
                    x = j - 1;

                    if (x < min) {
                        pixels[3] = boundState;
                        break;
                    }

                    pixels[3] = map.get(new Coordinate(y, x));
                    break;

                case NORTH_WEST:
                    y = i - 1;
                    x = j - 1;

                    if (y < min || x < min) {
                        pixels[0] = boundState;
                        break;
                    }

                    pixels[0] = map.get(new Coordinate(y, x));
                    break;
            }
        }

        return new String(pixels).replaceAll("#", "1").replaceAll("\\.", "0");
    }

    public static char[] generateImageAlgorithm(List<String> input) {
        if (input == null || input.isEmpty()) {
            throw new IllegalArgumentException("Input must not be empty");
        }
        if (input.get(0).length() != 512) {
            throw new IllegalArgumentException("Image algorithm must be 512 characters long");
        }
        final char[] imageAlgorithm = new char[512];
        int idx = 0;

        for (char c : input.get(0).toCharArray()) {
            imageAlgorithm[idx] = c;
            idx++;
        }

        return imageAlgorithm;
    }

    public static Map<Coordinate, Character> generateInitialInputImage(List<String> input, int buffer) {
        if (input == null || input.isEmpty()) {
            throw new IllegalArgumentException("Input must not be empty");
        }
        final Map<Coordinate, Character> initial = new HashMap<>();

        // create a +/-buffer border
        for (int i = -buffer;  i < input.size() + buffer; i++) {
            for (int j = -buffer; j < input.size() + buffer; j++) {
                initial.put(new Coordinate(i, j), '.');
            }
        }

        int x = 0;
        int y = 0;

        for (int i = 2; i < input.size(); i++) {
            for (Character c : input.get(i).toCharArray()) {
                initial.put(new Coordinate(y, x), c);
                x++;
            }
            x = 0;
            y++;
        }

        return initial;
    }

    public static long findNumberOfLitPixels(
        char[] imageAlgorithm,
        Map<Coordinate, Character> image,
        int buffer,
        int idx,
        int steps) {

        if (imageAlgorithm == null || imageAlgorithm.length != 512) {
            throw new IllegalArgumentException("Image algorithm must be 512 characters long");
        }
        if (image == null || image.isEmpty()) {
            throw new IllegalArgumentException("Image must not be empty");
        }
        char boundState = '.';

        while (steps != 0) {
            final Map<Coordinate, Character> changeMap = new HashMap<>();

            for (Map.Entry<Coordinate, Character> entry : image.entrySet()) {
                final Coordinate c = entry.getKey();
                final String binary = generateBinaryString(image, c.x, c.y, -buffer, buffer + idx, boundState);
                final char output = imageAlgorithm[binaryStringToNumber(binary)];
                changeMap.put(c, output);
            }

            changeMap.forEach((k,v) -> {
                image.put(k, v);
            });

            // infinite boundary flips between on/off
            // track bound char across iterations, not steps parity
            boundState = boundState == '.' ? imageAlgorithm[0] : imageAlgorithm[511];
            steps--;
        }

        return image.values().stream().filter((c) -> c.equals('#')).count();
    }
    
    public static void main( String[] args ) throws IOException {
        final List<String> input = InputReader.readLines("Day20.txt");
        final char[] imageAlgorithm = generateImageAlgorithm(input);
        final int idx = input.size() - 1;

        int buffer = 5;
        Map<Coordinate, Character> initialImage = generateInitialInputImage(input, buffer);
        System.out.println("Part 1: " + findNumberOfLitPixels(imageAlgorithm, initialImage, buffer, idx, 2));

        // takes numerous seconds, could be optimised
        buffer *= 25;
        initialImage = generateInitialInputImage(input, buffer);
        System.out.println("Part 2: " + findNumberOfLitPixels(imageAlgorithm, initialImage, buffer, idx, 50));
    }
}
