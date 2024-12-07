package service;

import model.DoublingCube;
import model.Player;

public class DoublingManager {
    private Player playerToRespond;
    private final DoublingCube doublingCube;

    public DoublingManager() {
        this.doublingCube = new DoublingCube();
    }

    public Player getPlayerToRespond() {
        return playerToRespond;
    }

    public void setPlayerToRespond(Player playerToRespond) {
        this.playerToRespond = playerToRespond;
    }

    public DoublingCube getDoublingCube() {
        return doublingCube;
    }
}