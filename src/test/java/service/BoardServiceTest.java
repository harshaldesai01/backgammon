package service;

import model.Board;
import model.Checker;
import model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class BoardServiceTest {

    private BoardService boardService;
    private Player player1;
    private Player player2;

    @BeforeEach
    void setUp() {
        player1 = new Player("Alice");
        player2 = new Player("Bob");
        boardService = new BoardService(player1, player2);
    }

    @Test
    void testGetBoard() {
        Board board = boardService.getBoard();
        assertNotNull(board, "The board should be initialized");
        assertEquals(player1, board.getPositions().get(1).getFirst().getOwner(), "Player 1 should own checkers on position 1");
        assertEquals(player2, board.getPositions().get(24).getFirst().getOwner(), "Player 2 should own checkers on position 24");
    }

    @Test
    void testGetBearOffForPlayer() {
        // Initially, no checkers should be in the bear-off area
        List<Checker> player1BearOff = boardService.getBearOffForPlayer(player1);
        List<Checker> player2BearOff = boardService.getBearOffForPlayer(player2);

        assertTrue(player1BearOff.isEmpty(), "Player 1 bear-off area should be empty at the start");
        assertTrue(player2BearOff.isEmpty(), "Player 2 bear-off area should be empty at the start");
    }

    @Test
    void testGetPositions() {
        Map<Integer, List<Checker>> positions = boardService.getPositions();

        assertNotNull(positions, "The board positions map should not be null");
        assertTrue(positions.containsKey(1), "The positions map should contain position 1");
        assertTrue(positions.containsKey(24), "The positions map should contain position 24");

        // Check the initial state of a few positions
        assertEquals(2, positions.get(1).size(), "Position 1 should have 2 checkers at the start");
        assertEquals(5, positions.get(6).size(), "Position 6 should have 5 checkers at the start");
        assertEquals(5, positions.get(12).size(), "Position 12 should have 5 checkers at the start");
        assertEquals(2, positions.get(24).size(), "Position 24 should have 2 checkers at the start");
    }

    @Test
    void testDisplayBoard() {
        // Simply ensure no exceptions are thrown when displaying the board
        assertDoesNotThrow(() -> boardService.displayBoard(), "Displaying the board should not throw any exceptions");
    }
}