package model;

/**
 * Represents the doubling cube in Backgammon, which tracks the doubling value and its owner.
 */
public class DoublingCube {
    private int value;
    private Player owner;

    /**
     * Creates a new DoublingCube with the default value of 1 and no owner.
     */
    public DoublingCube() {
        this.value = 1;
        this.owner = null;
    }

    /**
     * Returns the current value of the doubling cube.
     *
     * @return the value of the doubling cube.
     */
    public int getValue() {
        return value;
    }

    /**
     * Returns the current owner of the doubling cube.
     *
     * @return the owner of the doubling cube, or null if there is no owner.
     */
    public Player getOwner() {
        return owner;
    }

    /**
     * Doubles the value of the cube and assigns a new owner.
     *
     * @param newOwner the new owner of the doubling cube.
     */
    public void doubleValue(Player newOwner) {
        value *= 2;
        owner = newOwner;
    }

    /**
     * Resets the doubling cube to its default state (value = 1, no owner).
     */
    public void reset() {
        value = 1;
        owner = null;
    }
}