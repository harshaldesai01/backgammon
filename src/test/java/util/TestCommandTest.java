package util;

import enums.CommandType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TestCommandTest {

    @Test
    void getFilename() {
        // Create a TestCommand instance with a specific filename
        TestCommand testCommand = new TestCommand(CommandType.TEST, "testfile.txt");

        // Verify that getFilename() returns the correct filename
        assertEquals("testfile.txt", testCommand.getFilename(), "getFilename() should return the correct filename.");
    }
}
