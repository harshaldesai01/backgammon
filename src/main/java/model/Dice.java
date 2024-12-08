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
