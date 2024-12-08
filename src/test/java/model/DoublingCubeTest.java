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

package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DoublingCubeTest {
    private DoublingCube doublingCube;
    private Player player1;
    private Player player2;

    @BeforeEach
    void setUp() {
        doublingCube = new DoublingCube();
        player1 = new Player("Alice");
        player2 = new Player("Bob");
    }

    @Test
    void testInitialValue() {
        assertEquals(1, doublingCube.getValue(), "Doubling cube value should initialize to 1.");
    }

    @Test
    void testInitialOwner() {
        assertNull(doublingCube.getOwner(), "Doubling cube owner should initially be null.");
    }

    @Test
    void testDoubleValue() {
        doublingCube.doubleValue(player1);

        assertEquals(2, doublingCube.getValue(), "Doubling cube value should double from 1 to 2.");
        assertEquals(player1, doublingCube.getOwner(), "Doubling cube owner should be player1.");
    }

    @Test
    void testDoubleValueMultipleTimes() {
        doublingCube.doubleValue(player1);
        doublingCube.doubleValue(player2);

        assertEquals(4, doublingCube.getValue(), "Doubling cube value should be 4 after two doublings.");
        assertEquals(player2, doublingCube.getOwner(), "Doubling cube owner should be updated to player2.");
    }

    @Test
    void testReset() {
        doublingCube.doubleValue(player1);

        doublingCube.reset();

        assertEquals(1, doublingCube.getValue(), "Doubling cube value should reset to 1.");
        assertNull(doublingCube.getOwner(), "Doubling cube owner should be reset to null.");
    }
}
