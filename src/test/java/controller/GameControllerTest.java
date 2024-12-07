package controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class GameControllerTest {

    private GameController gameController;
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outputStream));
        gameController = new GameController();
    }

    @Test
    void startGame() {
        String simulatedInput = """
                Alice
                Bob
                3
                no
                """;
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        assertDoesNotThrow(() -> gameController.startGame());

        String output = outputStream.toString();
        assertTrue(output.contains("Welcome to Backgammon!"), "Output should contain the welcome message.");
        assertTrue(output.contains("Enter Player 1 name:"), "Output should prompt for Player 1 name.");
        assertTrue(output.contains("Enter Player 2 name:"), "Output should prompt for Player 2 name.");
        assertTrue(output.contains("Enter the match length"), "Output should prompt for match length.");
        assertTrue(output.contains("Thank you for playing Backgammon!"), "Output should thank the user when they choose to exit.");
    }
}
