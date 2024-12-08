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

package util;

import enums.CommandType;
import exceptions.InvalidCommandException;

import static util.CommonConstants.MAXIMUM_DICE_VALUE;
import static util.CommonConstants.MINIMUM_DICE_VALUE;

/**
 * Represents a command for specifying dice rolls. Used to forcefully set the next dice rolls to the given values.
 */
public class DiceCommand extends Command {
    private final int roll1;
    private final int roll2;

    /**
     * Constructs a DiceCommand with the specified dice rolls.
     *
     * @param type  the type of command.
     * @param roll1 the value of the first die.
     * @param roll2 the value of the second die.
     * @throws InvalidCommandException if dice values are not between 1 and 6.
     */
    public DiceCommand(CommandType type, int roll1, int roll2) {
        super(type);
        if (roll1 < MINIMUM_DICE_VALUE || roll1 > MAXIMUM_DICE_VALUE || roll2 < MINIMUM_DICE_VALUE || roll2 > MAXIMUM_DICE_VALUE) {
            throw new InvalidCommandException("Dice values must be between 1 and 6.");
        }
        this.roll1 = roll1;
        this.roll2 = roll2;
    }


    /**
     * Retrieves the value of the first die.
     *
     * @return the value of the first die.
     */
    public int getRoll1() {
        return roll1;
    }

    /**
     * Retrieves the value of the second die.
     *
     * @return the value of the second die.
     */
    public int getRoll2() {
        return roll2;
    }
}

