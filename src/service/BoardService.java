package service;

import model.Board;

public class BoardService {
    private final Board board = new Board();

    public void displayBoard() {
        board.displayBoard();
    }
}
