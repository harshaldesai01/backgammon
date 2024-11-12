package service;

import enums.CommandType;
import model.Checker;
import model.Dice;
import model.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GameService {
    private BoardService boardService;
    private final Dice dice = new Dice();
    private Player player1;
    private Player player2;
    private Player currentPlayer;

    public GameService() {
        // GameService constructor does not need CommandParser
    }

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

    public void executeRollAndPlay(Player player) {
        int roll1 = dice.roll();
        int roll2 = dice.roll();
        System.out.println(player.getName() + " rolled " + roll1 + " and " + roll2);

        playRoll(player, roll1, roll2);
    }

    public void executeCommand(CommandType command) {
        if (command == CommandType.PIP) {
            System.out.println(player1.getName() + "'s pip count: " + calculatePipCount(player1));
            System.out.println(player2.getName() + "'s pip count: " + calculatePipCount(player2));
        } else if (command == CommandType.ROLL) {
            executeRollAndPlay(currentPlayer);
            displayGameState();
            toggleCurrentPlayer();
        }
    }

    private void toggleCurrentPlayer() {
        currentPlayer = (currentPlayer == player1) ? player2 : player1;
    }

    public void displayGameState() {
        System.out.println("\nCurrent Game State:");
        boardService.displayBoard();
        System.out.println("It's " + currentPlayer.getName() + "'s turn.");
        System.out.println(currentPlayer.getName() + "'s pip count: " + calculatePipCount(currentPlayer));
    }

    public boolean isGameOver() {
        if (hasPlayerWon(player1)) {
            System.out.println("Congratulations " + player1.getName() + ", you have won the game!");
            return true;
        } else if (hasPlayerWon(player2)) {
            System.out.println("Congratulations " + player2.getName() + ", you have won the game!");
            return true;
        }
        return false;
    }

    private boolean hasPlayerWon(Player player) {
        return boardService.getBearOffForPlayer(player).size() + boardService.getPositions().values().stream()
                .flatMap(List::stream)
                .filter(checker -> checker.getOwner().equals(player))
                .count() == 0;
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

    private void playRoll(Player player, int roll1, int roll2) {
        List<Integer> rolls = new ArrayList<>();
        if (roll1 == roll2) {
            for (int i = 0; i < 4; i++) rolls.add(roll1);
        } else {
            rolls.add(roll1);
            rolls.add(roll2);
        }

        while (!rolls.isEmpty()) {
            List<String> options = generateMoveOptions(player, rolls);
            if (options.isEmpty()) {
                System.out.println("No legal moves available for the current rolls. Turn passes to the next player.");
                break;
            }

            System.out.println("Available move options for current roll:");
            char optionLetter = 'A';
            for (String option : options) {
                System.out.println(optionLetter + ") " + option);
                optionLetter++;
            }

            char selectedOption = getUserSelection();
            boolean successfulMove = executeSelectedOption(selectedOption, options, rolls);

            if (!successfulMove) {
                System.out.println("Invalid selection. Please try again.");
                continue;
            }
            displayGameState();
        }
    }

    private List<String> generateMoveOptions(Player player, List<Integer> rolls) {
        // Check if the player has checkers on the bar
        List<Checker> barCheckers = boardService.getBoard().getBarForPlayer(player);
        if (!barCheckers.isEmpty()) {
            return generateBarEntryMoves(player, rolls); // Only allow bar re-entry moves
        }
        return boardService.getBoard().getLegalMoves(player, rolls); // Regular move generation otherwise
    }

    // Helper method to generate moves for re-entering checkers from the bar
    private List<String> generateBarEntryMoves(Player player, List<Integer> rolls) {
        List<String> barEntryMoves = new ArrayList<>();
        int homeStart = (player == player1) ? 1 : 19;
        int homeEnd = (player == player1) ? 6 : 24;

        for (int roll : rolls) {
            int targetPosition = (player == player1) ? roll : (25 - roll);
            if (targetPosition >= homeStart && targetPosition <= homeEnd) {
                if (boardService.getBoard().canEnterFromBar(player, targetPosition)) {
                    barEntryMoves.add("BAR -> " + targetPosition);
                }
            }
        }
        return barEntryMoves;
    }

    private char getUserSelection() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the option letter: ");
        return scanner.next().toUpperCase().charAt(0);
    }

    private boolean executeSelectedOption(char selectedOption, List<String> options, List<Integer> rolls) {
        int optionIndex = selectedOption - 'A';
        if (optionIndex >= 0 && optionIndex < options.size()) {
            String chosenMove = options.get(optionIndex);
            System.out.println("You chose: " + chosenMove);

            int toPosition;
            int rollUsed;

            if (chosenMove.startsWith("BAR")) {
                // Handling move from the bar
                toPosition = Integer.parseInt(chosenMove.split(" -> ")[1].trim());
                boardService.getBoard().enterFromBar(currentPlayer, toPosition);

                // Calculate rollUsed directly from the destination position for bar entries
                rollUsed = Math.abs((currentPlayer == player1 ? 0 : 25) - toPosition);
            } else if(chosenMove.endsWith("OFF")) {
                // Bear-off handling
                int fromPosition = Integer.parseInt(chosenMove.split(" -> ")[0].trim());
                boardService.getBoard().bearOffChecker(currentPlayer, fromPosition);

                // Calculate rollUsed for bear-off moves
                rollUsed = Math.abs((currentPlayer == player1 ? 0 : 25) - fromPosition);
            } else {
                // Regular move handling
                String[] moveParts = chosenMove.split(" -> ");
                int fromPosition = Integer.parseInt(moveParts[0].trim());
                toPosition = Integer.parseInt(moveParts[1].trim());
                boardService.getBoard().makeMove(currentPlayer, fromPosition, toPosition);

                // Calculate rollUsed for regular moves
                rollUsed = Math.abs(toPosition - fromPosition);
            }

            // Remove the used roll from rolls list
            rolls.remove(Integer.valueOf(rollUsed));

            return true;
        }
        return false;
    }
}