package model;

import java.util.Random;

import static util.CommonConstants.MAXIMUM_DICE_VALUE;

/**
 * Represents a single die used in the Backgammon game.
 */
public class Dice {
    private final Random random = new Random();

    /**
     * Rolls the die and returns a value between 1 and 6.
     *
     * @return the result of the die roll.
     */
    public int roll() {
        return random.nextInt(MAXIMUM_DICE_VALUE) + 1;
    }
}
