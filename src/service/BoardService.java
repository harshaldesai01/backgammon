package service;

import model.Board;
import model.Player;

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
}