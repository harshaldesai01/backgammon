package controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class GameControllerTest {
    private GameController gameController;
    private ByteArrayOutputStream outputStream;

    @BeforeEach
    void setUp() {
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        gameController = new GameController();
    }

    @Test
    void testStartGameQuitImmediately() {
        String input = "no\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        gameController.startGame();

        String output = outputStream.toString();
        assertTrue(output.contains("Welcome to Backgammon!"));
        assertTrue(output.contains("Thank you for playing Backgammon!"));
    }

    @Test
    void testSetupNewMatch() {
        String input = "Alice\nBob\n3\nno\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        gameController.startGame();

        String output = outputStream.toString();
        assertTrue(output.contains("Enter Player 1 name:"));
        assertTrue(output.contains("Enter Player 2 name:"));
        assertTrue(output.contains("Enter the match length"));
    }

    @Test
    void testHandleInvalidPlayerName() {
        String input = "\n\nAlice\nBob\n3\nno\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        gameController.startGame();

        String output = outputStream.toString();
        assertTrue(output.contains("Player name cannot be empty. Please try again."));
    }

    @Test
    void testHandleInvalidMatchLength() {
        String input = "Alice\nBob\n-1\nabc\n3\nno\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        gameController.startGame();

        String output = outputStream.toString();
        assertTrue(output.contains("Match length must be a positive integer. Try again."));
        assertTrue(output.contains("Invalid input. Please enter a valid number for match length."));
    }

    @Test
    void testPlaySingleGameAndQuit() {
        String input = "Alice\nBob\n3\nROLL\nQUIT\nno\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        gameController.startGame();

        String output = outputStream.toString();
        assertTrue(output.contains("Alice Score: 0"));
        assertTrue(output.contains("Bob Score: 0"));
        assertTrue(output.contains("ROLL"));
        assertTrue(output.contains("Thank you for playing Backgammon!"));
    }

    @Test
    void testDisplayHint() {
        String input = "Alice\nBob\n3\nHINT\nno\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        gameController.startGame();

        String output = outputStream.toString();
        assertTrue(output.contains("Available commands:"));
        assertTrue(output.contains("ROLL"));
        assertTrue(output.contains("QUIT"));
        assertTrue(output.contains("HINT"));
    }
}
