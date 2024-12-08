package model;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return Objects.equals(name, player.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}