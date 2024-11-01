package util;

import enums.CommandType;
import exceptions.InvalidCommandException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.util.Scanner;

public class CommandParser {
    private final Scanner scanner = new Scanner(System.in);

    public String getUserCommand() {
        return scanner.nextLine();
    }

    public Command parseCommand(String command) throws InvalidCommandException {
        command = command.trim().toUpperCase();
        String[] parts = command.split(" ");
        String commandPart = parts[0];

        // Map shortcuts to actual commands
        commandPart = switch (commandPart) {
            case "Q" -> "QUIT";
            case "R" -> "ROLL";
            case "H" -> "HINT";
            default -> commandPart;
        };


        try {
            CommandType type = CommandType.valueOf(commandPart);

            // This approach creates a list from the array directly, then uses subList() to skip the first element (the command part).
            List<String> parameters = Arrays.asList(parts).subList(1, parts.length);


            // Validate parameters based on command type
            validateParameters(type, parameters);

            return new Command(type, parameters);
        } catch (IllegalArgumentException e) {
            throw new InvalidCommandException("Invalid command: '" + command + "'. Type 'HINT' for valid commands.");
        }
    }

    private void validateParameters(CommandType type, List<String> parameters) throws InvalidCommandException {
        switch (type) {
            case ROLL:
                // ROLL should not have any parameters
                if (!parameters.isEmpty()) {
                    throw new InvalidCommandException("ROLL command does not accept parameters.");
                }
                break;

            case MOVE:
                // MOVE command validation for two positions
                if (parameters.size() != 2) {
                    throw new InvalidCommandException("MOVE command requires exactly two numeric parameters. Example: 'MOVE 5 8'.");
                }
                try {
                    int from = Integer.parseInt(parameters.get(0));
                    int to = Integer.parseInt(parameters.get(1));
                    // Additional logic to check if from and to are within valid bounds
                    if (from < 1 || from > 24 || to < 1 || to > 24) {
                        throw new InvalidCommandException("Position values must be between 1 and 24.");
                    }
                } catch (NumberFormatException e) {
                    throw new InvalidCommandException("MOVE parameters must be numeric.");
                }
                break;

            // Add cases for other commands that may require specific validation
            default:
                if (!parameters.isEmpty()) {
                    throw new InvalidCommandException(type + " command does not accept parameters.");
                }
                break;
        }
    }





    public List<String> getAvailableCommands() {
        List<String> commands = new ArrayList<>();
        for (CommandType type : CommandType.values()) {
            commands.add(type.name());
        }
        return commands;
    }

    public void displayError(String message) {
        System.out.println("Error: " + message);
    }

    public void close() {
        scanner.close();
    }
}