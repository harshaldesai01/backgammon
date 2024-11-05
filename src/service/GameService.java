package service;

import enums.CommandType;
import model.Checker;
import model.Dice;
import model.Player;

import java.util.List;

import static util.CommonConstants.QUIT_MESSAGE;

public class GameService {
    private BoardService boardService;
    private final Dice dice = new Dice();
    private Player player1;
    private Player player2;
    private Player currentPlayer;

    public GameService() {
        // GameService constructor does not need CommandParser
    }

    /**
     * Sets up players with their names and initializes BoardService with players.
     */
    public void setUpPlayers(String name1, String name2) {
        player1 = new Player(name1);
        player2 = new Player(name2);
        currentPlayer = player1; // Initialize with player1 as the current player

        // Initialize BoardService with player1 and player2
        this.boardService = new BoardService(player1, player2);
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void rollDice(Player player) {
        int roll1 = dice.roll();
        int roll2 = dice.roll();
        System.out.println(player.getName() + " rolled " + roll1 + " and " + roll2);
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

    public void displayGameState() {
        System.out.println("\nCurrent Game State:");
        boardService.displayBoard();
        System.out.println("It's " + currentPlayer.getName() + "'s turn.");
        System.out.println(player1.getName() + "'s pip count: " + calculatePipCount(player1));
        System.out.println(player2.getName() + "'s pip count: " + calculatePipCount(player2));
    }

    public boolean isGameOver() {
        return false; // Placeholder for game-over condition logic
    }

    public void determineStartingPlayer() {
        int rollPlayer1, rollPlayer2;
        System.out.println("Rolling dice to determine who goes first...");
        do {
            rollPlayer1 = dice.roll();
            rollPlayer2 = dice.roll();
            System.out.println(player1.getName() + " rolled: " + rollPlayer1);
            System.out.println(player2.getName() + " rolled: " + rollPlayer2);
            if (rollPlayer1 > rollPlayer2) {
                currentPlayer = player1;
                System.out.println(player1.getName() + " goes first!");
            } else if (rollPlayer2 > rollPlayer1) {
                currentPlayer = player2;
                System.out.println(player2.getName() + " goes first!");
            } else {
                System.out.println("It's a tie! Rolling again...");
            }
        } while (rollPlayer1 == rollPlayer2);
    }

    private int calculatePipCount(Player player) {
        int pipCount = 0;
        int direction = (player == player1) ? 1 : -1;
        for (int position : boardService.getBoard().getPositions().keySet()) {
            List<Checker> checkers = boardService.getBoard().getPositions().get(position);
            for (Checker checker : checkers) {
                if (checker.getOwner().equals(player)) {
                    pipCount += Math.abs(position - (direction == 1 ? 25 : 0));
                }
            }
        }
        return pipCount;
    }
}