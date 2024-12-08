/**
 * This file is part of the Backgammon game project developed by the Dice Bros - Group 5 team.
 *
 * Team Information:
 * Team Name: Dice Bros - Group 5
 * Student Names:
 *   - Harshal Desai
 *   - Alparslan Balci
 *   - Manish Tawade
 * GitHub IDs:
 *   - harshaldesai01
 *   - Apistomeister
 *   - Manish9881
 */

package service;

import model.Player;
import util.CommonConstants;

/**
 * Manages the state of a Backgammon match, including scores, match length, and player information.
 */
public class MatchManager {
    private final Player player1;
    private final Player player2;
    private int player1Score = 0;
    private int player2Score = 0;
    private boolean matchOver = false;
    private final int matchLength;
    private int gamesPlayed = 0;

    /**
     * Constructs a MatchManager with the specified player names and match length.
     *
     * @param name1       the name of Player 1.
     * @param name2       the name of Player 2.
     * @param matchLength the length of the match (number of games).
     */
    public MatchManager(String name1, String name2, int matchLength) {
        this.player1 = new Player(name1);
        this.player2 = new Player(name2);
        this.matchLength = matchLength;
    }

    /**
     * Retrieves Player 1.
     *
     * @return Player 1.
     */
    public Player getPlayer1() {
        return player1;
    }

    /**
     * Retrieves Player 2.
     *
     * @return Player 2.
     */
    public Player getPlayer2() {
        return player2;
    }

    /**
     * Increments the score of the specified player by a given number of points.
     *
     * @param player the player whose score is to be incremented.
     * @param points the number of points to add.
     */
    public void incrementScore(Player player, int points) {
        if(null == player) {
            return;
        }
        if (player.equals(player1)) {
            player1Score += points;
        } else if (player.equals(player2)) {
            player2Score += points;
        }
    }

    /**
     * Checks if the match is over based on the matchOver condition or matchLength number of games are played.
     *
     * @return true if the match is over, false otherwise.
     */
    public boolean isMatchOver() {
        return matchOver || gamesPlayed >= matchLength;
    }

    /**
     * Increments the count of games played in the match.
     */
    public void incrementGamesPlayed() {
        gamesPlayed++;
    }

    /**
     * Retrieves the name of the player who is currently winning the match.
     *
     * @return the name of the leading player, or "Draw" if scores are tied.
     */
    public String getWinnerName() {
        if (player1Score > player2Score) {
            return player1.getName();
        } else if (player2Score > player1Score) {
            return player2.getName();
        } else {
            return CommonConstants.DRAW_CONDITION;
        }
    }

    /**
     * Retrieves the score of Player 1.
     *
     * @return Player 1's score.
     */
    public int getPlayer1Score() {
        return player1Score;
    }

    /**
     * Retrieves the score of Player 2.
     *
     * @return Player 2's score.
     */
    public int getPlayer2Score() {
        return player2Score;
    }

    /**
     * Marks the match as over.
     *
     * @param matchOver true if the match is over, false otherwise.
     */
    public void setMatchOver(boolean matchOver) {
        this.matchOver = matchOver;
    }

    /**
     * Retrieves the current game number.
     *
     * @return the current game number.
     */
    public int getCurrentGameNumber() {
        return gamesPlayed+1;
    }

    /**
     * Retrieves the length of the match.
     *
     * @return the total number of games in the match.
     */
    public int getMatchLength() {
        return matchLength;
    }
}