package org.example.eiscuno.model.machine;

import javafx.application.Platform;
import org.example.eiscuno.controller.GameUnoController;
import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.deck.Deck;
import org.example.eiscuno.model.game.GameUno;
import org.example.eiscuno.model.player.Player;

import java.util.ArrayList;

public class ThreadSingUNOMachine implements Runnable {
    private final GameUno gameUno;
    private final GameUnoController gameUnoController;
    private volatile boolean running = false;
    private boolean buttonUNOPressed = false;
    private int currentTurn;

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

                checkUno();

                Platform.runLater(() -> gameUnoController.setDisableUnoButton(true));

                running = false;
            }
        }
    }

    public void checkUno(){
        if (currentTurn == 0){
            if (buttonUNOPressed){
                System.out.println("HAS CANTADO UNO");
                Platform.runLater(() -> gameUnoController.showAdviseUnoTemporarily(0));
            } else {
                System.out.println("La máquina ha cantado uno primero");
                gameUno.hasSungOne("MACHINE_PLAYER");
                Platform.runLater(() -> {
                    gameUnoController.printCardsHumanPlayer();
                    gameUnoController.showAdviseUnoTemporarily(1);
                });
            }
        } else if (currentTurn == 1) {
            if (buttonUNOPressed) {
                System.out.println("HAS CANTADO UNO");
                gameUno.hasSungOne("HUMAN_PLAYER");
                Platform.runLater(() -> {
                    gameUnoController.printCardsMachinePlayer();
                    gameUnoController.showAdviseUnoTemporarily(0);
                });
            } else {
                System.out.println("La máquina ha cantado uno primero");
                Platform.runLater(() -> gameUnoController.showAdviseUnoTemporarily(1));
            }
        }
    }

    public void setRunning(boolean running) {
        this.running = running;
    }
    public void setButtonUNOPressed(boolean buttonUNOPressed) {
        this.buttonUNOPressed = buttonUNOPressed;
    }
    public void setCurrentTurn(int currentTurn){ this.currentTurn = currentTurn;}

    public boolean getButtonUNOPressed(){ return buttonUNOPressed;}
}
