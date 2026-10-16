package com.github.dogunyoye;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day21 {

    public enum PlayerName {
        PLAYER_ONE,
        PLAYER_TWO
    }

    public static class Player {
        public PlayerName name;
        public int position;
        public int score;

        public Player(PlayerName name, int position) {
            this.name = name;
            this.position = position;
            this.score = 0;
        }
    }

    public static class GameState {
        public int playerOnePosition;
        public int playerOneScore;
        public int playerTwoPosition;
        public int playerTwoScore;
        public PlayerName nextTurn;

        public GameState(int playerOnePosition, int playerOneScore, int playerTwoPosition, int playerTwoScore, PlayerName next) {
            this.playerOnePosition = playerOnePosition;
            this.playerOneScore = playerOneScore;
            this.playerTwoPosition = playerTwoPosition;
            this.playerTwoScore = playerTwoScore;
            this.nextTurn = next;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((nextTurn == null) ? 0 : nextTurn.hashCode());
            result = prime * result + playerOnePosition;
            result = prime * result + playerOneScore;
            result = prime * result + playerTwoPosition;
            result = prime * result + playerTwoScore;
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            GameState other = (GameState) obj;
            if (nextTurn != other.nextTurn)
                return false;
            if (playerOnePosition != other.playerOnePosition)
                return false;
            if (playerOneScore != other.playerOneScore)
                return false;
            if (playerTwoPosition != other.playerTwoPosition)
                return false;
            if (playerTwoScore != other.playerTwoScore)
                return false;
            return true;
        }
    }

    public static Player[] generatePlayers(List<String> input) {
        if (input == null || input.size() < 2) {
            throw new IllegalArgumentException("Expected at least 2 player lines, got: " + input);
        }
        final Player[] players = new Player[2];
        int idx = 0;

        PlayerName name = PlayerName.PLAYER_ONE;

        for (String line : input) {
            final String[] parts = line.trim().split("\\s+");
            if (parts.length < 5) {
                throw new IllegalArgumentException("Malformed player line: " + line);
            }
            final int position = Integer.parseInt(parts[4]);
            players[idx] = new Player(name, position);
            idx++;
            if (idx >= 2) {
                break;
            }

            name = PlayerName.PLAYER_TWO;
        }

        return players;
    }

    public static int playDiracDice(Player[] players) {
        int die = 0;
        final int[] positions = new int[]{10, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

        while(true) {
            for (Player p : players) {
                int spacesToMove = rollDie(die) + rollDie(die + 1) + rollDie(die + 2);
                p.position = positions[((p.position + spacesToMove) % 10)];
                p.score += p.position;

                die += 3;

                if (p.score >= 1000) {
                    return die * Math.min(players[0].score, players[1].score);
                }
            }
        }
    }

    private static int rollDie(int die) {
        return (die % 100) + 1;
    }

    private static long[] playQuantumDiracDice(
        Map<GameState, long[]> memo,
        int p1Score,
        int p2Score,
        int p1Pos,
        int p2Pos,
        PlayerName turn) {

        final PlayerName next = turn == PlayerName.PLAYER_ONE ? PlayerName.PLAYER_TWO : PlayerName.PLAYER_ONE;
        final GameState gameState = new GameState(p1Pos, p1Score, p2Pos, p2Score, next);

        if (p1Score >= 21) {
            return new long[]{1, 0};
        }

        if (p2Score >= 21) {
            return new long[]{0, 1};
        }

        if (memo.containsKey(gameState)) {
            return memo.get(gameState);
        }

        final int[] positions = new int[]{10, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        final long[] count = new long[2];
        long[] win = new long[2];

        // Dirac roll frequencies: 3:1, 4:3, 5:6, 6:7, 7:6, 8:3, 9:1
        final int[] sums = {3, 4, 5, 6, 7, 8, 9};
        final int[] freqs = {1, 3, 6, 7, 6, 3, 1};

        for (int s = 0; s < sums.length; s++) {
            final int spacesToMove = sums[s];
            final int freq = freqs[s];

            if (turn == PlayerName.PLAYER_ONE) {
                final int newPos = positions[((p1Pos + spacesToMove) % 10)];
                final int newScore = p1Score + newPos;
                win = playQuantumDiracDice(memo, newScore, p2Score, newPos, p2Pos, PlayerName.PLAYER_TWO);
            } else {
                final int newPos = positions[((p2Pos + spacesToMove) % 10)];
                final int newScore = p2Score + newPos;
                win = playQuantumDiracDice(memo, p1Score, newScore, p1Pos, newPos, PlayerName.PLAYER_ONE);
            }

            count[0] += win[0] * freq;
            count[1] += win[1] * freq;
        }

        memo.put(gameState, count);
        return count;
    }

    public static long findWinsInMostUniverses(Player[] players) {
        final Map<GameState, long[]> memo = new HashMap<>();
        final long[] result = playQuantumDiracDice(memo, 0, 0, players[0].position, players[1].position, PlayerName.PLAYER_ONE);
        return Math.max(result[0], result[1]);
    }
    
    public static void main( String[] args ) throws IOException {
        final List<String> input = InputReader.readLines("Day21.txt");

        Player[] players = generatePlayers(input);
        System.out.println("Part 1: " + playDiracDice(players));

        players = generatePlayers(input);
        System.out.println("Part 2: " + findWinsInMostUniverses(players));
    }
}
