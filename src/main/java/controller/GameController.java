package controller;

import enums.CommandType;
import exceptions.InvalidCommandException;
import service.GameService;
import service.MatchManager;
import util.Command;
import util.CommandParser;
import util.TestCommand;

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

        while (true) {
            playMatch();

            System.out.print("Would you like to start a new match? (yes/no): ");
            String response = commandParser.getUserInput().toLowerCase();
            if (response.equals("yes")) {
                setupNewMatch();
            } else {
                System.out.println("Thank you for playing Backgammon!");
                commandParser.close();
                break;
            }
        }
    }

    private void setupNewMatch() {
        String name1  = getPlayerName("Enter Player 1 name: ");
        String name2 = getPlayerName("Enter Player 2 name: ");
        int matchLength = getValidMatchLength();

        matchManager = new MatchManager(name1, name2, matchLength);
        gameService = new GameService(matchManager);
    }

    private void playMatch() {
        while (!matchManager.isMatchOver()) {
            playSingleGame();
            matchManager.incrementGamesPlayed();
            gameService.updateScore();
        }

        announceMatchWinner();
    }

    private void playSingleGame() {
        gameService.setUpGame();

        while (true) {
            try {
                if (gameService.isGameOver() || matchManager.isGameOver()) {
                    break;
                }

                gameService.displayGameState();

                String input = commandParser.getUserInput();
                Command command = commandParser.parseCommand(input);

                if (command instanceof TestCommand testCommand) {
                    gameService.executeCommand(testCommand);
                    continue;
                }

                switch (command.getType()) {
                    case QUIT -> {
                        handleQuit();
                        System.exit(0);
                    }
                    case HINT -> displayHint();
                    case END_MATCH -> {
                        handleEndMatch();
                        return;
                    }
                    case DOUBLE -> gameService.offerDouble();
                    case ACCEPT, REFUSE -> throw new InvalidCommandException("This command can only be used in case of a double offer!");
                    default -> gameService.executeCommand(command);
                }
            } catch (InvalidCommandException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void handleQuit() {
        announceMatchWinner();
        System.out.println(QUIT_MESSAGE);
        commandParser.close();
    }

    private void handleEndMatch() {
        System.out.println("Ending the current match...");
        announceMatchWinner();
        matchManager.resetMatch();
        matchManager.setMatchOver(true);
    }

    private void announceMatchWinner() {
        String winner = matchManager.getWinnerName();
        System.out.println("Match Over! " + (winner.equals("Draw") ? "It's a draw!" : winner + " wins the match!"));
    }

    private void displayHint() {
        System.out.println("Available commands:");
        for (CommandType type : CommandType.values()) {
            System.out.println("- " + type.name());
        }
    }

    private String getPlayerName(String prompt) {
        while (true) {
            System.out.print(prompt);
            String name = commandParser.getUserInput();
            if (!name.isEmpty()) {
                return name;
            }
            System.out.println("Player name cannot be empty. Please try again.");
        }
    }

    private int getValidMatchLength() {
        while (true) {
            System.out.print("Enter the match length (e.g., 3, 5, 7): ");
            String input = commandParser.getUserInput();
            try {
                int matchLength = Integer.parseInt(input);
                if (matchLength > 0) {
                    return matchLength;
                } else {
                    System.out.println("Match length must be a positive integer. Try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number for match length.");
            }
        }
    }
}