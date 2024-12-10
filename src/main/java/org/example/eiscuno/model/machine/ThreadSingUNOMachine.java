package org.example.eiscuno.model.machine;

import javafx.application.Platform;
import org.example.eiscuno.controller.GameUnoController;
import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.deck.Deck;
import org.example.eiscuno.model.game.GameUno;
import org.example.eiscuno.model.player.Player;

import java.util.ArrayList;

public class ThreadSingUNOMachine implements Runnable {
    private GameUno gameUno;
    private GameUnoController gameUnoController;
    private volatile boolean running = false;
    boolean buttonUNOPressed = false;

    public ThreadSingUNOMachine(GameUno gameUno, GameUnoController gameUnoController) {
        this.gameUno = gameUno;
        this.gameUnoController = gameUnoController;
    }

    @Override
    public void run(){
        while (true){
            if (running) {
                try {
                    //Random timeout between 2000 ms (2 s) and 4000 ms (4 s)
                    Thread.sleep((long) (Math.random() * 2000) + 2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(!buttonUNOPressed){
                    hasOneCardTheHumanPlayer();
                }
                running = false;
            }
        }
    }

    private void hasOneCardTheHumanPlayer(){
        gameUno.haveSungOne("MACHINE_PLAYER");
        Platform.runLater(() -> gameUnoController.updateVisibleCardsHumanPlayer());
        Platform.runLater(() -> gameUnoController.setDisableButton(true));
    }

    public void setRunning(boolean running) {
        this.running = running;
    }
    public void setButtonUNOPressed(boolean buttonUNOPressed) {
        this.buttonUNOPressed = buttonUNOPressed;
    }
}
