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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MatchManagerTest {
    private MatchManager matchManager;
    private Player player1;
    private Player player2;

    @BeforeEach
    void setUp() {
        player1 = new Player("Alice");
        player2 = new Player("Bob");
        matchManager = new MatchManager(player1.getName(), player2.getName(), 3); // Match of 3 games
    }

    @Test
    void testGetPlayer1() {
        assertEquals(player1.getName(), matchManager.getPlayer1().getName(), "Player 1 should be Alice.");
    }

    @Test
    void testGetPlayer2() {
        assertEquals(player2.getName(), matchManager.getPlayer2().getName(), "Player 2 should be Bob.");
    }

    @Test
    void testIncrementScore() {
        matchManager.incrementScore(player1, 5);

        assertEquals(5, matchManager.getPlayer1Score(), "Player 1's score should be incremented by 5.");
        assertEquals(0, matchManager.getPlayer2Score(), "Player 2's score should remain 0.");

        matchManager.incrementScore(player2, 3);

        assertEquals(5, matchManager.getPlayer1Score(), "Player 1's score should remain 5.");
        assertEquals(3, matchManager.getPlayer2Score(), "Player 2's score should be incremented by 3.");
    }

    @Test
    void testIsMatchOver() {
        assertFalse(matchManager.isMatchOver(), "Match should not be over initially.");

        matchManager.incrementGamesPlayed();
        matchManager.incrementGamesPlayed();
        matchManager.incrementGamesPlayed();

        assertTrue(matchManager.isMatchOver(), "Match should be over after all games are played.");
    }

    @Test
    void testIncrementGamesPlayed() {
        matchManager.incrementGamesPlayed();
        matchManager.incrementGamesPlayed();

        assertEquals(3, matchManager.getCurrentGameNumber(), "Games played should be 3 after incrementing twice.");
    }

    @Test
    void testGetWinnerName() {
        matchManager.incrementScore(player1, 10);
        matchManager.incrementScore(player2, 5);

        assertEquals(player1.getName(), matchManager.getWinnerName(), "Player 1 should be the winner.");

        matchManager.incrementScore(player2, 10);

        assertEquals(player2.getName(), matchManager.getWinnerName(), "Player 2 should be the winner after scoring more points.");

        matchManager.incrementScore(player1, 5);

        assertEquals("Draw", matchManager.getWinnerName(), "The match should be a draw when scores are equal.");
    }

    @Test
    void testGetPlayer1Score() {
        matchManager.incrementScore(player1, 8);

        assertEquals(8, matchManager.getPlayer1Score(), "Player 1's score should be 8.");
    }

    @Test
    void testGetPlayer2Score() {
        matchManager.incrementScore(player2, 7);

        assertEquals(7, matchManager.getPlayer2Score(), "Player 2's score should be 7.");
    }

    @Test
    void testSetMatchOver() {
        matchManager.setMatchOver(true);

        assertTrue(matchManager.isMatchOver(), "Match should be marked as over.");
    }

    @Test
    void testGetGamesNumber() {
        matchManager.incrementGamesPlayed();

        assertEquals(2, matchManager.getCurrentGameNumber(), "Games played should be 2 after incrementing once.");
    }

    @Test
    void testGetMatchLength() {
        assertEquals(3, matchManager.getMatchLength(), "Match length should be 3 as initialized.");
    }
}
