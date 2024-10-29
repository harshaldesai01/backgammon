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
        positions.put(1, createCheckers("White", 2));
        positions.put(6, createCheckers("Black", 5));
        positions.put(8, createCheckers("Black", 3));
        positions.put(12, createCheckers("White", 5));
        positions.put(13, createCheckers("Black", 5));
        positions.put(17, createCheckers("White", 3));
        positions.put(19, createCheckers("White", 5));
        positions.put(24, createCheckers("Black", 2));
    }

    //added to simplify the checker create logic
    private Checker[] createCheckers(String color, int count) {
        Checker[] checkers = new Checker[count];
        for (int i = 0; i < count; i++) {
            checkers[i] = new Checker(color);
        }
        return checkers;
    }

    // Helper method to get the display symbol for a checker
    private String getCheckerSymbol(Checker checker) {
        return checker.getColor().equals("Black") ? "X" : "O";
    }

    // Display the board with checkers in each position according to Backgammon format
    public void displayBoard() {
        int maxCheckers = getMaxCheckersInPosition();
        // Top row numbering and separators
        System.out.println("13--+---+---+---+---18  BAR  19--+---+---+---+---24  OFF");

        // Top row checkers display
        for (int row = 0; row < maxCheckers; row++) {
            // Positions 13 to 18
            for (int i = 13; i <= 18; i++) {
                displayCheckers(i, row);
            }
            System.out.print("   BAR");
            // Positions 19 to 24
            for (int i = 19; i <= 24; i++) {
                displayCheckers(i, row);
            }
            System.out.println("  OFF");
        }

        // Bottom row numbering and separators
        System.out.println("12--+---+---+---+---07  BAR  06--+---+---+---+---01  OFF");

        // Bottom row checkers display
        for (int row = maxCheckers - 1; row >= 0; row--) {
            // Positions 12 to 7
            for (int i = 12; i >= 7; i--) {
                displayCheckers(i, row);
            }
            System.out.print("   BAR");
            // Positions 6 to 1
            for (int i = 6; i >= 1; i--) {
                displayCheckers(i, row);
            }

            System.out.println("  OFF");
        }
    }

    //find the vertical print limit - i.e  Get the maximum number of checkers in any position
    private int getMaxCheckersInPosition() {
        int max = 0;
        for (Checker[] checkers : positions.values()) {
            if (checkers.length > max) {
                max = checkers.length;
            }
        }
        return max;
    }

    // Display checkers at a specific position
    private void displayCheckers(int position, int row) {
        Checker[] checkers = positions.getOrDefault(position, new Checker[0]);
        if (checkers.length > row) {
            System.out.print(" " + getCheckerSymbol(checkers[row]) + "  ");
        } else if(position==18 || position==7){
            System.out.print(" ");
        }else {
            System.out.print("    ");
        }
    }
}
