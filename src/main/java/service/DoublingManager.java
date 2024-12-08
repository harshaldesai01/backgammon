package service;

import model.DoublingCube;
import model.Player;

/**
 * Manages the doubling cube in Backgammon, including its value and the player responsible for responding to doubling offers.
 */
public class DoublingManager {
    private Player playerToRespond;
    private final DoublingCube doublingCube;

    /**
     * Initializes the DoublingManager with a new {@link DoublingCube}.
     */
    public DoublingManager() {
        this.doublingCube = new DoublingCube();
    }

    /**
     * Retrieves the player who needs to respond to a doubling offer.
     *
     * @return the player required to respond, or null if no response is pending.
     */
    public Player getPlayerToRespond() {
        return playerToRespond;
    }

    /**
     * Sets the player who needs to respond to a doubling offer.
     *
     * @param playerToRespond the player who must respond.
     */
    public void setPlayerToRespond(Player playerToRespond) {
        this.playerToRespond = playerToRespond;
    }

    /**
     * Retrieves the {@link DoublingCube} managed by this DoublingManager.
     *
     * @return the doubling cube.
     */
    public DoublingCube getDoublingCube() {
        return doublingCube;
    }
}