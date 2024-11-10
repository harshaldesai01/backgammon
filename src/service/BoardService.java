package service;

import model.Board;
import model.Player;
import model.Checker;
import java.util.List;
import java.util.Map;


public class BoardService {
    private final Board board;

    public BoardService(Player player1, Player player2) {
        this.board = new Board(player1, player2);
    }

    public void displayBoard() {
        board.displayBoard();
    }

    public Board getBoard() {
        return board;
    }

    // Method to get bear-off checkers for a player
    public List<Checker> getBearOffForPlayer(Player player) {
        return board.getBearOffForPlayer(player);
    }

    // Method to get positions of checkers
    public Map<Integer, List<Checker>> getPositions() {
        return board.getPositions();
    }

}