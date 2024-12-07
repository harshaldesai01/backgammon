package util;

import enums.CommandType;
import exceptions.InvalidCommandException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DiceCommandTest {

    @Test
    void getRoll1() {
        DiceCommand diceCommand = new DiceCommand(CommandType.DICE, 3, 5);
        assertEquals(3, diceCommand.getRoll1(), "getRoll1() should return the first dice roll value.");
    }

    @Test
    void getRoll2() {
        DiceCommand diceCommand = new DiceCommand(CommandType.DICE, 3, 5);
        assertEquals(5, diceCommand.getRoll2(), "getRoll2() should return the second dice roll value.");
    }

    @Test
    void validDiceValues() {
        assertDoesNotThrow(() -> new DiceCommand(CommandType.DICE, 1, 6),
                "DiceCommand with valid values should not throw an exception.");
    }

    @Test
    void invalidDiceValuesTooLow() {
        Exception exception = assertThrows(InvalidCommandException.class,
                () -> new DiceCommand(CommandType.DICE, 0, 5),
                "DiceCommand should throw an exception if any dice value is less than 1.");

        assertEquals("Dice values must be between 1 and 6.", exception.getMessage());
    }

    @Test
    void invalidDiceValuesTooHigh() {
        Exception exception = assertThrows(InvalidCommandException.class,
                () -> new DiceCommand(CommandType.DICE, 4, 7),
                "DiceCommand should throw an exception if any dice value is greater than 6.");

        assertEquals("Dice values must be between 1 and 6.", exception.getMessage());
    }

    @Test
    void invalidDiceValuesBothInvalid() {
        Exception exception = assertThrows(InvalidCommandException.class,
                () -> new DiceCommand(CommandType.DICE, 0, 7),
                "DiceCommand should throw an exception if both dice values are invalid.");

        assertEquals("Dice values must be between 1 and 6.", exception.getMessage());
    }
}
