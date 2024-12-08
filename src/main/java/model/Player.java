/**
 * This file is part of the Backgammon game project developed by the Dice Bros - Group 5 team.
 *
 * Team Information:
 * Team Name: Dice Bros - Group 5
 * Student Names:
 *   - Harshal Desai
 *   - Alparslan Balci
 *   - Manish Tawade
 * GitHub IDs:
 *   - harshaldesai01
 *   - Apistomeister
 *   - Manish9881
 */

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