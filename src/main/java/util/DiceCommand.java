package util;

import enums.CommandType;
import exceptions.InvalidCommandException;

public class DiceCommand extends Command {
    private final int roll1;
    private final int roll2;

    public DiceCommand(CommandType type, int roll1, int roll2) {
        super(type);
        if (roll1 < 1 || roll1 > 6 || roll2 < 1 || roll2 > 6) {
            throw new InvalidCommandException("Dice values must be between 1 and 6.");
        }
        this.roll1 = roll1;
        this.roll2 = roll2;
    }


    public int getRoll1() {
        return roll1;
    }

    public int getRoll2() {
        return roll2;
    }
}

