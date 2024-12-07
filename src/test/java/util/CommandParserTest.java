package util;

import enums.CommandType;
import exceptions.InvalidCommandException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommandParserTest {

    private CommandParser parser;

    @BeforeEach
    void setUp() {
        parser = new CommandParser();
    }

    @Test
    void testEmptyInput() {
        assertThrows(InvalidCommandException.class, () -> parser.parseCommand(""));
    }

    @Test
    void testParseSimpleCommand() throws InvalidCommandException {
        Command cmd = parser.parseCommand("roll");
        assertEquals(CommandType.ROLL, cmd.getType(), "Command type should be ROLL");
    }

    @Test
    void testLowercaseCommands() throws InvalidCommandException {
        // Already tested some cases, but just to emphasize:
        Command cmd = parser.parseCommand("roll");
        assertEquals(CommandType.ROLL, cmd.getType());
    }

    @Test
    void testOnlyWhitespaceInput() {
        assertThrows(InvalidCommandException.class, () -> parser.parseCommand("   "));
    }


    @Test
    void testParseCommandCaseInsensitivity() throws InvalidCommandException {
        Command cmd = parser.parseCommand("Hint");
        assertEquals(CommandType.HINT, cmd.getType(), "Command should parse regardless of case");
    }

    @Test
    void testParseTestCommandValid() throws InvalidCommandException {
        Command cmd = parser.parseCommand("test moves.txt");
        assertTrue(cmd instanceof TestCommand, "Should return a TestCommand");
        assertEquals(CommandType.TEST, cmd.getType());

        TestCommand testCmd = (TestCommand) cmd;
        assertEquals("moves.txt", testCmd.getFilename(), "Filename should match input");
    }

    @Test
    void testParseTestCommandMissingArgument() {
        assertThrows(InvalidCommandException.class, () -> parser.parseCommand("test"));
    }

    @Test
    void testParseDiceCommandValid() throws InvalidCommandException {
        Command cmd = parser.parseCommand("dice 3 4");
        assertTrue(cmd instanceof DiceCommand, "Should return a DiceCommand");
        assertEquals(CommandType.DICE, cmd.getType());

        DiceCommand diceCmd = (DiceCommand) cmd;
        assertEquals(3, diceCmd.getRoll1(), "First die should be 3");
        assertEquals(4, diceCmd.getRoll2(), "Second die should be 4");
    }

    @Test
    void testParseDiceCommandInvalidFormat() {
        assertThrows(InvalidCommandException.class, () -> parser.parseCommand("dice 3"));
    }

    @Test
    void testParseDiceCommandNonInteger() {
        assertThrows(InvalidCommandException.class, () -> parser.parseCommand("dice a b"));
    }

    @Test
    void testParseInvalidCommand() {
        assertThrows(InvalidCommandException.class, () -> parser.parseCommand("xyz"));
    }

    @Test
    void testParseHintCommand() throws InvalidCommandException {
        Command cmd = parser.parseCommand("hint");
        assertEquals(CommandType.HINT, cmd.getType(), "Command should be HINT");
    }

    @Test
    void testParseQuitCommand() throws InvalidCommandException {
        Command cmd = parser.parseCommand("quit");
        assertEquals(CommandType.QUIT, cmd.getType(), "Command should be QUIT");
    }

    @Test
    void testDiceCommandWithBoundaryValues() throws InvalidCommandException {
        Command cmd = parser.parseCommand("dice 1 6");
        assertTrue(cmd instanceof DiceCommand);
        DiceCommand diceCmd = (DiceCommand) cmd;
        assertEquals(1, diceCmd.getRoll1());
        assertEquals(6, diceCmd.getRoll2());
    }

}