package service;

import enums.CommandType;
import model.Checker;
import model.Dice;
import model.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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

        // Log the roll (if there’s a logging mechanism, add it here)

        // Display possible moves based on the dice roll
        displayMoveOptions(player, roll1, roll2);

        // Toggle to the other player after rolling and move selection
        toggleCurrentPlayer();
    }

    public void executeCommand(CommandType command) {
        if (command == CommandType.QUIT) {
            System.out.println(QUIT_MESSAGE);
            System.exit(0);
        } else if (command == CommandType.ROLL) {
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
        if (hasPlayerWon(player1)) {
            System.out.println("Congratulations " + player1.getName() + ", you have won the game!");
            return true;
        } else if (hasPlayerWon(player2)) {
            System.out.println("Congratulations " + player2.getName() + ", you have won the game!");
            return true;
        }
        return false; // The game is still ongoing
    }

    // Helper method to check if a player has won
    private boolean hasPlayerWon(Player player) {
        // Check if the player has no checkers remaining on the board
        return boardService.getBearOffForPlayer(player).size() + boardService.getPositions().values().stream()
                .flatMap(List::stream)
                .filter(checker -> checker.getOwner().equals(player))
                .count() == 0; // If all checkers are off the board, count should be 0
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

    private void displayMoveOptions(Player player, int roll1, int roll2) {
        List<String> options = generateMoveOptions(player, roll1, roll2);
        if (options.isEmpty()) {
            System.out.println("No legal moves available. Turn passes to the next player.");
            toggleCurrentPlayer();
            return;
        }
        // Display options for the player to select
        System.out.println("Select a move option:");
        char optionLetter = 'A';
        for (String option : options) {
            System.out.println(optionLetter + ") " + option);
            optionLetter++;
        }

        // Capture the player's selection
        char selectedOption = getUserSelection();
        executeSelectedOption(selectedOption, options);
    }

    private List<String> generateMoveOptions(Player player, int roll1, int roll2) {
        List<String> options = new ArrayList<>();

        options = boardService.getBoard().getLegalMoves(player, roll1, roll2);

        // Example options based on dice rolls; modify according to game rules
        //options.add("Play " + roll1 + "-" + roll2);
        //options.add("Play " + roll1 + " from one position and " + roll2 + " from another position");

        // Additional options can be generated based on specific rules of backgammon
        return options;
    }

    private char getUserSelection() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the option letter: ");
        return scanner.next().toUpperCase().charAt(0);
    }

    private void executeSelectedOption(char selectedOption, List<String> options) {
        int optionIndex = selectedOption - 'A';
        if (optionIndex >= 0 && optionIndex < options.size()) {
            String chosenMove = options.get(optionIndex);
            System.out.println("You chose: " + chosenMove);

            // Parse the chosen move to get fromPosition and toPosition
            String[] moveParts = chosenMove.split(" -> ");
            int fromPosition = Integer.parseInt(moveParts[0].trim());
            int toPosition = Integer.parseInt(moveParts[1].trim());

            // Make the move on the board
            boardService.getBoard().makeMove(currentPlayer, fromPosition, toPosition);
        } else {
            System.out.println("Invalid selection. Please try again.");
            // Optionally, you can re-prompt the user for a valid selection
        }
    }
}
