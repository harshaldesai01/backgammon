package service;

import enums.CommandType;
import model.Dice;
import model.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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

        // Display the board
        boardService.displayBoard();

        // Display the current player's turn
        System.out.println("It's " + currentPlayer.getName() + "'s turn.");
    }

    public boolean isGameOver() {
        return false; // Placeholder, assuming the game is not over yet
    }

    private void displayMoveOptions(Player player, int roll1, int roll2) {
        List<String> options = generateMoveOptions(player, roll1, roll2);

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

        // Example options based on dice rolls; modify according to game rules
        options.add("Play " + roll1 + "-" + roll2);
        options.add("Play " + roll1 + " from one position and " + roll2 + " from another position");

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
            // Implement the move execution based on the selected option
        } else {
            System.out.println("Invalid selection. Please try again.");
        }
    }
}
