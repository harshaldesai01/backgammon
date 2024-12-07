package model;

public class Checker {
    private String color;
    private final Player owner;

    public Checker(String color, Player owner) {
        this.color = color;
        this.owner = owner;
    }

    public String getColor() {
        return color;
    }

    /**
     * Returns the owner of this checker.
     */
    public Player getOwner() {
        return owner;
    }
}
