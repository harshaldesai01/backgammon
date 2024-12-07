package controller;

import enums.CommandType;
import exceptions.InvalidCommandException;
import service.GameService;
import service.MatchManager;
import util.Command;
import util.CommandParser;
import util.TestCommand;

import static util.CommonConstants.HORIZONTAL_DIVIDER;
import static util.CommonConstants.QUIT_MESSAGE;
import static util.CommonConstants.WELCOME_MESSAGE;

public class GameController {
    private final CommandParser commandParser;
    private MatchManager matchManager;
    private GameService gameService;

    public GameController() {
        this.commandParser = new CommandParser();
    }

    public void startGame() {
        System.out.println(WELCOME_MESSAGE);

        while (true) {
            setupNewMatch();
            playMatch();

            System.out.print("Would you like to start a new match? (yes/no): ");
            String response = commandParser.getUserInput();
            if (!response.equalsIgnoreCase("yes")) {
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
            System.out.println(HORIZONTAL_DIVIDER);
            playSingleGame();

            if(!matchManager.isMatchOver()) {
                matchManager.incrementGamesPlayed();
            }
        }
    }

    private void playSingleGame() {
        gameService.setUpGame();

        while (!gameService.isGameOver()) {
            gameService.displayGameState();

            String input = commandParser.getUserInput();
            Command command;
            try {
                command = commandParser.parseCommand(input);
            } catch (InvalidCommandException e) {
                System.out.println(e.getMessage());
                continue;
            }

            try {
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
                    case END_GAME -> {
                        handleEndGame();
                        return;
                    }
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
            if(gameService.isGameOver()) {
                handleEndGame();
            }
        }
    }

    private void handleEndGame() {
        System.out.println("Ending current game!");
        updateScoreAndAnnounceWinner(false);
        gameService.setGameOver(true);
        System.out.println(HORIZONTAL_DIVIDER);
    }

    private void handleEndMatch() {
        System.out.println("Ending the current match...");
        updateScoreAndAnnounceWinner(true);
        matchManager.setMatchOver(true);
        System.out.println(HORIZONTAL_DIVIDER);
        System.out.println(HORIZONTAL_DIVIDER);
    }

    private void handleQuit() {
        updateScoreAndAnnounceWinner(true);
        System.out.println(QUIT_MESSAGE);
        commandParser.close();
    }

    private void updateScoreAndAnnounceWinner(boolean matchEnd) {
        gameService.updateScore();
        announceWinner(matchEnd);
    }

    private void announceWinner(boolean matchEnd) {
        String winner = matchManager.getWinnerName();

        if(matchEnd)
            System.out.println("Match Over! "+ (winner.equals("Draw") ? "It's a draw!" : winner+" wins!!"));
        else
            System.out.println("Game Over! " + (winner.equals("Draw") ? "It's a draw!" : winner + " wins the current game!"));
    }

    private void displayHint() {
        System.out.println(HORIZONTAL_DIVIDER);
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