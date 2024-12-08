package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    @Test
    void testGetName() {
        String playerName = "Alice";
        Player player = new Player(playerName);

        String result = player.getName();

        assertEquals(playerName, result, "Player name should match the value passed to the constructor.");
    }

    @Test
    void testEmptyName() {
        String playerName = "";
        Player player = new Player(playerName);

        String result = player.getName();

        assertEquals(playerName, result, "Player name should handle empty strings correctly.");
    }
}
