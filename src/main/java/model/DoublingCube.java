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
        if (value < 64) {
            value *= 2;
            owner = newOwner;
        } else {
            System.out.println("Maximum cube value reached.");
        }
    }

    public void reset() {
        value = 1;
        owner = null;
    }
}