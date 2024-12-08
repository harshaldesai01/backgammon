package util;

import enums.CommandType;

/**
 * Represents a generic command issued by a player.
 */
public class Command {
    private final CommandType type;

    /**
     * Constructs a Command with the specified type.
     *
     * @param type the type of command.
     */
    public Command(CommandType type) {
        this.type = type;
    }

    /**
     * Retrieves the type of this command.
     *
     * @return the command type.
     */
    public CommandType getType() {
        return type;
    }
}
