package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    @Test
    void getName() {
        // Create a Player instance with a name
        Player player = new Player("Alice");

        // Verify that the name is correctly retrieved
        assertEquals("Alice", player.getName(), "getName() should return the correct player name.");
    }
}
