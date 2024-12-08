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
