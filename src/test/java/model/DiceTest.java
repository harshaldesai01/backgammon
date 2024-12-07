package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DiceTest {

    @Test
    void roll() {
        Dice dice = new Dice();

        for (int i = 0; i < 100; i++) { // Test multiple rolls to ensure randomness
            int result = dice.roll();
            assertTrue(result >= 1 && result <= 6, "Dice roll should produce a value between 1 and 6 inclusive.");
        }
    }
}
