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