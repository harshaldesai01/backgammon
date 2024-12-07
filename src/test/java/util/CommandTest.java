package util;

import enums.CommandType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommandTest {

    @Test
    void getType() {
        // Create a Command instance with a specific CommandType
        Command command = new Command(CommandType.ROLL);

        // Verify that getType() returns the correct CommandType
        assertEquals(CommandType.ROLL, command.getType(), "getType() should return the correct command type.");
    }
}
