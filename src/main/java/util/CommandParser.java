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
        String[] parts = input.split("\\s+");
        try {
            CommandType type = CommandType.valueOf(parts[0].toUpperCase());

            if (type == CommandType.TEST) {
                if (parts.length != 2) {
                    throw new InvalidCommandException("Invalid TEST command format. Use: test <filename>");
                }
                return new TestCommand(type, parts[1].trim());
            }

            return new Command(type);
        } catch (IllegalArgumentException e) {
            throw new InvalidCommandException("Invalid command: '" + input + "'. Type 'HINT' for valid commands.");
        }
    }

    public void close() {
        scanner.close();
    }
}