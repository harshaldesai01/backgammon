package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    private Board board;
    private Player player1;
    private Player player2;

    @BeforeEach
    void setUp() {
        player1 = new Player("Alice");
        player2 = new Player("Bob");
        board = new Board(player1, player2);
    }

    @Test
    void displayBoard() {
        assertDoesNotThrow(() -> board.displayBoard(), "Display board should not throw any exceptions.");
    }

    @Test
    void getLegalMoves() {
        // Simulate dice rolls
        List<Integer> rolls = List.of(3, 5);
        List<String> legalMoves = board.getLegalMoves(player1, rolls);
        assertNotNull(legalMoves, "Legal moves should not be null.");
        assertFalse(legalMoves.isEmpty(), "There should be legal moves for player1.");
    }

    @Test
    void makeMove() {
        // Simulate moving a checker
        board.makeMove(player1, 1, 4);
        Map<Integer, List<Checker>> positions = board.getPositions();
        assertEquals(1, positions.get(4).size(), "Position 4 should now have 1 checker.");
        assertNull(positions.get(1), "Position 1 should be empty after the move.");
    }

    @Test
    void getPositions() {
        Map<Integer, List<Checker>> positions = board.getPositions();
        assertNotNull(positions, "Positions map should not be null.");
        assertTrue(positions.containsKey(1), "Position 1 should be initialized.");
    }

    @Test
    void getBarForPlayer() {
        List<Checker> bar = board.getBarForPlayer(player1);
        assertNotNull(bar, "Bar for player1 should not be null.");
        assertTrue(bar.isEmpty(), "Bar for player1 should initially be empty.");
    }

    @Test
    void getBearOffForPlayer() {
        List<Checker> bearOff = board.getBearOffForPlayer(player1);
        assertNotNull(bearOff, "Bear-off area for player1 should not be null.");
        assertTrue(bearOff.isEmpty(), "Bear-off area for player1 should initially be empty.");
    }

    @Test
    void canEnterFromBar() {
        // Place a checker on the bar for player1
        board.getBarForPlayer(player1).add(new Checker("White", player1));
        boolean canEnter = board.canEnterFromBar(player1, 3);
        assertTrue(canEnter, "Player1 should be able to enter from the bar to position 3.");
    }

    @Test
    void enterFromBar() {
        // Place a checker on the bar for player1
        board.getBarForPlayer(player1).add(new Checker("White", player1));
        board.enterFromBar(player1, 3);

        // Verify checker was moved to position 3
        List<Checker> position3Checkers = board.getPositions().get(3);
        assertNotNull(position3Checkers, "Position 3 should have checkers after entering from the bar.");
        assertEquals(1, position3Checkers.size(), "Position 3 should have 1 checker after entering from the bar.");
        assertTrue(board.getBarForPlayer(player1).isEmpty(), "Bar for player1 should be empty after entering the board.");
    }

    @Test
    void bearOffChecker() {
        // Simulate moving a checker to a bear-off position
        board.makeMove(player1, 1, 25); // Position 25 is off-board for player1
        board.bearOffChecker(player1, 25);

        // Verify the checker is in the bear-off area
        List<Checker> bearOff = board.getBearOffForPlayer(player1);
        assertEquals(1, bearOff.size(), "Player1 should have 1 checker in the bear-off area.");
    }
}
