# advent-of-code-2021
Advent of Code 2021 solutions!

All questions can be accessed here: https://adventofcode.com/2021

## Prerequisites

- Java 17+ (tested on Java 21)
- Maven 3.8+

## Run a solution

Place your personal puzzle input at `src/main/resources/DayXX.txt`
(these files are intentionally untracked — see `.gitignore`),
then run:

```sh
mvn exec:java -Dexec.mainClass="com.github.dogunyoye.DayXX"
```

where XX denotes the chosen day - Day01 for the 1st day and so on.
Solutions also fall back to loading `DayXX.txt` from the classpath,
so they work from a packaged jar or a different working directory.

## Run tests

```sh
mvn test
```

Tests use example inputs under `src/test/resources/`.
