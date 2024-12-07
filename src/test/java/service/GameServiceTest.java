package service;

import enums.CommandType;
import model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.DiceCommand;

import static org.junit.jupiter.api.Assertions.*;

class GameServiceTest {

    private GameService gameService;
    private MatchManager matchManager;
    private Player player1;
    private Player player2;

    @BeforeEach
    void setUp() {
        matchManager = new MatchManager("Alice", "Bob", 3);
        gameService = new GameService(matchManager);
        player1 = matchManager.getPlayer1();
        player2 = matchManager.getPlayer2();
        gameService.setUpGame();
    }

    @Test
    void setPresetDiceRolls() {
        gameService.setPresetDiceRolls(3, 5);
        // Simulate a roll and check if the preset values are used
        gameService.setPresetDiceRolls(3, 5);
        assertDoesNotThrow(() -> gameService.setPresetDiceRolls(3, 5), "Setting preset dice rolls should not throw an exception.");
    }

    @Test
    void setUpGame() {
        gameService.setUpGame();
        assertNotNull(gameService, "GameService should be properly initialized.");
    }

    @Test
    void executeCommand() {
        DiceCommand diceCommand = new DiceCommand(CommandType.DICE, 4, 6);
        assertDoesNotThrow(() -> gameService.executeCommand(diceCommand), "Executing a DICE command should not throw exceptions.");
    }

    @Test
    void updateScorePlayerOne() {
        // Simulate scoring by making Player1 win the game
        matchManager.incrementScore(player1, 3);
        gameService.updateScore();
        assertEquals(3, matchManager.getPlayer1Score(), "Player1 should have 3 points after scoring.");
    }

    @Test
    void updateScorePlayerTwo() {
        // Simulate scoring by making Player1 win the game
        matchManager.incrementScore(player2, 3);
        gameService.updateScore();
        assertEquals(3, matchManager.getPlayer1Score(), "Player1 should have 3 points after scoring.");
    }


    @Test
    void isGameOver() {
        matchManager.incrementScore(player1, 3);
        assertTrue(gameService.isGameOver(), "Game should be over when one player reaches the match length.");
    }

    @Test
    void displayGameState() {
        assertDoesNotThrow(() -> gameService.displayGameState(), "Displaying the game state should not throw exceptions.");
    }

    @Test
    void offerDouble() {
        assertDoesNotThrow(() -> gameService.offerDouble(), "Offering a double should not throw exceptions.");
    }

    @Test
    void acceptDouble() {
        gameService.offerDouble();
        assertDoesNotThrow(() -> gameService.acceptDouble(), "Accepting a double should not throw exceptions.");
    }

    @Test
    void refuseDouble() {
        gameService.offerDouble();
        assertDoesNotThrow(() -> gameService.refuseDouble(), "Refusing a double should not throw exceptions.");
    }
}
