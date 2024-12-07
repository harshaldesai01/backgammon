package model;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

import static util.CommonConstants.HORIZONTAL_DIVIDER;

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
        System.out.println(HORIZONTAL_DIVIDER);

        System.out.print("|");
        for (int i = 13; i <= 24; i++) {
            System.out.printf(" %2d |", i);
        }

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
            if (row < bearOffPlayer1.size()) {
                System.out.print("  O  |");
            } else {
                System.out.print("     |");
            }
            System.out.println();
        }

        System.out.println("------------------------------- BAR -------------------------------");
        System.out.print("Player 1 Bar: ");
        barPlayer1.forEach(c -> System.out.print("O "));
        System.out.print("\nPlayer 2 Bar: ");
        barPlayer2.forEach(c -> System.out.print("X "));
        System.out.println();
        System.out.println("------------------------------- BAR -------------------------------");

        System.out.print("|");
        for (int i = 12; i >= 1; i--) {
            System.out.printf(" %2d |", i);
        }

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

            if (row < bearOffPlayer2.size()) {
                System.out.print("  X  |");
            } else {
                System.out.print("     |");
            }
            System.out.println();
        }

        System.out.println(HORIZONTAL_DIVIDER);

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

    public List<String> getLegalMoves(Player player, List<Integer> rolls) {
        List<String> legalMoves = new ArrayList<>();
        int direction = (player == player1) ? 1 : -1;
        boolean canBearOff = canBearOff(player);

        for (int position : positions.keySet()) {
            List<Checker> checkers = positions.get(position);
            if (!checkers.isEmpty() && checkers.get(0).getOwner().equals(player)) {
                for (int roll : rolls) {
                    int targetPosition = position + roll * direction;

                    if (canBearOff && isBearOffPosition(player, targetPosition)) {
                        legalMoves.add(position + " -> OFF");
                    } else if (isLegalMove(player, position, targetPosition)) {
                        legalMoves.add(position + " -> " + targetPosition);
                    }
                }
            }
        }
        return legalMoves;
    }

    private boolean canBearOff(Player player) {
        int homeStart = (player == player1) ? 19 : 1;
        int homeEnd = (player == player1) ? 24 : 6;

        for (int position : positions.keySet()) {
            if (position < homeStart || position > homeEnd) {
                List<Checker> checkers = positions.get(position);
                if (!checkers.isEmpty() && checkers.get(0).getOwner().equals(player)) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isLegalMove(Player player, int fromPosition, int toPosition) {
        if (fromPosition < 1 || fromPosition > 24) {
            return false;
        }

        if (toPosition < 1 || toPosition > 24) {
            return false;
        }

        List<Checker> fromPositionCheckers = positions.getOrDefault(fromPosition, new ArrayList<>());
        List<Checker> toPositionCheckers = positions.getOrDefault(toPosition, new ArrayList<>());

        if (fromPositionCheckers.isEmpty() || !fromPositionCheckers.get(0).getOwner().equals(player)) {
            return false;
        }

        return toPositionCheckers.isEmpty() ||
                toPositionCheckers.get(0).getOwner().equals(player) ||
                (toPositionCheckers.size() == 1 && !toPositionCheckers.get(0).getOwner().equals(player));
    }

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

        handleHitAndUpdatePosition(player, movingChecker, toCheckers, toPosition);
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

        if (targetPositionCheckers.isEmpty()) {
            return true;
        } else if (targetPositionCheckers.get(0).getOwner().equals(player)) {
            return true;
        } else {
            return targetPositionCheckers.size() == 1;
        }
    }

    public void enterFromBar(Player player, int toPosition) {
        List<Checker> bar = (player == player1) ? barPlayer1 : barPlayer2;
        if (!bar.isEmpty()) {
            Checker checker = bar.remove(bar.size() - 1);
            List<Checker> toPositionCheckers = positions.getOrDefault(toPosition, new ArrayList<>());

            handleHitAndUpdatePosition(player, checker, toPositionCheckers, toPosition);
        }
    }

    private void handleHitAndUpdatePosition(Player player, Checker movingChecker, List<Checker> toCheckers, int toPosition) {
        if (!toCheckers.isEmpty() && !toCheckers.get(0).getOwner().equals(player)) {
            Checker hitChecker = toCheckers.remove(0);
            if (player == player1) {
                barPlayer2.add(hitChecker);
            } else {
                barPlayer1.add(hitChecker);
            }
        }

        toCheckers.add(movingChecker);
        positions.put(toPosition, toCheckers);
    }

    public void bearOffChecker(Player player, int fromPosition) {
        List<Checker> fromCheckers = positions.get(fromPosition);

        if (fromCheckers == null || fromCheckers.isEmpty()) {
            System.out.println("Error: No checkers to bear off from this position.");
            return;
        }

        Checker movingChecker = fromCheckers.remove(fromCheckers.size() - 1);

        if (fromCheckers.isEmpty()) {
            positions.remove(fromPosition);
        } else {
            positions.put(fromPosition, fromCheckers);
        }

        if (player == player1) {
            bearOffPlayer1.add(movingChecker);
        } else {
            bearOffPlayer2.add(movingChecker);
        }
    }
}