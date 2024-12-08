package util;

import enums.CommandType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TestCommandTest {

    @Test
    void testGetFilename() {
        String expectedFilename = "testfile.txt";
        TestCommand testCommand = new TestCommand(CommandType.TEST, expectedFilename);

        String filename = testCommand.getFilename();

        assertEquals(expectedFilename, filename, "The filename should match the expected value.");
    }

    @Test
    void testCommandTypeIsTest() {
        TestCommand testCommand = new TestCommand(CommandType.TEST, "testfile.txt");

        CommandType type = testCommand.getType();

        assertEquals(CommandType.TEST, type, "The command type should be TEST.");
    }
}
