package util;

import enums.CommandType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommandTest {

    @Test
    void getType() {
        Command command = new Command(CommandType.ROLL);

        CommandType type = command.getType();

        assertEquals(CommandType.ROLL, type, "The type of the command should be ROLL.");
    }
}
