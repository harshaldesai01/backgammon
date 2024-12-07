package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CheckerTest {

    @Test
    void getColor() {
        // Create a Player instance
        Player player = new Player("Alice");

        // Create a Checker instance
        Checker checker = new Checker("White", player);

        // Verify the color is correct
        assertEquals("White", checker.getColor(), "getColor() should return the correct color of the checker.");
    }

    @Test
    void getOwner() {
        // Create a Player instance
        Player player = new Player("Alice");

        // Create a Checker instance
        Checker checker = new Checker("White", player);

        // Verify the owner is correct
        assertEquals(player, checker.getOwner(), "getOwner() should return the correct owner of the checker.");
        assertEquals("Alice", checker.getOwner().getName(), "getOwner() should return the player with the correct name.");
    }
}
