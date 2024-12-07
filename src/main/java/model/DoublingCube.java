package model;

public class DoublingCube {
    private int value;
    private Player owner;

    public DoublingCube() {
        this.value = 1;
        this.owner = null;
    }

    public int getValue() {
        return value;
    }

    public Player getOwner() {
        return owner;
    }

    public void doubleValue(Player newOwner) {
        value *= 2;
        owner = newOwner;
    }

    public void reset() {
        value = 1;
        owner = null;
    }
}