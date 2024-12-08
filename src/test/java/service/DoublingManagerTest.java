package service;

import model.DoublingCube;
import model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DoublingManagerTest {
    private DoublingManager doublingManager;
    private Player player1;
    private Player player2;

    @BeforeEach
    void setUp() {
        doublingManager = new DoublingManager();
        player1 = new Player("Alice");
        player2 = new Player("Bob");
    }

    @Test
    void testGetPlayerToRespondInitiallyNull() {
        assertNull(doublingManager.getPlayerToRespond(), "The initial player to respond should be null.");
    }

    @Test
    void testSetPlayerToRespond() {
        doublingManager.setPlayerToRespond(player1);

        assertEquals(player1, doublingManager.getPlayerToRespond(), "The player to respond should be set to player1.");

        doublingManager.setPlayerToRespond(player2);

        assertEquals(player2, doublingManager.getPlayerToRespond(), "The player to respond should be updated to player2.");
    }

    @Test
    void testGetDoublingCube() {
        DoublingCube doublingCube = doublingManager.getDoublingCube();

        assertNotNull(doublingCube, "The doubling cube should not be null.");

        assertEquals(1, doublingCube.getValue(), "The initial value of the doubling cube should be 1.");

        assertNull(doublingCube.getOwner(), "The initial owner of the doubling cube should be null.");
    }
}
