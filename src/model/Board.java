package model;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

public class Board {
    private final Map<Integer, List<Checker>> positions = new HashMap<>();
    private final Player player1;
    private final Player player2;
    private final List<Checker> barPlayer1 = new ArrayList<>();
    private final List<Checker> barPlayer2 = new ArrayList<>();
    private final List<Checker> bearOffPlayer1 = new ArrayList<>();
    private final List<Checker> bearOffPlayer2 = new ArrayList<>();

    public Board(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
        initializeBoard();
    }

    public void displayBoard() {
        int maxCheckers = getMaxCheckersInPosition();

        System.out.println("Backgammon Board:");
        System.out.println("----------------------------------------------------------------------");

        // Top side (points 13 to 24)
        System.out.print("|");
        for (int i = 13; i <= 24; i++) {
            System.out.printf(" %2d |", i);
        }
        // Now print 'OFF' column header for Player 2
        System.out.print(" OFF |");
        System.out.println();
        System.out.println("-------------------------------------------------------------------");

        for (int row = 0; row < maxCheckers; row++) {
            System.out.print("|");
            for (int point = 13; point <= 24; point++) {
                List<Checker> checkers = positions.getOrDefault(point, new ArrayList<>());
                if (row < checkers.size()) {
                    System.out.printf("%3s", checkers.get(row).getOwner() == player1 ? "O" : "X");
                } else {
                    System.out.print("   ");
                }
                System.out.print(" |");
            }
            // Now print the bear-off checkers for Player 2
            if (row < bearOffPlayer2.size()) {
                System.out.print("  X  |"); // Player 2's bear-off checkers
            } else {
                System.out.print("     |");
            }
            System.out.println();
        }

        // Middle bar
        System.out.println("------------------------------- BAR -------------------------------");
        System.out.print("Player 1 Bar: ");
        barPlayer1.forEach(c -> System.out.print("O "));
        System.out.print("\nPlayer 2 Bar: ");
        barPlayer2.forEach(c -> System.out.print("X "));
        System.out.println();
        System.out.println("------------------------------- BAR -------------------------------");

        // Bottom side (points 12 to 1)
        System.out.print("|");
        for (int i = 12; i >= 1; i--) {
            System.out.printf(" %2d |", i);
        }
        // Now print 'OFF' column header for Player 1
        System.out.print(" OFF |");
        System.out.println();
        System.out.println("-------------------------------------------------------------------");

        for (int row = 0; row < maxCheckers; row++) {
            System.out.print("|");
            for (int point = 12; point >= 1; point--) {
                List<Checker> checkers = positions.getOrDefault(point, new ArrayList<>());
                if (row > maxCheckers-checkers.size()-1) {
                    System.out.printf("%3s", checkers.get(maxCheckers-1-row).getOwner() == player1 ? "O" : "X");
                } else {
                    System.out.print("   ");
                }
                System.out.print(" |");
            }
            // Now print the bear-off checkers for Player 1
            if (row < bearOffPlayer1.size()) {
                System.out.print("  O  |"); // Player 1's bear-off checkers
            } else {
                System.out.print("     |");
            }
            System.out.println();
        }

        System.out.println("----------------------------------------------------------------------");

        // Display bar separately if needed

        System.out.println();
    }

    private int getMaxCheckersInPosition() {
        int max = 0;
        for (List<Checker> checkers : positions.values()) {
            if (checkers.size() > max) {
                max = checkers.size();
            }
        }
        return max;
    }

    // Initialize the board with checkers owned by specific players
    private void initializeBoard() {
        positions.put(1, createCheckers("White", 2, player1));
        positions.put(6, createCheckers("Black", 5, player2));
        positions.put(8, createCheckers("Black", 3, player2));
        positions.put(12, createCheckers("White", 5, player1));
        positions.put(13, createCheckers("Black", 5, player2));
        positions.put(17, createCheckers("White", 3, player1));
        positions.put(19, createCheckers("White", 5, player1));
        positions.put(24, createCheckers("Black", 2, player2));
    }

    private List<Checker> createCheckers(String color, int count, Player owner) {
        List<Checker> checkers = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            checkers.add(new Checker(color, owner));
        }
        return checkers;
    }

    // Get all legal moves based on dice roll and current board state
    public List<String> getLegalMoves(Player player, List<Integer> rolls) {
        List<String> legalMoves = new ArrayList<>();
        int direction = (player == player1) ? 1 : -1;

        for (int position : positions.keySet()) {
            List<Checker> checkers = positions.get(position);
            if (!checkers.isEmpty() && checkers.get(0).getOwner().equals(player)) {
                // Iterate over each roll in the list to generate moves based on the current board state
                for (int roll : rolls) {
                    int targetPosition = position + roll * direction;
                    if (isLegalMove(player, position, targetPosition)) {
                        legalMoves.add(position + " -> " + targetPosition);
                    }
                }
            }
        }
        return legalMoves;
    }

    // Check if a move is legal by evaluating hits, blockades, and empty positions
    private boolean isLegalMove(Player player, int fromPosition, int toPosition) {
        // Check if the move is within the board boundaries
        if (toPosition < 1 || toPosition > 24 || fromPosition < 1 || fromPosition > 24) {
            return false; // Outside board limits
        }

        List<Checker> fromPositionCheckers = positions.getOrDefault(fromPosition, new ArrayList<>());

        // Ensure there is at least one checker of the player's color at the fromPosition
        if (fromPositionCheckers.isEmpty() || !fromPositionCheckers.get(0).getOwner().equals(player)) {
            return false; // No checkers to move or checkers at fromPosition don't belong to the player
        }

        return canEnterFromBar(player, toPosition);
    }

    // Update the board to reflect a move, applying hits or bear-offs if needed
    public void makeMove(Player player, int fromPosition, int toPosition) {
        List<Checker> fromCheckers = positions.get(fromPosition);
        if (fromCheckers == null || fromCheckers.isEmpty()) {
            System.out.println("Error: No checkers to move from this position.");
            return;
        }

        List<Checker> toCheckers = positions.getOrDefault(toPosition, new ArrayList<>());

        Checker movingChecker = fromCheckers.remove(fromCheckers.size() - 1);
        if (fromCheckers.isEmpty()) {
            positions.remove(fromPosition);
        }

        // Check for hits
        if (!toCheckers.isEmpty() && !toCheckers.get(0).getOwner().equals(player)) {
            Checker hitChecker = toCheckers.remove(0);
            if (player == player1) {
                barPlayer2.add(hitChecker);
            } else {
                barPlayer1.add(hitChecker);
            }
        }

        // Update the destination position with the moving checker
        toCheckers.add(movingChecker);
        positions.put(toPosition, toCheckers);

        // Handle bear-off case
        if (isBearOffPosition(player, toPosition)) {
            if (player == player1) {
                bearOffPlayer1.add(movingChecker);
            } else {
                bearOffPlayer2.add(movingChecker);
            }
            toCheckers.remove(movingChecker);
            positions.put(toPosition, toCheckers);
        }
    }

    private boolean isBearOffPosition(Player player, int position) {
        return (player == player1 && position >= 25) || (player == player2 && position <= 0);
    }

    public Map<Integer, List<Checker>> getPositions() {
        return positions;
    }

    public List<Checker> getBarForPlayer(Player player) {
        return player == player1 ? barPlayer1 : barPlayer2;
    }

    public List<Checker> getBearOffForPlayer(Player player) {
        return player == player1 ? bearOffPlayer1 : bearOffPlayer2;
    }

    public boolean canEnterFromBar(Player player, int toPosition) {
        List<Checker> targetPositionCheckers = positions.getOrDefault(toPosition, new ArrayList<>());

        // Check if the target position is empty, occupied by the player's own checkers, or has a single opponent checker
        if (targetPositionCheckers.isEmpty()) {
            return true; // Can enter an empty spot
        } else if (targetPositionCheckers.get(0).getOwner().equals(player)) {
            return true; // Can stack on own checkers
        } else {
            return targetPositionCheckers.size() == 1; // Can hit a single opponent checker
        }
    }

    public void enterFromBar(Player player, int toPosition) {
        List<Checker> bar = (player == player1) ? barPlayer1 : barPlayer2;
        if (!bar.isEmpty()) {
            Checker checker = bar.remove(bar.size() - 1);

            List<Checker> toPositionCheckers = positions.getOrDefault(toPosition, new ArrayList<>());

            // Handle hit if necessary
            if (!toPositionCheckers.isEmpty() && !toPositionCheckers.get(0).getOwner().equals(player)) {
                Checker hitChecker = toPositionCheckers.remove(0);
                if (player == player1) {
                    barPlayer2.add(hitChecker);
                } else {
                    barPlayer1.add(hitChecker);
                }
            }

            // Place checker on the target position
            toPositionCheckers.add(checker);
            positions.put(toPosition, toPositionCheckers);
        }
    }
}