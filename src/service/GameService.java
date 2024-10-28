package service;

import enums.CommandType;
import model.Dice;
import model.Player;

import static util.CommonConstants.QUIT_MESSAGE;

public class GameService {
    private final BoardService boardService = new BoardService();
    private final Dice dice = new Dice();
    private Player player1;
    private Player player2;
    private Player currentPlayer;

    public void setPlayers(String name1, String name2) {
        player1 = new Player(name1);
        player2 = new Player(name2);
        currentPlayer = player1; // Initialize the game with player1 as the current player
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void rollDice(Player player) {
        int roll1 = dice.roll();
        int roll2 = dice.roll();
        System.out.println(player.getName() + " rolled " + roll1 + " and " + roll2);
        // After each roll, toggle to the other player
        toggleCurrentPlayer();
    }

    public void executeCommand(CommandType command) {
        if (command == CommandType.QUIT) {
            System.out.println(QUIT_MESSAGE);
            System.exit(0);
        }
        if (command == CommandType.ROLL) {
            rollDice(currentPlayer);
        }
    }

    private void toggleCurrentPlayer() {
        currentPlayer = (currentPlayer == player1) ? player2 : player1;
    }

    /**
     * Displays the current state of the game, including the board and current player.
     */
    public void displayGameState() {
        System.out.println("\nCurrent Game State:");

        // Display the board
        boardService.displayBoard();

        // Display the current player's turn
        System.out.println("It's " + currentPlayer.getName() + "'s turn.");
    }

    public boolean isGameOver() {
        // Add game-over condition logic here, returning true if the game is over
        return false; // Placeholder, assuming the game is not over yet
    }
}