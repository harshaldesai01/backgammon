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
        System.out.println("Backgammon Board:");
        System.out.println("--------------------------------------------------------------------");

        // Top side (points 13 to 24)
        for (int i = 13; i <= 24; i++) {
            System.out.printf(" %2d ", i);
        }
        System.out.println();

        for (int row = 0; row < 5; row++) {
            for (int point = 13; point <= 24; point++) {
                List<Checker> checkers = positions.getOrDefault(point, new ArrayList<>());
                if (row < checkers.size()) {
                    System.out.print(checkers.get(row).getOwner() == player1 ? " O " : " X ");
                } else {
                    System.out.print("   ");
                }
                System.out.print("|");
            }
            System.out.println();
        }

        // Middle bar
        System.out.println("---------------------------- BAR ----------------------------");

        // Bottom side (points 12 to 1)
        for (int i = 12; i >= 1; i--) {
            System.out.printf(" %2d ", i);
        }
        System.out.println();

        for (int row = 0; row < 5; row++) {
            for (int point = 12; point >= 1; point--) {
                List<Checker> checkers = positions.getOrDefault(point, new ArrayList<>());
                if (row < checkers.size()) {
                    System.out.print(checkers.get(row).getOwner() == player1 ? " O " : " X ");
                } else {
                    System.out.print("   ");
                }
                System.out.print("|");
            }
            System.out.println();
        }

        System.out.println("--------------------------------------------------------------------");

        // Display bar and bear-off separately if needed
        System.out.print("Player 1 Bar: ");
        barPlayer1.forEach(c -> System.out.print("O "));
        System.out.print("\nPlayer 2 Bar: ");
        barPlayer2.forEach(c -> System.out.print("X "));
        System.out.println();

        System.out.print("Player 1 Bear-Off: ");
        bearOffPlayer1.forEach(c -> System.out.print("O "));
        System.out.print("\nPlayer 2 Bear-Off: ");
        bearOffPlayer2.forEach(c -> System.out.print("X "));
        System.out.println();
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
    public List<String> getLegalMoves(Player player, int roll1, int roll2) {
        List<String> legalMoves = new ArrayList<>();
        int direction = (player == player1) ? 1 : -1;

        for (int position : positions.keySet()) {
            List<Checker> checkers = positions.get(position);
            if (!checkers.isEmpty() && checkers.get(0).getOwner().equals(player)) {
                int targetPosition1 = position + roll1 * direction;
                int targetPosition2 = position + roll2 * direction;

                if (isLegalMove(player, position, targetPosition1)) {
                    legalMoves.add(position + " -> " + targetPosition1);
                }
                if (isLegalMove(player, position, targetPosition2)) {
                    legalMoves.add(position + " -> " + targetPosition2);
                }
            }
        }
        return legalMoves;
    }

    // Check if a move is legal by evaluating hits, blockades, and empty positions
    private boolean isLegalMove(Player player, int fromPosition, int toPosition) {
        if (toPosition < 1 || toPosition > 24) {
            return false; // Outside board limits
        }
        List<Checker> targetPositionCheckers = positions.getOrDefault(toPosition, new ArrayList<>());

        if (targetPositionCheckers.isEmpty()) {
            return true; // Can move to an empty spot
        } else if (targetPositionCheckers.get(0).getOwner().equals(player)) {
            return true; // Can stack on own checkers
        } else if (targetPositionCheckers.size() == 1) {
            return true; // Can hit opponent's single checker
        }
        return false; // Cannot move to a spot blocked by two or more opponent checkers
    }

    // Update the board to reflect a move, applying hits or bear-offs if needed
    public void makeMove(Player player, int fromPosition, int toPosition) {
        List<Checker> fromCheckers = positions.get(fromPosition);
        List<Checker> toCheckers = positions.getOrDefault(toPosition, new ArrayList<>());

        Checker movingChecker = fromCheckers.remove(fromCheckers.size() - 1); // Remove the last checker in the list
        if (!fromCheckers.isEmpty()) {
            positions.put(fromPosition, fromCheckers); // Update if checkers remain
        } else {
            positions.remove(fromPosition); // Clear position if no checkers remain
        }

        if (!toCheckers.isEmpty() && !toCheckers.get(0).getOwner().equals(player)) {
            Checker hitChecker = toCheckers.remove(0); // Hit opponent's checker
            if (player == player1) {
                barPlayer2.add(hitChecker);
            } else {
                barPlayer1.add(hitChecker);
            }
        }

        toCheckers.add(movingChecker);
        positions.put(toPosition, toCheckers);

        // Handle bear-off if the checker reaches the end of the board
        if (isBearOffPosition(player, toPosition)) {
            if (player == player1) {
                bearOffPlayer1.add(movingChecker);
            } else {
                bearOffPlayer2.add(movingChecker);
            }
            toCheckers.remove(movingChecker); // Remove checker from board after bear-off
        }
    }

    private boolean isBearOffPosition(Player player, int position) {
        return (player == player1 && position == 25) || (player == player2 && position == 0);
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
}