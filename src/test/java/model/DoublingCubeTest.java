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
        assertEquals(1, doublingCube.getValue(), "Initial value of the doubling cube should be 1");
    }

    @Test
    void testInitialOwner() {
        assertNull(doublingCube.getOwner(), "Initial owner of the doubling cube should be null");
    }

    @Test
    void testDoubleValue() {
        doublingCube.doubleValue(player1);
        assertEquals(2, doublingCube.getValue(), "Value of the doubling cube should double to 2");
        assertEquals(player1, doublingCube.getOwner(), "Owner of the cube should be updated to player1");

        doublingCube.doubleValue(player2);
        assertEquals(4, doublingCube.getValue(), "Value of the doubling cube should double to 4");
        assertEquals(player2, doublingCube.getOwner(), "Owner of the cube should update to player2");
    }

    @Test
    void testMaximumValue() {
        doublingCube.doubleValue(player1); // 2
        doublingCube.doubleValue(player1); // 4
        doublingCube.doubleValue(player1); // 8
        doublingCube.doubleValue(player1); // 16
        doublingCube.doubleValue(player1); // 32
        doublingCube.doubleValue(player1); // 64
        doublingCube.doubleValue(player1); // Exceeding

        assertEquals(64, doublingCube.getValue(), "Value should not exceed 64");
        assertEquals(player1, doublingCube.getOwner(), "Owner should remain as player1");
    }

    @Test
    void testReset() {
        doublingCube.doubleValue(player1); // Double once
        doublingCube.reset();

        assertEquals(1, doublingCube.getValue(), "Value should reset to 1");
        assertNull(doublingCube.getOwner(), "Owner should reset to null");
    }
}
