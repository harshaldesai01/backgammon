package controller;

import enums.CommandType;
import exceptions.InvalidCommandException;
import exceptions.InvalidMoveException;
import model.Player;
import service.GameService;
import service.MatchManager;
import util.Command;
import util.CommandParser;

import static util.CommonConstants.*;

public class GameController {
    private final CommandParser commandParser;
    private MatchManager matchManager;
    private GameService gameService;

    public GameController() {
        this.commandParser = new CommandParser();
    }

    public void startGame() {
        System.out.println(WELCOME_MESSAGE);

        setupNewMatch();

        while (!matchManager.isGameOver()) {
            playMatch();

            if(matchManager.isGameOver()) {
                System.out.println(GAME_OVER_MESSAGE);
                break;
            }

            System.out.println("Match Over! " + matchManager.getWinnerName() + " wins the match!");

            System.out.print("Would you like to start a new match? (yes/no): ");
            String response = commandParser.getUserInput().toLowerCase();

            if (response.equalsIgnoreCase("yes")) {
                setupNewMatch();
            } else {
                matchManager.setGameOver(true);
                System.out.println("Thank you for playing Backgammon!");
            }
        }
        commandParser.close();
    }

    private void setupNewMatch() {
        if (matchManager != null && matchManager.isMatchOver()) {
            matchManager.completeMatch();
        }

        if (matchManager == null || !matchManager.isGameOver()) {
            System.out.print("Enter Player 1 name: ");
            String name1 = commandParser.getUserInput();
            System.out.print("Enter Player 2 name: ");
            String name2 = commandParser.getUserInput();
            System.out.print("Enter the match length (e.g., 3, 5, 7): ");
            int matchLength = getMatchLengthInputFromUser();

            matchManager = new MatchManager(name1, name2, matchLength);
            gameService = new GameService(matchManager);
        }
    }

    private int getMatchLengthInputFromUser() {
        int matchLength;
        while (true) {
            try {
                System.out.print("Enter the match length (e.g., 3, 5, 7): ");
                matchLength = Integer.parseInt(commandParser.getUserInput());
                if (matchLength > 0) {
                    return matchLength;
                } else {
                    System.out.println("Match length must be a positive number.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
            }
        }
    }

    private void playMatch() {
        while (!matchManager.isMatchOver()) {
            playSingleGame();
            // Update and display scores after each game
            gameService.updateScore();
            displayMatchProgress();
        }
        matchManager.completeMatch();
    }

    private void displayMatchProgress() {
        System.out.println("Match Progress:");
        System.out.printf("%s: %d points\n", matchManager.getPlayer1().getName(), matchManager.getPlayer1Score());
        System.out.printf("%s: %d points\n", matchManager.getPlayer2().getName(), matchManager.getPlayer2Score());
    }

    private void playSingleGame() {
        gameService.setUpGame();

        while (!matchManager.isMatchOver()) {
            try {
                if (matchManager.isGameOver()) {
                    return;
                }

                gameService.displayGameState();
                String input = commandParser.getUserInput();
                Command command = commandParser.parseCommand(input);

                processCommand(command);

                if (gameService.isGameOver()) {
                    handleGameOver();
                    break;
                }
            } catch (InvalidCommandException | InvalidMoveException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void handleGameOver() {
        gameService.displayGameState();
        System.out.println(GAME_OVER_MESSAGE);
    }

    private void processCommand(Command command) {
        switch (command.getType()) {
            case QUIT -> {
                System.out.println(QUIT_MESSAGE);
                System.exit(0);
            }
            case HINT -> displayHint();
            case END_MATCH -> handleEndMatch();
            case DOUBLE ->gameService.offerDouble();
            case ACCEPT, REFUSE -> System.out.println("Invalid action: Use the DOUBLE command to initiate this process.");
            default -> gameService.executeCommand(command.getType());
        }
    }


    private void handleEndMatch() {
        System.out.println("Ending the current match...");
        matchManager.setMatchOver(true); // Forcefully end the match
        Player highestScorer = matchManager.getPlayer1Score() > matchManager.getPlayer2Score()
                ? matchManager.getPlayer1()
                : matchManager.getPlayer2();
        matchManager.incrementScore(highestScorer, SINGLE);
        System.out.println("Match ended early. " + highestScorer.getName() + " is awarded 1 point!");
    }

    private void displayHint() {
        System.out.println("Available commands:");
        for (CommandType type : CommandType.values()) {
            System.out.println("- " + type.name());
        }
    }
}