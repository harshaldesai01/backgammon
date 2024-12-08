package model;

/**
 * Represents a player in the Backgammon game.
 */
public class Player {
    private final String name;

    /**
     * Creates a Player with the specified name.
     *
     * @param name the name of the player.
     */
    public Player(String name) {
        this.name = name;
    }

    /**
     * Returns the name of the player.
     *
     * @return the player's name.
     */
    public String getName() {
        return name;
    }
}