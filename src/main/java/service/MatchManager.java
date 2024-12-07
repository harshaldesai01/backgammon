package service;

import model.Player;

public class MatchManager {
    private final Player player1;
    private final Player player2;
    private int player1Score = 0;
    private int player2Score = 0;
    private boolean matchOver = false;

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    private boolean gameOver = false;

    public int getMatchLength() {
        return matchLength;
    }

    private final int matchLength;
    private int gamesPlayed = 0;

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
        return matchOver || gamesPlayed >= matchLength;
    }

    public void incrementGamesPlayed() {
        gamesPlayed++;
    }

    public void resetMatch() {
        player1Score = 0;
        player2Score = 0;
        gamesPlayed = 0;
        matchOver = false;
    }

    public String getWinnerName() {
        if (player1Score > player2Score) {
            return player1.getName();
        } else if (player2Score > player1Score) {
            return player2.getName();
        } else {
            return "Draw";
        }
    }

    public int getPlayer1Score() {
        return player1Score;
    }

    public int getPlayer2Score() {
        return player2Score;
    }

    public void setMatchOver(boolean matchOver) {
        this.matchOver = matchOver;
    }
}