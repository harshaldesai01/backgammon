package service;

import model.Player;

public class MatchManager {
    private final Player player1;
    private final Player player2;
    private int player1Score = 0;
    private int player2Score = 0;
    private int matchLength;
    private boolean doublingOffered = false;
    private Player playerToRespond;
    private boolean gameOver = false;

    public MatchManager(String name1, String name2, int matchLength) {
        this.player1 = new Player(name1);
        this.player2 = new Player(name2);
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

    public boolean isMatchOver() {
        return player1Score == matchLength || player2Score == matchLength;
    }

    public String getWinnerName() {
        return player1Score > player2Score ? player1.getName() : player2.getName();
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

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

}