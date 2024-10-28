package util;

import enums.CommandType;
import exceptions.InvalidCommandException;

import java.util.Scanner;

public class CommandParser {
    private final Scanner scanner = new Scanner(System.in);

    public String getUserCommand() {
        return scanner.nextLine();
    }

    public Command parseCommand(String input) throws InvalidCommandException {
        try {
            CommandType type = CommandType.valueOf(input.toUpperCase());
            return new Command(type);
        } catch (IllegalArgumentException e) {
            throw new InvalidCommandException("Invalid command: " + input);
        }
    }

    public void displayError(String message) {
        System.out.println("Error: " + message);
    }

    public void close() {
        scanner.close();
    }
}