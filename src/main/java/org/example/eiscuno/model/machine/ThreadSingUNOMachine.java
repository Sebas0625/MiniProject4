package org.example.eiscuno.model.machine;

import javafx.application.Platform;
import org.example.eiscuno.controller.GameUnoController;
import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.deck.Deck;
import org.example.eiscuno.model.game.GameUno;
import org.example.eiscuno.model.player.Player;

import java.util.ArrayList;
/**
 * ThreadSingUNOMachine handles the behavior of a machine player in the UNO game
 * running as a separate thread.
 */
public class ThreadSingUNOMachine implements Runnable {
    private final GameUno gameUno;
    private final GameUnoController gameUnoController;
    private volatile boolean running = false;
    private boolean buttonUNOPressed = false;
    private int currentTurn;

    /**
     * Constructs a ThreadSingUNOMachine with the given game logic and controller.
     *
     * @param gameUno The instance of GameUno containing the game logic.
     * @param gameUnoController The controller managing the game interactions and UI.
     */
    public ThreadSingUNOMachine(GameUno gameUno, GameUnoController gameUnoController) {
        this.gameUno = gameUno;
        this.gameUnoController = gameUnoController;
    }
    /**
     * Executes the logic for the machine player in the UNO game when the thread is running.
     * Continuously checks if the thread is active (`running`).
     * The thread stops its main logic once the `running` flag is set to false.
     */
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

    /**
     * Checks the current turn and determines the outcome of the "UNO" action.
     * This method evaluates whether the "UNO" button was pressed and executes the corresponding logic
     * for the human or machine player based on the current turn.
     */
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
    /**
     * Sets the running state of the thread.
     *
     * @param running {@code true} to start the thread's execution logic,
     *                {@code false} to stop it.
     */
    public void setRunning(boolean running) {
        this.running = running;
    }
    /**
     * Sets the state of the "UNO" button press.
     *
     * @param buttonUNOPressed {@code true} if the "UNO" button has been pressed,
     *                         {@code false} otherwise.
     */
    public void setButtonUNOPressed(boolean buttonUNOPressed) {
        this.buttonUNOPressed = buttonUNOPressed;
    }
    /**
     * Sets the current turn in the game.
     *
     * @param currentTurn The current turn of the game
     */
    public void setCurrentTurn(int currentTurn){ this.currentTurn = currentTurn;}

    public boolean getButtonUNOPressed(){ return buttonUNOPressed;}
}
