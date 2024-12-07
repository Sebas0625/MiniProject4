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
                    // Cuando el jugador tenga una carta en su mano, se espera 4 segundos, si se pasa este tiempo
                    // la maquina lo penaliza agregando una carta al jugador humano.
                    //Random timeout between 2000 ms (2 s) and 4000 ms (4 s)
                    Thread.sleep((long) (Math.random() * 2000) + 2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // Solo se agregara una carta si el boton uno no ha sido presionado
                if(!buttonUNOPressed){
                    hasOneCardTheHumanPlayer();
                }
                running = false; // Detiene la ejecución para agregar cartas hasta ser activado nuevamente
            }
        }
    }

    // Se agrega una carta como penalización a la mano del jugador
    private void hasOneCardTheHumanPlayer(){
        gameUno.haveSungOne("MACHINE_PLAYER");
        // Se garantiza que el bloque de codigo se ejecute en el hilo  javaFx, evitando conflictos entre hilos
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
