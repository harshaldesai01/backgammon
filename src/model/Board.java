package model;

import java.util.HashMap;
import java.util.Map;

public class Board {
    private final Map<Integer, Checker[]> positions = new HashMap<>();

    public Board() {
        initializeBoard();
    }

    // Initialize the board with checkers in starting positions
    private void initializeBoard() {
        positions.put(1, new Checker[]{new Checker("Black"), new Checker("Black")});
        positions.put(6, new Checker[]{new Checker("White"), new Checker("White"), new Checker("White"), new Checker("White"), new Checker("White")});
        positions.put(8, new Checker[]{new Checker("White"), new Checker("White"), new Checker("White")});
        positions.put(12, new Checker[]{new Checker("Black"), new Checker("Black"), new Checker("Black"), new Checker("Black"), new Checker("Black")});
        positions.put(13, new Checker[]{new Checker("White"), new Checker("White"), new Checker("White"), new Checker("White"), new Checker("White")});
        positions.put(17, new Checker[]{new Checker("Black"), new Checker("Black"), new Checker("Black")});
        positions.put(19, new Checker[]{new Checker("Black"), new Checker("Black"), new Checker("Black"), new Checker("Black"), new Checker("Black")});
        positions.put(24, new Checker[]{new Checker("White"), new Checker("White")});
    }

    // Helper method to get the display symbol for a checker
    private String getCheckerSymbol(Checker checker) {
        return checker.getColor().equals("Black") ? "X" : "O";
    }

    // Display the board with checkers in each position according to Backgammon format
    public void displayBoard() {
        // Top row numbering and separators
        System.out.println("13--+---+---+---+---18 BAR  19--+---+---+---+---24  OFF");

        // Top row checkers display
        for (int i = 13; i <= 18; i++) {
            displayCheckers(i);
        }
        System.out.print(" BAR ");
        for (int i = 19; i <= 24; i++) {
            displayCheckers(i);
        }
        System.out.println("  OFF");

        // Bottom row numbering and separators
        System.out.println("12--+---+---+---+---07 BAR  06--+---+---+---+---01  OFF");

        // Bottom row checkers display
        for (int i = 12; i >= 7; i--) {
            displayCheckers(i);
        }
        System.out.print(" BAR ");
        for (int i = 6; i >= 1; i--) {
            displayCheckers(i);
        }
        System.out.println("  OFF");
    }

    // Display checkers at a specific position
    private void displayCheckers(int position) {
        Checker[] checkers = positions.getOrDefault(position, new Checker[0]);
        if (checkers.length > 0) {
            for (Checker checker : checkers) {
                System.out.print(getCheckerSymbol(checker) + " ");
            }
        } else {
            System.out.print("  ");
        }
    }
}
