/**
 * This file is part of the Backgammon game project developed by the Dice Bros - Group 5 team.
 *
 * Team Information:
 * Team Name: Dice Bros - Group 5
 * Student Names:
 *   - Harshal Desai
 *   - Alparslan Balci
 *   - Manish Tawade
 * GitHub IDs:
 *   - harshaldesai01
 *   - Apistomeister
 *   - Manish9881
 */

package controller;

import enums.CommandType;
import exceptions.InvalidCommandException;
import service.GameService;
import service.MatchManager;
import util.Command;
import util.CommandParser;
import util.TestCommand;

import static util.CommonConstants.*;

/**
 * Controls the overall flow of the Backgammon game, including match setup, game play, and game termination.
 */
public class GameController {
    private final CommandParser commandParser;
    private MatchManager matchManager;
    private GameService gameService;
    private boolean quit = false;

    /**
     * Initializes the GameController with necessary utilities such as the CommandParser.
     */
    public GameController() {
        this.commandParser = new CommandParser();
    }
    public GameController(CommandParser cd) {
        this.commandParser = cd;
    }

    /**
     * Starts the game, manages matches, and facilitates user interactions.
     */
    public void startGame() {
        System.out.println(WELCOME_MESSAGE);

        while (!quit) {
            setupNewMatch();
            playMatch();

            if(quit) {
                break;
            }

            System.out.print(NEW_MATCH_PROMPT);
            String response = commandParser.getUserInput();
            if (!response.equalsIgnoreCase("yes")) {
                System.out.println(FINAL_THANK_YOU_MESSAGE);
                commandParser.close();
                break;
            }
        }
    }

    /**
     * Sets up a new match by collecting player names and match length from the user.
     */
    private void setupNewMatch() {
        String name1  = getPlayerName("Enter Player 1 name: ");
        String name2 = getPlayerName("Enter Player 2 name: ");
        int matchLength = getValidMatchLength();

        matchManager = new MatchManager(name1, name2, matchLength);
        gameService = new GameService(matchManager);
    }

    /**
     * Manages the match gameplay until the match is completed.
     */
    private void playMatch() {
        while (!matchManager.isMatchOver() && !quit) {
            System.out.println(HORIZONTAL_DIVIDER);
            playSingleGame();

            if(!matchManager.isMatchOver() && !quit) {
                matchManager.incrementGamesPlayed();
            }
        }
    }

    /**
     * Plays a single game within the match, handling player inputs and game state transitions.
     */
    private void playSingleGame() {
        gameService.setUpGame();

        while (!gameService.isGameOver() && !quit) {
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
                        return;
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
                    case ACCEPT, REFUSE -> throw new InvalidCommandException(DOUBLING_COMMANDS_ERROR_MESSAGE);
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

    /**
     * Ends the current game, updates scores, and announces the game winner.
     */
    private void handleEndGame() {
        System.out.println(GAME_END_MESSAGE);
        updateScoreAndAnnounceWinner(false);
        gameService.setGameOver(true);
        System.out.println(HORIZONTAL_DIVIDER);
    }

    /**
     * Ends the match, updates scores, and announces the match winner.
     */
    private void handleEndMatch() {
        System.out.println(MATCH_END_MESSAGE);
        updateScoreAndAnnounceWinner(true);
        matchManager.setMatchOver(true);
        System.out.println(HORIZONTAL_DIVIDER);
        System.out.println(HORIZONTAL_DIVIDER);
    }

    /**
     * Quits the game, updates scores, and displays a farewell message.
     */
    private void handleQuit() {
        updateScoreAndAnnounceWinner(true);
        System.out.println(QUIT_MESSAGE);
        quit = true;
        commandParser.close();
    }

    /**
     * Updates the score and announces the winner of the game or match.
     *
     * @param matchEnd true if the match is ending, false if a single game is ending.
     */
    private void updateScoreAndAnnounceWinner(boolean matchEnd) {
        gameService.updateScore();
        announceWinner(matchEnd);
    }

    /**
     * Announces the winner of the game or match.
     *
     * @param matchEnd true if the match is ending, false if a single game is ending.
     */
    private void announceWinner(boolean matchEnd) {
        String winner = matchManager.getWinnerName();
        if(matchEnd)
            System.out.println(MATCH_OVER_MESSAGE+ (winner.equals(DRAW_CONDITION) ? "It's a draw!" : winner+" wins!!"));
        else
            System.out.println(GAME_OVER_MESSAGE + (winner.equals(DRAW_CONDITION) ? "It's a draw!" : winner + " wins the current game!"));
    }

    /**
     * Displays a list of available commands to the player.
     */
    private void displayHint() {
        System.out.println(HORIZONTAL_DIVIDER);
        System.out.println("Available commands:");
        for (CommandType type : CommandType.values()) {
            System.out.println("- " + type.name());
        }

    }

    /**
     * Prompts the user for a player name until a non-empty name is provided.
     *
     * @param prompt the prompt message to display to the user.
     * @return the valid player name entered by the user.
     */
    private String getPlayerName(String prompt) {
        while (true) {
            System.out.print(prompt);
            String name = commandParser.getUserInput();
            if (!name.isEmpty()) {
                return name;
            }
            System.out.println(EMPTY_PLAYER_NAME_MESSAGE);
        }
    }

    /**
     * Prompts the user for a valid match length (positive integer).
     *
     * @return the valid match length entered by the user.
     */
    private int getValidMatchLength() {
        while (true) {
            System.out.print(MATCH_LENGTH_INPUT_PROMPT);
            String input = commandParser.getUserInput();
            try {
                int matchLength = Integer.parseInt(input);
                if (matchLength > 0) {
                    return matchLength;
                } else {
                    System.out.println(POSITIVE_MATCH_LENGTH_MESSAGE);
                }
            } catch (NumberFormatException e) {
                System.out.println(INVALID_MATCH_LENGTH_INPUT_MESSAGE);
            }
        }
    }
}