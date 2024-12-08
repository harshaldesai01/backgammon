package service;

import enums.CommandType;
import exceptions.InvalidCommandException;
import model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import util.Command;

import static org.junit.jupiter.api.Assertions.*;

class GameServiceTest {
    private GameService gameService;
    private MatchManager matchManager;

    @BeforeEach
    void setUp() {
        Player player1 = new Player("Alice");
        Player player2 = new Player("Bob");
        matchManager = new MatchManager(player1.getName(), player2.getName(), 3); // A match of 3 games
        gameService = new GameService(matchManager);
    }

    @Test
    void testSetPresetDiceRolls() {
        gameService.setPresetDiceRolls(3, 5);

        assertDoesNotThrow(() -> gameService.setPresetDiceRolls(3, 5), "Setting dice rolls should not throw exceptions.");
    }

    @Test
    void testSetUpGame() {
        gameService.setUpGame();

        assertNotNull(gameService, "GameService should be properly initialized.");
        assertFalse(gameService.isGameOver(), "Game should not be over immediately after setup.");
    }

    @Disabled
    void testExecuteCommandRoll() {
        gameService.setUpGame();
        Command command = new Command(CommandType.ROLL);

        assertDoesNotThrow(() -> gameService.executeCommand(command), "Executing a ROLL command should not throw an exception.");
    }

    @Test
    void testExecuteCommandInvalid() {
        Command command = new Command(CommandType.HINT); // Unsupported in current context

        assertDoesNotThrow(() -> gameService.executeCommand(command), "Invalid commands should be gracefully handled.");
    }

    @Test
    void testUpdateScoreWithWinner() {
        gameService.setUpGame();
        gameService.updateScore();

        int p1Score = matchManager.getPlayer1Score();
        int p2Score = matchManager.getPlayer2Score();

        assertEquals(0, p1Score, "Initially, Player 1 should have 0 points.");
        assertEquals(0, p2Score, "Initially, Player 2 should have 0 points.");
    }

    @Test
    void testIsGameOverInitial() {
        gameService.setUpGame();

        assertFalse(gameService.isGameOver(), "Game should not be over immediately after setup.");
    }

    @Test
    void testSetGameOver() {
        gameService.setGameOver(true);

        assertTrue(gameService.isGameOver(), "Game should be marked as over after setting it to true.");
    }

    @Disabled
    void testOfferDouble() throws InvalidCommandException {
        gameService.setUpGame();
        gameService.setPresetDiceRolls(3, 5);

        assertDoesNotThrow(() -> gameService.offerDouble(), "Offering a double should not throw exceptions.");
    }

    @Disabled
    void testAcceptDouble() throws InvalidCommandException {
        gameService.setUpGame();
        gameService.setPresetDiceRolls(3, 5);
        gameService.offerDouble();

        assertDoesNotThrow(() -> gameService.acceptDouble(), "Accepting a double should not throw exceptions.");
    }

    @Disabled
    void testRefuseDouble() {
        gameService.setUpGame();
        gameService.setPresetDiceRolls(3, 5);
        gameService.offerDouble();

        assertDoesNotThrow(() -> gameService.refuseDouble(), "Refusing a double should not throw exceptions.");
        assertTrue(gameService.isGameOver(), "Game should end when a double is refused.");
    }

    @Test
    void testDisplayGameState() {
        gameService.setUpGame();

        assertDoesNotThrow(() -> gameService.displayGameState(), "Displaying game state should not throw exceptions.");
    }
}
