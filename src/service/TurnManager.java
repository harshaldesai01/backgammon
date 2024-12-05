package service;

import model.Dice;
import model.Player;

public class TurnManager {
    private Player currentPlayer;
    private final Dice dice = new Dice();

    public void determineStartingPlayer() {
        System.out.println("Rolling dice to determine who goes first...");
        while(true) {
            int roll1 = dice.roll();
            int roll2 = dice.roll();
            if(roll1 != roll2) {
                currentPlayer = roll1 > roll2 ? Player.PLAYER1: Player.PLAYER2;
                return;
            }
        }
    }

    public void toggleCurrentPlayer() {
        currentPlayer = (currentPlayer == Player.PLAYER1) ? Player.PLAYER2 : Player.PLAYER1;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public Player getOpponentPlayer() {
        return (currentPlayer == Player.PLAYER1) ? Player.PLAYER2 : Player.PLAYER1;
    }
}
