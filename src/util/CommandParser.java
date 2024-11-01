package util;

import enums.CommandType;
import exceptions.InvalidCommandException;

import java.util.ArrayList;
import java.util.List;

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