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

import controller.GameController;

/**
 * The main class to initiate the Backgammon game.
 */
public class Backgammon {

    /**
     * The entry point of the Backgammon game.
     * Creates an instance of GameController and starts the game.
     *
     * @param args command-line arguments (not used).
     */
    public static void main(String[] args) {
        GameController gameController = new GameController();
        gameController.startGame();
    }
}