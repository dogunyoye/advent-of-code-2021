package com.github.dogunyoye;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class Day04 {
    
    public static class BingoCard {
        public int[][] grid;
        public boolean winner;

        public BingoCard() {
            this.grid = new int[5][5];
            this.winner = false;
        }

        public void fill(int rowIndex, int[] numbers) {
            if (numbers == null) {
                throw new IllegalArgumentException("Row numbers must not be null");
            }
            if (numbers.length != 5) {
                throw new IllegalArgumentException("Row must contain exactly 5 numbers, got " + numbers.length);
            }
            if (rowIndex < 0 || rowIndex >= 5) {
                throw new IllegalArgumentException("Row index out of bounds: " + rowIndex);
            }
            for (int i = 0; i < 5; i++) {
                this.grid[rowIndex][i] = numbers[i];
            }
        }

        public void mark(int number) {
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 5; j++) {
                    if (this.grid[i][j] == number) {
                        this.grid[i][j] = -1;
                    }
                }
            }
        }

        public boolean hasWon() {
            return this.winner;
        }

        public boolean checkWin() {
            boolean allChecked = false;
            for (int i = 0; i < 5; i++) {
                allChecked = true;

                // check rows
                for (int j = 0; j < 5; j++) {
                    if (this.grid[i][j] != -1) {
                        allChecked = false;
                        break;
                    }
                }

                // winning row
                if (allChecked) {
                    this.winner = true;
                    return true;
                }

                allChecked = true;

                // check columns
                for (int j = 0; j < 5; j++) {
                    if (this.grid[j][i] != -1) {
                        allChecked = false;
                        break;
                    }
                }

                // winning column
                if (allChecked) {
                    this.winner = true;
                    return true;
                }
            }

            return false;
        }

        public int calculateWin() {
            int sum = 0;
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 5; j++) {
                    if (this.grid[i][j] != -1) {
                        sum += this.grid[i][j];
                    }
                }
            }

            return sum;
        }
    }

    public static int playBingo(int[] numbers, List<BingoCard> bingoCards) {
        for (int num : numbers) {
            for (BingoCard card : bingoCards) {
                card.mark(num);
                if (card.checkWin()) {
                    return num * card.calculateWin();
                }
            }
        }

        return -1;
    }

    public static int lastWinner(int[] numbers, List<BingoCard> bingoCards) {
        Stream<BingoCard> cardStream;
        BingoCard lastWinner = null;
        int lastWinningNum = 0;

        for (int num : numbers) {
            cardStream = bingoCards.stream().filter((card) -> !card.hasWon());
            bingoCards = cardStream.toList();

            for (BingoCard card : bingoCards) {
                card.mark(num);
                if (card.checkWin()) {
                    lastWinningNum = num;
                    lastWinner = card;
                }
            }
        }

        if (lastWinner == null) {
            throw new IllegalStateException("No winning bingo card found");
        }
        return lastWinningNum * lastWinner.calculateWin();
    }

    static List<BingoCard> generateBingoCards(Iterator<String> iter) {
        final List<BingoCard> bingoCards = new ArrayList<>();

        // skip first blank line
        iter.next();

        BingoCard card = new BingoCard();
        int rowIndex = 0;

        while(iter.hasNext()) {
            final String line = iter.next();

            if (!line.isEmpty()) {
                final int[] lineNumbers = Stream.of(line.trim().split("\\s+"))
                    .filter(str -> !str.isEmpty())
                    .mapToInt(Integer::parseInt)
                    .toArray();

                if (lineNumbers.length != 5) {
                    throw new IllegalArgumentException("Malformed bingo row, expected 5 numbers: " + line);
                }
                if (rowIndex >= 5) {
                    throw new IllegalArgumentException("Malformed bingo card, too many rows");
                }
                card.fill(rowIndex, lineNumbers);
                rowIndex++;
                continue;
            }

            // blank separator: only close a card if one is in progress
            if (rowIndex == 0) {
                continue;
            }
            if (rowIndex != 5) {
                throw new IllegalArgumentException("Incomplete bingo card, expected 5 rows but got " + rowIndex);
            }
            bingoCards.add(card);
            // reset for new card
            card = new BingoCard();
            rowIndex = 0;
        }

        // add last card only if one is in progress
        if (rowIndex > 0) {
            if (rowIndex != 5) {
                throw new IllegalArgumentException("Incomplete bingo card, expected 5 rows but got " + rowIndex);
            }
            bingoCards.add(card);
        }
        if (bingoCards.isEmpty()) {
            throw new IllegalArgumentException("No bingo cards found");
        }
        return bingoCards;
    }

    public static void main( String[] args ) throws IOException {
        final List<String> input = InputReader.readLines("Day04.txt");
        final Supplier<Stream<String>> streamSupplier = () -> input.stream();

        final Iterator<String> iter = streamSupplier.get().iterator();
        final int[] numbers = Stream.of(iter.next().split(",")).mapToInt(Integer::parseInt).toArray();

        System.out.println("Part 1: " + playBingo(numbers, generateBingoCards(iter)));

        final Iterator<String> iter2 = streamSupplier.get().iterator();
        iter2.next();
        System.out.println("Part 2: " + lastWinner(numbers, generateBingoCards(iter2)));
    }
}
