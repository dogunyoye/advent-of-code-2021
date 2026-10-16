package com.github.dogunyoye;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Loads puzzle inputs from the classpath first (works from a jar or any
 * working directory), falling back to {@code src/main/resources/} for
 * local runs where resources are untracked.
 */
public final class InputReader {

    private InputReader() {
    }

    public static List<String> readLines(String fileName) throws IOException {
        final InputStream in = InputReader.class.getClassLoader().getResourceAsStream(fileName);
        if (in != null) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
                return reader.lines().collect(Collectors.toList());
            }
        }
        final Path fallback = Path.of("src/main/resources", fileName);
        if (Files.exists(fallback)) {
            return Files.readAllLines(fallback, StandardCharsets.UTF_8);
        }
        throw new IOException("Input not found on classpath or at " + fallback + ". "
            + "Place your personal puzzle input at src/main/resources/" + fileName);
    }
}
