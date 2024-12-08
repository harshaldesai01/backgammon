package controller;

import enums.CommandType;
import exceptions.InvalidCommandException;
import org.junit.jupiter.api.*;
import util.Command;
import util.CommandParser;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.mockito.Mockito.*;

/**
 * JUnit test class for GameController using Mockito.
 */
class GameControllerTest {

    private CommandParser mockCommandParser;
    private GameController gameController;

    // Streams to capture System.out output
    private final ByteArrayOutputStream outContent = new
            ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUp() {
        // Initialize the mock CommandParser
        mockCommandParser = mock(CommandParser.class);

        // Initialize GameController with the mocked CommandParser
        gameController = new GameController(mockCommandParser);

        // Redirect System.out to capture outputs
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void tearDown() {
        // Restore original System.out
        System.setOut(originalOut);
    }

    @Test
    void testStartGameWithInvalidCommand() throws InvalidCommandException {
        // Define the sequence of inputs:
        // 1. "mans" - Player 1 name
        // 2. "sa" - Player 2 name
        // 3. "1" - Match length
        // 4. "invalid_command" - Invalid command
        // 5. "no" - Terminate the game

        // Mocking user inputs using Mockito's when().thenReturn() chaining
        when(mockCommandParser.getUserInput())
                .thenReturn("mans")            // Player 1 name
                .thenReturn("sa")              // Player 2 name
                .thenReturn("1")               // Match length
                .thenReturn("invalid_command") // Invalid command
                .thenReturn("no");             // Terminate the game

        // Mocking parseCommand() behavior for invalid command
        when(mockCommandParser.parseCommand("invalid_command"))
                .thenThrow(new InvalidCommandException("Invalid command: 'invalid_command'. Type 'HINT' for valid commands."));

        // Mocking parseCommand() behavior for terminating the game
        when(mockCommandParser.parseCommand("no"))
                .thenReturn(new Command(CommandType.QUIT));

        // Attempt to start the game
        gameController.startGame();

        // Convert captured output to string
        String output = outContent.toString();

        Assertions.assertTrue(output.contains("Welcome to Backgammon!"), "Should display welcome message.");
        Assertions.assertTrue(output.contains("Enter Player 1 name: "), "Should prompt for Player 1 name.");
        Assertions.assertTrue(output.contains("Enter Player 2 name: "), "Should prompt for Player 2 name.");
        Assertions.assertTrue(output.contains("Enter the match length (e.g., 3, 5, 7): "), "Should prompt for match length.");
        Assertions.assertTrue(output.contains("Invalid command: 'invalid_command'. Type 'HINT' for valid commands."), "Should display invalid command error.");

        // Verify that getUserInput() was called exactly 5 times
        verify(mockCommandParser, times(5)).getUserInput();

        // Verify that parseCommand() was called with the correct arguments
        verify(mockCommandParser).parseCommand("invalid_command");
        verify(mockCommandParser).parseCommand("no");
    }
}