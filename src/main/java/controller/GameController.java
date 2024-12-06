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

        while (true) {
            playMatch();

            System.out.println("Match Over! " + matchManager.getWinnerName() + " wins the match!");

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
        if (matchManager != null) {
            Player highestScorer = matchManager.getPlayer1Score() > matchManager.getPlayer2Score()
                    ? matchManager.getPlayer1()
                    : matchManager.getPlayer2();
            matchManager.incrementScore(highestScorer, SINGLE);
            System.out.println("Current match ended. " + highestScorer.getName() + " is awarded 1 point!");
        }

        System.out.print("Enter Player 1 name: ");
        String name1 = commandParser.getUserInput();
        System.out.print("Enter Player 2 name: ");
        String name2 = commandParser.getUserInput();
        System.out.print("Enter the match length (e.g., 3, 5, 7): ");
        int matchLength = Integer.parseInt(commandParser.getUserInput());

        matchManager = new MatchManager(name1, name2, matchLength);
        gameService = new GameService(matchManager);
    }

    private void playMatch() {
        while (!matchManager.isMatchOver()) {
            playSingleGame();
            gameService.updateScore();
        }
    }

    private void playSingleGame() {
        gameService.setUpGame();

        while (true) {
            try {
                if (matchManager.isGameOver()) {
                    matchManager.setGameOver(false);
                    break;
                }

                gameService.displayGameState();

                String input = commandParser.getUserInput();
                Command command = commandParser.parseCommand(input);

                switch (command.getType()) {
                    case QUIT:
                        System.out.println(QUIT_MESSAGE);
                        return;
                    case HINT:
                        displayHint();
                        break;
                    case END_MATCH:
                        System.out.println("Ending the current match...");
                        handleEndMatch();
                        return;
                    case DOUBLE:
                        gameService.offerDouble();
                        break;
                    case ACCEPT:
                        gameService.acceptDouble();
                        break;
                    case REFUSE:
                        gameService.refuseDouble();
                        break;
                    default:
                        gameService.executeCommand(command);
                }

                if (gameService.isGameOver()) {
                    gameService.displayGameState();
                    gameService.updateScore(); // Now includes doubling cube value
                    System.out.println(GAME_WON_MESSAGE);
                    break;
                }
            } catch (InvalidCommandException | InvalidMoveException e) {
                System.out.println(e.getMessage());
            }
        }
    }


    private void handleEndMatch() {
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