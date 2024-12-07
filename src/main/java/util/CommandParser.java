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
        String[] parts = input.split("\\s+"); // Split into all components
        try {
            CommandType type = CommandType.valueOf(parts[0].toUpperCase());

            if (type == CommandType.TEST) {
                if (parts.length != 2) {
                    throw new InvalidCommandException("Invalid TEST command format. Use: test <filename>");
                }
                return new TestCommand(type, parts[1].trim());
            }

            if (type == CommandType.DICE) {
                if (parts.length != 3) {
                    throw new InvalidCommandException("Invalid DICE command format. Use: dice <int> <int>");
                }
                try {
                    int roll1 = Integer.parseInt(parts[1]);
                    int roll2 = Integer.parseInt(parts[2]);
                    return new DiceCommand(type, roll1, roll2);
                } catch (NumberFormatException e) {
                    throw new InvalidCommandException("Dice values must be integers.");
                }
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
