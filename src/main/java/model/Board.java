package model;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

import static util.CommonConstants.*;

/**
 * Represents the Backgammon board, including player positions, bars, and bear-off areas.
 */
public class Board {
    private final Map<Integer, List<Checker>> positions = new HashMap<>();
    private final Player player1;
    private final Player player2;
    private final List<Checker> barPlayer1 = new ArrayList<>();
    private final List<Checker> barPlayer2 = new ArrayList<>();
    private final List<Checker> bearOffPlayer1 = new ArrayList<>();
    private final List<Checker> bearOffPlayer2 = new ArrayList<>();

    /**
     * Creates a new Backgammon board with two players and initializes the starting positions.
     *
     * @param player1 the first player.
     * @param player2 the second player.
     */
    public Board(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
        initializeBoard();
    }

    /**
     * Displays the current state of the board in a formatted manner.
     */
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
        System.out.print(player1.getName()+" Bar: ");
        barPlayer1.forEach(c -> System.out.print("O "));
        System.out.print("\n"+player2.getName()+" Bar: ");
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

    /**
     * Determines the maximum number of checkers at any single position on the board.
     *
     * @return the maximum number of checkers present at any board position.
     */
    private int getMaxCheckersInPosition() {
        int max = 0;
        for (List<Checker> checkers : positions.values()) {
            if (checkers.size() > max) {
                max = checkers.size();
            }
        }
        return max;
    }

    /**
     * Initializes Board by placing checkers for each player
     */
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

    /**
     * Creates checkers for the player
     * @param color Color of the checker
     * @param count Count of checkers that need to be created
     * @param owner Player who will own the checkers
     * @return Return the list of checkers based on the given conditions
     */
    private List<Checker> createCheckers(String color, int count, Player owner) {
        List<Checker> checkers = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            checkers.add(new Checker(color, owner));
        }
        return checkers;
    }

    /**
     * Retrieves all legal moves for a player based on the current board state and dice rolls.
     *
     * @param player the player for whom legal moves are being determined.
     * @param rolls  the dice rolls available for the turn.
     * @return a list of legal moves in string format.
     */
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

    /**
     * Checks if a player is eligible to bear off their checkers.
     * A player can bear off only when all their checkers are in the home quadrant.
     *
     * @param player the player being checked for bear-off eligibility.
     * @return true if the player can bear off, false otherwise.
     */
    private boolean canBearOff(Player player) {
        int homeStart = (player == player1) ? PLAYER_1_HOME_START : PLAYER_2_HOME_START;
        int homeEnd = (player == player1) ? PLAYER_1_HOME_END : PLAYER_2_HOME_END;

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

    /**
     * Determines whether a move is legal for the given player.
     *
     * @param player       the player attempting the move.
     * @param fromPosition the starting position of the move.
     * @param toPosition   the target position of the move.
     * @return true if the move is legal, false otherwise.
     */
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

    /**
     * Executes a move by moving a checker from one position to another.
     *
     * @param player       the player making the move.
     * @param fromPosition the starting position of the checker.
     * @param toPosition   the destination position of the checker.
     */
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

    /**
     * Checks if a specified position corresponds to a bear-off position for the given player.
     *
     * @param player   the player attempting to bear off.
     * @param position the position being checked.
     * @return true if the position is a bear-off position for the player, false otherwise.
     */
    private boolean isBearOffPosition(Player player, int position) {
        return (player == player1 && position >= PLAYER_1_BEAR_OFF_POSITION) || (player == player2 && position <= PLAYER_2_BEAR_OFF_POSITION);
    }

    /**
     * Retrieves the map of all board positions and their respective checkers.
     *
     * @return a map of positions to checkers.
     */
    public Map<Integer, List<Checker>> getPositions() {
        return positions;
    }

    /**
     * Retrieves the bar area for a specific player.
     *
     * @param player the player whose bar is to be retrieved.
     * @return the list of checkers in the player's bar.
     */
    public List<Checker> getBarForPlayer(Player player) {
        return player == player1 ? barPlayer1 : barPlayer2;
    }

    /**
     * Retrieves the bear-off area for a specific player.
     *
     * @param player the player whose bear-off area is to be retrieved.
     * @return the list of checkers in the player's bear-off area.
     */
    public List<Checker> getBearOffForPlayer(Player player) {
        return player == player1 ? bearOffPlayer1 : bearOffPlayer2;
    }

    /**
     * Determines whether a player can re-enter a checker from the bar to the specified position.
     *
     * @param player      the player attempting to enter a checker from the bar.
     * @param toPosition  the target position on the board.
     * @return true if the player can legally enter a checker to the target position, false otherwise.
     */
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

    /**
     * Moves a checker from the bar to the specified position on the board, if legal.
     *
     * @param player      the player re-entering a checker from the bar.
     * @param toPosition  the target position where the checker will be placed.
     */
    public void enterFromBar(Player player, int toPosition) {
        List<Checker> bar = (player == player1) ? barPlayer1 : barPlayer2;
        if (!bar.isEmpty()) {
            Checker checker = bar.remove(bar.size() - 1);
            List<Checker> toPositionCheckers = positions.getOrDefault(toPosition, new ArrayList<>());

            handleHitAndUpdatePosition(player, checker, toPositionCheckers, toPosition);
        }
    }

    /**
     * Handles moving a checker to a new position on the board, including hitting an opponent's checker if applicable.
     *
     * @param player        the player making the move.
     * @param movingChecker the checker being moved.
     * @param toCheckers    the list of checkers at the target position.
     * @param toPosition    the target position to which the checker is being moved.
     */
    private void handleHitAndUpdatePosition(Player player, Checker movingChecker, List<Checker> toCheckers, int toPosition) {
        if (!toCheckers.isEmpty() && !toCheckers.get(0).getOwner().equals(player)) {
            Checker hitChecker = toCheckers.remove(0);
            if (player == player1) {
                barPlayer2.add(hitChecker);
            } else {
                barPlayer1.add(hitChecker);
            }
        }

        // Place the moving checker at the target position.
        toCheckers.add(movingChecker);
        positions.put(toPosition, toCheckers);
    }

    /**
     * Handles the process of bearing off a checker for a player.
     *
     * @param player       the player bearing off a checker.
     * @param fromPosition the position from which the checker is being borne off.
     */
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