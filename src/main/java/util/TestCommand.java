package util;
import enums.CommandType;

/**
 * Represents a command for testing game functionality using a specific file.
 */
public class TestCommand extends Command {
    private final String filename;

    /**
     * Constructs a TestCommand with the specified filename.
     *
     * @param type     the type of command.
     * @param filename the name of the test file.
     */
    public TestCommand(CommandType type, String filename) {
        super(type);
        this.filename = filename;
    }

    /**
     * Retrieves the name of the test file associated with this command.
     *
     * @return the test file name.
     */
    public String getFilename() {
        return filename;
    }
}
