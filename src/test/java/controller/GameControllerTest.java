package controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import util.CommandParser;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Disabled
class GameControllerTest {
    private GameController gameController;
    private ByteArrayOutputStream outputStream;
    private CommandParser mockCommandParser;

    @BeforeEach
    void setUp() {
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        mockCommandParser = mock(CommandParser.class);
        gameController = new GameController();
    }

    @Test
    void testStartGameQuitImmediately() {
        when(mockCommandParser.getUserInput())
                .thenReturn("quit");

        gameController.startGame();

        String output = outputStream.toString();
        assertTrue(output.contains("Welcome to Backgammon!"));
        assertTrue(output.contains("Thank you for playing Backgammon!"));
    }

    @Test
    void testSetupNewMatch() {
        when(mockCommandParser.getUserInput())
                .thenReturn("Alice") // Player 1 name
                .thenReturn("Bob")   // Player 2 name
                .thenReturn("3")     // Match length
                .thenReturn("quit");   // Exit game

        gameController.startGame();

        String output = outputStream.toString();
        assertTrue(output.contains("Enter Player 1 name:"));
        assertTrue(output.contains("Enter Player 2 name:"));
        assertTrue(output.contains("Enter the match length"));
    }

    @Test
    void testHandleInvalidPlayerName() {
        when(mockCommandParser.getUserInput())
                .thenReturn("")      // Invalid name
                .thenReturn(" ")     // Invalid name
                .thenReturn("Alice") // Valid name
                .thenReturn("Bob")   // Player 2 name
                .thenReturn("3")     // Match length
                .thenReturn("no");   // Exit game

        gameController.startGame();

        String output = outputStream.toString();
        assertTrue(output.contains("Player name cannot be empty. Please try again."));
    }

    @Test
    void testHandleInvalidMatchLength() {
        when(mockCommandParser.getUserInput())
                .thenReturn("Alice") // Player 1 name
                .thenReturn("Bob")   // Player 2 name
                .thenReturn("-1")    // Invalid length
                .thenReturn("abc")   // Invalid input
                .thenReturn("3")     // Valid length
                .thenReturn("no");   // Exit game

        gameController.startGame();

        String output = outputStream.toString();
        assertTrue(output.contains("Match length must be a positive integer. Try again."));
        assertTrue(output.contains("Invalid input. Please enter a valid number for match length."));
    }

    @Test
    void testDisplayHint() {
        when(mockCommandParser.getUserInput())
                .thenReturn("Alice") // Player 1 name
                .thenReturn("Bob")   // Player 2 name
                .thenReturn("3")     // Match length
                .thenReturn("HINT")  // Display hint
                .thenReturn("no");   // Exit game

        gameController.startGame();

        String output = outputStream.toString();
        assertTrue(output.contains("Available commands:"));
        assertTrue(output.contains("ROLL"));
        assertTrue(output.contains("QUIT"));
        assertTrue(output.contains("HINT"));
    }
}
