package util;

/**
 * Defines common constants used throughout the Backgammon application.
 */
public class CommonConstants {
    public static final String WELCOME_MESSAGE = "Welcome to Backgammon!";
    public static final String QUIT_MESSAGE = "Game terminated.";
    public static final String FINAL_THANK_YOU_MESSAGE = "Thank you for playing Backgammon!";
    public static final int SINGLE = 1;
    public static final int GAMMON = 2;
    public static final int BACKGAMMON = 3;
    public static final String INVALID_SELECTION_MESSAGE = "Invalid selection. Please try again.";
    public static final String NO_LEGAL_MOVES_MESSAGE = "No legal moves available for the current rolls. Turn passes to the next player.";
    public static final String HORIZONTAL_DIVIDER = "----------------------------------------------------------------------";
    public static final String DRAW_CONDITION = "Draw";
    public static final int MINIMUM_DICE_VALUE = 1;
    public static final int MAXIMUM_DICE_VALUE = 6;
    public static final int PLAYER_1_HOME_START = 19;
    public static final int PLAYER_1_HOME_END = 24;
    public static final int PLAYER_2_HOME_START = 1;
    public static final int PLAYER_2_HOME_END = 6;
    public static final int PLAYER_1_MOVE_DIRECTION = 1;
    public static final int PLAYER_2_MOVE_DIRECTION = -1;
    public static final int PLAYER_1_BEAR_OFF_POSITION = 25;
    public static final int PLAYER_2_BEAR_OFF_POSITION = 0;
    public static final String NO_VALID_ROLLS_MESSAGE = "No valid rolls available for bearing off";
    public static final String BAR = "BAR";
    public static final String OFF = "OFF";
    public static final String EMPTY_PLAYER_NAME_MESSAGE = "Player name cannot be empty. Please try again.";
    public static final String POSITIVE_MATCH_LENGTH_MESSAGE = "Match length must be a positive integer. Try again.";
    public static final String INVALID_MATCH_LENGTH_INPUT_MESSAGE = "Invalid input. Please enter a valid number for match length.";
    public static final String MATCH_LENGTH_INPUT_PROMPT = "Enter the match length (e.g., 3, 5, 7): ";
    public static final String MATCH_END_MESSAGE = "Ending the current match...";
    public static final String GAME_END_MESSAGE = "Ending current game!";
    public static final String DOUBLING_COMMANDS_ERROR_MESSAGE = "This command can only be used in case of a double offer!";
    public static final String NEW_MATCH_PROMPT = "Would you like to start a new match? (yes/no): ";
    public static final String MATCH_OVER_MESSAGE = "Match Over! ";
    public static final String GAME_OVER_MESSAGE = "Game Over! ";
    public static final String GAMMON_MESSAGE = "It's a gammon! Points will be doubled.";
    public static final String BACKGAMMON_MESSAGE = "It's a Backgammon! Points will be tripled.";
    public static final String ROLL_TIE_MESSAGE = "It's a tie! Rolling again...";
    public static final String ROLL_TO_START_MESSAGE = "Rolling dice to determine who goes first...";
    public static final String PIP_COMMAND_MESSAGE = "ENTERED PIP COMMAND....";
    public static final String DOUBLING_CUBE_NO_OWNER_MESSAGE = "The cube is in the middle (no owner).";
    public static final String DOUBLING_CUBE_ERROR_MESSAGE = "It's not your turn to respond to the double.";
}