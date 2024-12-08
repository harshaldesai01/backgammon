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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CheckerTest {

    @Test
    void testGetColor() {
        Player owner = new Player("Alice");
        String color = "White";
        Checker checker = new Checker(color, owner);

        String result = checker.getColor();

        assertEquals(color, result, "Checker color should match the value passed to the constructor.");
    }

    @Test
    void testGetOwner() {
        Player owner = new Player("Bob");
        String color = "Black";
        Checker checker = new Checker(color, owner);

        Player result = checker.getOwner();

        assertEquals(owner, result, "Checker owner should match the value passed to the constructor.");
    }
}
