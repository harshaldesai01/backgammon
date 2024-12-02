package service;

import model.Player;

public class MatchManager {
    private final Player player1;
    private final Player player2;
    private int player1Score = 0;
    private int player2Score = 0;
    private int matchLength;

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

    public void incrementScore(Player player) {
        if (player.equals(player1)) {
            player1Score++;
        } else if (player.equals(player2)) {
            player2Score++;
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
}