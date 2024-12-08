package util;

import enums.CommandType;
import exceptions.InvalidCommandException;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

class CommandParserTest {

    @Test
    void testGetUserInput() {
        String input = "ROLL\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        CommandParser commandParser = new CommandParser();

        String userInput = commandParser.getUserInput();

        assertEquals("ROLL", userInput, "The user input should be 'ROLL'.");
    }

    @Test
    void testParseCommandValidRollCommand() throws InvalidCommandException {
        CommandParser commandParser = new CommandParser();

        Command command = commandParser.parseCommand("ROLL");

        assertNotNull(command, "The command should not be null.");
        assertEquals(CommandType.ROLL, command.getType(), "The command type should be ROLL.");
    }

    @Test
    void testParseCommandValidTestCommand() throws InvalidCommandException {
        CommandParser commandParser = new CommandParser();

        Command command = commandParser.parseCommand("TEST testfile.txt");

        assertNotNull(command, "The command should not be null.");
        assertInstanceOf(TestCommand.class, command, "The command should be of type TestCommand.");
        assertEquals(CommandType.TEST, command.getType(), "The command type should be TEST.");
    }

    @Test
    void testParseCommandValidDiceCommand() throws InvalidCommandException {
        CommandParser commandParser = new CommandParser();

        Command command = commandParser.parseCommand("DICE 3 5");

        assertNotNull(command, "The command should not be null.");
        assertInstanceOf(DiceCommand.class, command, "The command should be of type DiceCommand.");
        assertEquals(CommandType.DICE, command.getType(), "The command type should be DICE.");
        assertEquals(3, ((DiceCommand) command).getRoll1(), "The first roll should be 3.");
        assertEquals(5, ((DiceCommand) command).getRoll2(), "The second roll should be 5.");
    }

    @Test
    void testParseCommandInvalidCommand() {
        CommandParser commandParser = new CommandParser();

        assertThrows(InvalidCommandException.class, () -> commandParser.parseCommand("INVALID"), "Parsing an invalid command should throw an exception.");
    }

    @Test
    void testParseCommandInvalidTestCommandFormat() {
        CommandParser commandParser = new CommandParser();

        assertThrows(InvalidCommandException.class, () -> commandParser.parseCommand("TEST"), "Parsing a TEST command without a filename should throw an exception.");
    }

    @Test
    void testParseCommandInvalidDiceCommandFormat() {
        CommandParser commandParser = new CommandParser();

        assertThrows(InvalidCommandException.class, () -> commandParser.parseCommand("DICE 3"), "Parsing a DICE command with missing rolls should throw an exception.");
    }

    @Test
    void testParseCommandInvalidDiceValues() {
        CommandParser commandParser = new CommandParser();

        assertThrows(InvalidCommandException.class, () -> commandParser.parseCommand("DICE 3 A"), "Parsing a DICE command with non-numeric rolls should throw an exception.");
    }

    @Test
    void testClose() {
        CommandParser commandParser = new CommandParser();

        assertDoesNotThrow(commandParser::close, "Closing the scanner should not throw an exception.");
    }
}
