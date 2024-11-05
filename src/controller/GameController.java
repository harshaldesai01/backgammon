package controller;

import enums.CommandType;
import exceptions.InvalidCommandException;
import exceptions.InvalidMoveException;
import service.GameService;
import util.Command;
import util.CommandParser;

import static util.CommonConstants.*;

/**
 * Class to control the game flow.
 */
public class GameController {
    private final GameService gameService;
    private final CommandParser commandParser;

    public GameController() {
        this.commandParser = new CommandParser();
        this.gameService = new GameService();
    }

    /**
     * Method containing logic for the game flow. Takes input from the user & executes the command.
     */
    public void startGame() {
        System.out.println(WELCOME_MESSAGE);

        // Collect player names and set up players in GameService
        System.out.print("Enter Player 1 name: ");
        String name1 = commandParser.getUserCommand();
        System.out.print("Enter Player 2 name: ");
        String name2 = commandParser.getUserCommand();
        gameService.setUpPlayers(name1, name2);

        gameService.determineStartingPlayer();

        // Main game loop
        while (true) {
            try {
                // Display game state
                gameService.displayGameState();

                // Parse and execute user command
                Command command = commandParser.parseCommand(commandParser.getUserCommand());
                if (command.getType() == CommandType.QUIT) {
                    System.out.println(QUIT_MESSAGE);
                    break;
                }
                // Check for the HINT command
                else if (command.getType() == CommandType.HINT) {
                    displayHint();
                    continue; // Go to the next iteration after showing the hint
                }

                // Execute the command in the game service
                gameService.executeCommand(command.getType());

                // Check if game is over
                if (gameService.isGameOver()) {
                    gameService.displayGameState();
                    System.out.println(GAME_WON_MESSAGE);
                    break;
                }

            } catch (InvalidCommandException e) {
                commandParser.displayError(e.getMessage());
            } catch (InvalidMoveException e) {
                System.out.println("Invalid move: " + e.getMessage());
            }
        }

        // Close resources
        commandParser.close();
    }

    /**
     * Displays a list of available commands.
     */
    private void displayHint() {
        System.out.println("Available commands:");
        for (String command : commandParser.getAvailableCommands()) {
            System.out.println("- " + command);
        }
    }


}