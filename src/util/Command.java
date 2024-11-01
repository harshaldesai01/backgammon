package util;

import enums.CommandType;
import java.util.List;

public class Command {
    private final CommandType type;
    private final List<String> parameters;

    // Constructor to initialize command with type and parameters
    public Command(CommandType type, List<String> parameters) {
        this.type = type;
        this.parameters = parameters;
    }

    // Overloaded constructor for commands with no parameters
    public Command(CommandType type) {
        this(type, List.of()); // Use an empty list for commands without parameters
    }

    // Getter for command type
    public CommandType getType() {
        return type;
    }

    // Getter for parameters
    public List<String> getParameters() {
        return parameters;
    }
}
