package util;

import enums.CommandType;
import exceptions.InvalidCommandException;

import java.util.Scanner;

public class CommandParser {
    private final Scanner scanner = new Scanner(System.in);

    public String getUserInput() {
        return scanner.nextLine().trim();
    }

    public Command parseCommand(String input) throws InvalidCommandException {
        try {
            CommandType type = CommandType.valueOf(input.toUpperCase());
            return new Command(type);
        } catch (IllegalArgumentException e) {
            throw new InvalidCommandException("Invalid command: '" + input + "'. Type 'HINT' for valid commands.");
        }
    }

    public void close() {
        scanner.close();
    }
}
