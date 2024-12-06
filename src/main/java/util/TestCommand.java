package util;
import enums.CommandType;

public class TestCommand extends Command {
    private final String filename;

    public TestCommand(CommandType type, String filename) {
        super(type);
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }
}
