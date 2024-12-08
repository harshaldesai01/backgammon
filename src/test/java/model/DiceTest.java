package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DiceTest {

    @Test
    void testRollWithinRange() {
        Dice dice = new Dice();

        int roll = dice.roll();

        assertTrue(roll >= 1 && roll <= 6, "Roll should return a value between 1 and 6 inclusive.");
    }

    @Test
    void testRollRandomness() {
        Dice dice = new Dice();
        int minRoll = Integer.MAX_VALUE;
        int maxRoll = Integer.MIN_VALUE;
        boolean[] results = new boolean[6];

        for (int i = 0; i < 1000; i++) {
            int roll = dice.roll();
            assertTrue(roll >= 1 && roll <= 6, "Roll should always be between 1 and 6.");
            results[roll - 1] = true;
            minRoll = Math.min(minRoll, roll);
            maxRoll = Math.max(maxRoll, roll);
        }

        assertEquals(1, minRoll, "The minimum roll value should be 1.");
        assertEquals(6, maxRoll, "The maximum roll value should be 6.");
        for (boolean result : results) {
            assertTrue(result, "All numbers between 1 and 6 should appear at least once in 1000 rolls.");
        }
    }
}
