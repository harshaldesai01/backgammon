package model;

import org.junit.jupiter.api.Test;

import java.util.HashSet;

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

    @Test
    void testHashCodeConsistency() {
        Player player = new Player("Alice");
        int initialHashCode = player.hashCode();

        // Ensure hashCode returns the same value every time
        assertEquals(initialHashCode, player.hashCode(), "HashCode should be consistent across multiple calls.");
    }

    @Test
    void testHashCodeEqualityContract() {
        Player player1 = new Player("Alice");
        Player player2 = new Player("Alice");

        // Assume equals is overridden correctly to compare based on name
        assertEquals(player1, player2, "Players with the same name should be equal.");
        assertEquals(player1.hashCode(), player2.hashCode(), "Equal objects must have the same hashCode.");
    }

    @Test
    void testHashCodeInHashSet() {
        Player player1 = new Player("Alice");
        Player player2 = new Player("Alice");

        HashSet<Player> playerSet = new HashSet<>();
        playerSet.add(player1);

        // Check that the second player with the same name is considered a duplicate (because they are equal)
        assertTrue(playerSet.contains(player2), "HashSet should consider equal players as duplicates.");
    }

    @Test
    void testHashCodeInequality() {
        Player player1 = new Player("Alice");
        Player player2 = new Player("Bob");

        // Ensure unequal objects do not necessarily have the same hashCode
        assertNotEquals(player1, player2, "Players with different names should not be equal.");
        assertNotEquals(player1.hashCode(), player2.hashCode(), "Unequal objects should ideally have different hashCodes.");
    }
}
