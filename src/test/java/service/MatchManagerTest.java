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
        // Create a match manager with a match length of 3 points
        matchManager = new MatchManager("Alice", "Bob", 3);
        player1 = matchManager.getPlayer1();
        player2 = matchManager.getPlayer2();
    }

    @Test
    void testGetPlayer1() {
        assertEquals("Alice", player1.getName(), "Player1 should be Alice");
    }

    @Test
    void testGetPlayer2() {
        assertEquals("Bob", player2.getName(), "Player2 should be Bob");
    }

    @Test
    void testIncrementScore() {
        assertEquals(0, matchManager.getPlayer1Score());
        assertEquals(0, matchManager.getPlayer2Score());

        matchManager.incrementScore(player1, 1);
        assertEquals(1, matchManager.getPlayer1Score(), "Player1 score should increment by 1");
        assertEquals(0, matchManager.getPlayer2Score());

        matchManager.incrementScore(player2, 2);
        assertEquals(1, matchManager.getPlayer1Score());
        assertEquals(2, matchManager.getPlayer2Score(), "Player2 score should increment by 2");
    }

    @Test
    void testIsMatchOver_NotOverInitially() {
        // With no points scored, match should not be over
        assertFalse(matchManager.isMatchOver(), "Match should not be over at the start");
    }

    @Test
    void testIsMatchOver_WhenOver() {
        // Score some points so a player reaches the match length (3)
        matchManager.incrementScore(player1, 3);
        assertTrue(matchManager.isMatchOver(), "Match should be over when a player reaches match length");
    }

    @Test
    void testGetWinnerName() {
        // Initially, both scores are 0
        // If match is over, it means one player must have more points.
        matchManager.incrementScore(player1, 1);
        matchManager.incrementScore(player2, 3);
        assertTrue(matchManager.isMatchOver());
        assertEquals("Bob", matchManager.getWinnerName(), "Winner should be the player with the higher score");
    }

    @Test
    void testGetPlayer1Score() {
        matchManager.incrementScore(player1, 2);
        assertEquals(2, matchManager.getPlayer1Score(), "Check Player1's score");
    }

    @Test
    void testGetPlayer2Score() {
        matchManager.incrementScore(player2, 2);
        assertEquals(2, matchManager.getPlayer2Score(), "Check Player2's score");
    }

    @Test
    void testGetMatchLength() {
        assertEquals(3, matchManager.getMatchLength(), "Match length should match the constructor value");
    }

    @Test
    void testIsDoublingOffered() {
        assertFalse(matchManager.isDoublingOffered(), "Initially doubling is not offered");
        matchManager.setDoublingOffered(true);
        assertTrue(matchManager.isDoublingOffered(), "Doubling should now be offered");
    }

    @Test
    void testSetDoublingOffered() {
        matchManager.setDoublingOffered(true);
        assertTrue(matchManager.isDoublingOffered());
    }

    @Test
    void testGetPlayerToRespond() {
        assertNull(matchManager.getPlayerToRespond(), "Initially no player to respond to doubling");
        matchManager.setPlayerToRespond(player2);
        assertEquals("Bob", matchManager.getPlayerToRespond().getName(), "Check that playerToRespond is set correctly");
    }

    @Test
    void testSetPlayerToRespond() {
        matchManager.setPlayerToRespond(player1);
        assertEquals("Alice", matchManager.getPlayerToRespond().getName());
    }

    @Test
    void testIsGameOver() {
        assertFalse(matchManager.isGameOver(), "Initially gameOver should be false");
        matchManager.setGameOver(true);
        assertTrue(matchManager.isGameOver(), "GameOver should now be true");
    }

    @Test
    void testSetGameOver() {
        matchManager.setGameOver(true);
        assertTrue(matchManager.isGameOver());
    }
}
