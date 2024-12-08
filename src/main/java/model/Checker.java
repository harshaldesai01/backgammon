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

/**
 * Represents a single checker on the Backgammon board, with its associated color and owner.
 */
public class Checker {
    private final String color;
    private final Player owner;

    /**
     * Creates a Checker with the specified color and owner.
     *
     * @param color the color of the checker.
     * @param owner the player who owns the checker.
     */
    public Checker(String color, Player owner) {
        this.color = color;
        this.owner = owner;
    }

    /**
     * Returns the color of the checker.
     *
     * @return the color of the checker.
     */
    public String getColor() {
        return color;
    }

    /**
     * Returns the owner of this checker.
     *
     * @return the owner of the checker.
     */
    public Player getOwner() {
        return owner;
    }
}
