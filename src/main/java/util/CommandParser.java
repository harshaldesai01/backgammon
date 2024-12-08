package util;

import enums.CommandType;
import exceptions.InvalidCommandException;

import java.util.Scanner;

/**
 * Parses user input into game commands and handles invalid input.
 */
public class CommandParser {
    private final Scanner scanner = new Scanner(System.in);

    /**
     * Retrieves input from the user.
     *
     * @return the trimmed input string.
     */
    public String getUserInput() {
        return scanner.nextLine().trim();
    }

    /**
     * Parses a user input string into a Command object.
     *
     * @param input the input string.
     * @return the parsed Command object.
     * @throws InvalidCommandException if the input is invalid or unrecognized.
     */
    public Command parseCommand(String input) throws InvalidCommandException {
        String[] parts = input.split("\\s+");
        try {
            CommandType type = CommandType.valueOf(parts[0].toUpperCase());

            if (type == CommandType.TEST) {
                if (parts.length != 2) {
                    throw new InvalidCommandException("Invalid TEST command format. Use: test <filename>");
                }
                return new TestCommand(type, parts[1].trim());
            }

            if(type == CommandType.DICE) {
                if (parts.length != 3) {
                    throw new InvalidCommandException("Invalid DICE command format. Use: dice <int> <int>");
                }
                try {
                    int roll1 = Integer.parseInt(parts[1].trim());
                    int roll2 = Integer.parseInt(parts[2].trim());
                    return new DiceCommand(type, roll1, roll2);
                } catch (NumberFormatException e) {
                    throw new InvalidCommandException("Invalid DICE command format. Use: dice <int> <int>");
                }
            }

            return new Command(type);
        } catch (IllegalArgumentException e) {
            throw new InvalidCommandException("Invalid command: '" + input + "'. Type 'HINT' for valid commands.");
        }
    }

    /**
     * Closes the input scanner.
     */
    public void close() {
        scanner.close();
    }
}