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
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DiceCommandTest {

    @Test
    void testValidDiceValues() {
        DiceCommand diceCommand = new DiceCommand(CommandType.DICE, 3, 5);

        assertEquals(3, diceCommand.getRoll1(), "Roll1 should be 3.");
        assertEquals(5, diceCommand.getRoll2(), "Roll2 should be 5.");
        assertEquals(CommandType.DICE, diceCommand.getType(), "The command type should be DICE.");
    }

    @Test
    void testInvalidDiceValuesThrowsException() {
        assertThrows(InvalidCommandException.class, () -> new DiceCommand(CommandType.DICE, 0, 5), "Roll1 value of 0 should throw an exception.");
        assertThrows(InvalidCommandException.class, () -> new DiceCommand(CommandType.DICE, 7, 5), "Roll1 value of 7 should throw an exception.");
        assertThrows(InvalidCommandException.class, () -> new DiceCommand(CommandType.DICE, 3, 0), "Roll2 value of 0 should throw an exception.");
        assertThrows(InvalidCommandException.class, () -> new DiceCommand(CommandType.DICE, 3, 7), "Roll2 value of 7 should throw an exception.");
    }

    @Test
    void testValidDiceCommandType() {
        DiceCommand diceCommand = new DiceCommand(CommandType.DICE, 4, 6);

        assertEquals(CommandType.DICE, diceCommand.getType(), "The command type should be DICE.");
    }
}
