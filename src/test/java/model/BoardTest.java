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

package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
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
    void testDisplayBoard() {
        board.displayBoard();
    }

    @Test
    void testGetLegalMoves() {
        List<Integer> rolls = List.of(2, 4);

        List<String> legalMoves = board.getLegalMoves(player1, rolls);

        assertNotNull(legalMoves);
        assertFalse(legalMoves.isEmpty());
    }

    @Test
    void testMakeMoveValid() {
        int fromPosition = 1;
        int toPosition = 3;

        assertDoesNotThrow(() -> board.makeMove(player1, fromPosition, toPosition));

        Map<Integer, List<Checker>> positions = board.getPositions();
        assertTrue(positions.containsKey(toPosition));
        assertEquals(player1, positions.get(toPosition).getFirst().getOwner());
    }

    @Test
    void testMakeMoveInvalid() {
        int fromPosition = 5;
        int toPosition = 7;

        board.makeMove(player1, fromPosition, toPosition);

        Map<Integer, List<Checker>> positions = board.getPositions();
        assertFalse(positions.containsKey(toPosition));
    }

    @Test
    void testGetPositions() {
        Map<Integer, List<Checker>> positions = board.getPositions();
        assertNotNull(positions);
        assertTrue(positions.containsKey(1));
        assertTrue(positions.containsKey(24));
        assertEquals(2, positions.get(1).size());
    }

    @Test
    void testGetBarForPlayer() {
        List<Checker> bar = board.getBarForPlayer(player1);
        assertNotNull(bar);
        assertTrue(bar.isEmpty());
    }

    @Test
    void testGetBearOffForPlayer() {
        List<Checker> bearOff = board.getBearOffForPlayer(player1);
        assertNotNull(bearOff);
        assertTrue(bearOff.isEmpty());
    }

    @Test
    void testCanEnterFromBarValid() {
        board.getPositions().get(1).clear();

        boolean canEnter = board.canEnterFromBar(player1, 1);
        assertTrue(canEnter);
    }

    @Test
    void testCanEnterFromBarInvalid() {
        List<Checker> checkersAtPosition1 = new ArrayList<>(List.of(new Checker("Black", player2),
                new Checker("Black", player2)));
        board.getPositions().put(1, checkersAtPosition1);

        assertFalse(board.canEnterFromBar(player1, 1));
    }

    @Test
    void testEnterFromBarValid() {
        board.getBarForPlayer(player1).add(new Checker("White", player1));

        board.enterFromBar(player1, 1);

        // Check the bar is empty and position 1 has the checker
        assertTrue(board.getBarForPlayer(player1).isEmpty());
        assertEquals(3, board.getPositions().get(1).size());
        assertEquals(player1, board.getPositions().get(1).getFirst().getOwner());
    }

    @Test
    void testEnterFromBarInvalid() {
        board.getBarForPlayer(player1).add(new Checker("White", player1));

        List<Checker> checkersAtPosition1 = new ArrayList<>(List.of(new Checker("Black", player2),
                new Checker("Black", player2)));

        board.getPositions().put(1, checkersAtPosition1);

        if(board.canEnterFromBar(player1, 1)) {
            board.enterFromBar(player1, 1);
        }

        assertFalse(board.getBarForPlayer(player1).isEmpty());
    }

    @Test
    void testBearOffCheckerValid() {
        board.getPositions().put(24, new ArrayList<>(List.of(new Checker("White", player1))));

        board.bearOffChecker(player1, 24);

        assertFalse(board.getPositions().containsKey(24));
        assertEquals(1, board.getBearOffForPlayer(player1).size());
    }
}
