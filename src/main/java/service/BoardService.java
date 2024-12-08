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
import model.Player;
import model.Checker;
import java.util.List;
import java.util.Map;

/**
 * Service class that provides an interface for interacting with the Backgammon board.
 * It acts as a wrapper for the {@link Board} class, managing board state and operations.
 */
public class BoardService {
    private final Board board;

    /**
     * Initializes the BoardService with a new {@link Board} for the given players.
     *
     * @param player1 the first player.
     * @param player2 the second player.
     */
    public BoardService(Player player1, Player player2) {
        this.board = new Board(player1, player2);
    }

    /**
     * Displays the current state of the board in a formatted view.
     */
    public void displayBoard() {
        board.displayBoard();
    }

    /**
     * Retrieves the underlying {@link Board} instance.
     *
     * @return the board managed by this service.
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Retrieves the list of checkers that have been borne off by the specified player.
     *
     * @param player the player whose bear-off area is being queried.
     * @return a list of checkers in the bear-off area for the player.
     */
    public List<Checker> getBearOffForPlayer(Player player) {
        return board.getBearOffForPlayer(player);
    }

    /**
     * Retrieves the current positions of all checkers on the board.
     *
     * @return a map of board positions to lists of checkers at those positions.
     */
    public Map<Integer, List<Checker>> getPositions() {
        return board.getPositions();
    }

}