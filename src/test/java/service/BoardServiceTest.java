/**
 * This file is part of the Backgammon game project developed by the Dice Bros - Group 5 team.
 *
 * Team Information:
 * Team Name: Dice Bros - Group 5
 * Student Names:
 *   - Harshal Desai
 *   - Alparslan Balci
 *   - Manish Tawade
 * GitHub IDs:
 *   - harshaldesai01
 *   - Apistomeister
 *   - Manish9881
 */

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
    void testDisplayBoard() {
        boardService.displayBoard();
    }

    @Test
    void testGetBoard() {
        Board board = boardService.getBoard();
        assertNotNull(board, "The board should not be null.");
    }

    @Test
    void testGetBearOffForPlayer() {
        List<Checker> player1BearOff = boardService.getBearOffForPlayer(player1);
        List<Checker> player2BearOff = boardService.getBearOffForPlayer(player2);

        assertNotNull(player1BearOff, "Player 1's bear-off list should not be null.");
        assertTrue(player1BearOff.isEmpty(), "Player 1's bear-off list should be empty initially.");

        assertNotNull(player2BearOff, "Player 2's bear-off list should not be null.");
        assertTrue(player2BearOff.isEmpty(), "Player 2's bear-off list should be empty initially.");
    }

    @Test
    void testGetPositions() {
        Map<Integer, List<Checker>> positions = boardService.getPositions();

        assertNotNull(positions, "The positions map should not be null.");
        assertTrue(positions.containsKey(1), "Position 1 should exist in the initial board setup.");
        assertTrue(positions.containsKey(24), "Position 24 should exist in the initial board setup.");

        assertEquals(2, positions.get(1).size(), "Position 1 should have 2 checkers initially.");
        assertEquals(player1, positions.get(1).getFirst().getOwner(), "Checkers at position 1 should belong to Player 1.");

        assertEquals(5, positions.get(6).size(), "Position 6 should have 5 checkers initially.");
        assertEquals(player2, positions.get(6).getFirst().getOwner(), "Checkers at position 6 should belong to Player 2.");
    }
}
