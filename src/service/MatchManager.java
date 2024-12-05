package service;

import model.Player;

public class MatchManager {
    private final Player player1;
    private final Player player2;
    private int player1Score = 0;
    private int player2Score = 0;
    private final int matchLength; // Total number of matches in the game
    private int matchesPlayed = 0; // Count of matches completed
    private boolean matchOver = false; // Current match is over
    private boolean gameOver = false;  // Overall game is over
    private boolean doublingOffered = false;
    private Player playerToRespond;

    public MatchManager(String name1, String name2, int matchLength) {
        Player.initializePlayers(name1, name2);
        this.player1 = Player.PLAYER1;
        this.player2 = Player.PLAYER2;
        this.matchLength = matchLength;
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public void incrementScore(Player player, int points) {
        if (player.equals(player1)) {
            player1Score += points;
        } else if (player.equals(player2)) {
            player2Score += points;
        }
    }

    public void completeMatch() {
        matchesPlayed++;
        matchOver = true;

        if (matchesPlayed >= matchLength) {
            gameOver = true; // Entire game is complete
        }
    }

    public void resetMatch() {
        matchOver = false;
    }

    public boolean isMatchOver() {
        return matchOver;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public String getWinnerName() {
        return player1Score > player2Score ? player1.getName() : player2.getName();
    }

    public boolean isDoublingOffered() {
        return doublingOffered;
    }

    public void setDoublingOffered(boolean doublingOffered) {
        this.doublingOffered = doublingOffered;
    }

    public Player getPlayerToRespond() {
        return playerToRespond;
    }

    public void setPlayerToRespond(Player playerToRespond) {
        this.playerToRespond = playerToRespond;
    }

    public int getPlayer1Score() {
        return player1Score;
    }

    public int getPlayer2Score() {
        return player2Score;
    }

    public int getMatchLength() {
        return matchLength;
    }

    public int getMatchesPlayed() {
        return matchesPlayed;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public void setMatchOver(boolean matchOver) {
        this.matchOver = matchOver;
    }
}