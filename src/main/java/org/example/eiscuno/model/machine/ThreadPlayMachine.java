package org.example.eiscuno.model.machine;

import javafx.application.Platform;
import javafx.scene.control.Tab;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import org.example.eiscuno.controller.GameUnoController;
import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.game.GameUno;
import org.example.eiscuno.model.player.Player;
import org.example.eiscuno.model.table.Table;
import org.example.eiscuno.view.GameUnoStage;

import java.io.IOException;

public class ThreadPlayMachine extends Thread{
    private final GameUno gameUno;
    private final ImageView tableImageView;
    private volatile boolean running = true;
    private volatile boolean machinePlaying = false;
    private volatile boolean playerPlaying;

    public ThreadPlayMachine(GameUno gameUno, ImageView tableImageView) {
        this.gameUno = gameUno;
        this.tableImageView = tableImageView;
    }

    public void stopThread(){
        running = false;
    }

    public void run(){
        while (running){
            try {
                GameUnoController gameUnoController = GameUnoStage.getInstance().getGameUnoController();
                int currentTurn = gameUnoController.getCurrentTurn();

                if (currentTurn == 1 && !machinePlaying && !playerPlaying){
                    machinePlaying = true;
                    Thread.sleep((long) (3000));
                    putCardOnTheTable();
                    gameUnoController.getCurrentState().nexTurn(gameUnoController);
                    machinePlaying = false;
                }
            } catch (IOException | InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    public void putCardOnTheTable() throws IOException {
        GameUnoController gameUnoController = GameUnoStage.getInstance().getGameUnoController();

        Player machinePlayer = this.gameUno.getMachinePlayer();
        Table table = this.gameUno.getTable();

        // tries with random cards
        int index, counter = 0;
        boolean foundValidCard = false;
        do {
            index = (int) (Math.random() * machinePlayer.getCardsPlayer().size());
            counter++;
        } while (!gameUnoController.isCardPosible(machinePlayer.getCard(index), table)
                && counter <= machinePlayer.getCardsPlayer().size());

        // if no random card is valid, searches between its cards
        if (counter > machinePlayer.getCardsPlayer().size()){
            for (int i = 0; i < machinePlayer.getCardsPlayer().size(); i++){
                Card card = machinePlayer.getCard(i);
                if (gameUnoController.isCardPosible(card, table)){
                    index = i;
                    foundValidCard = true;
                    break;
                }
            }
        } else{
            foundValidCard = true;
        }

        // if machine has no valid cards, eats until finds any valid one
        while (!foundValidCard){
            gameUno.eatCard(machinePlayer, 1);
            System.out.println("La máquina ha comido una carta");
            if (gameUnoController.isCardPosible(machinePlayer.getCard(machinePlayer.getCardsPlayer().size() - 1), table)){
                index = machinePlayer.getCardsPlayer().size() - 1;
                foundValidCard = true;
            }
        }

        Card card = machinePlayer.getCardsPlayer().get(index);

        table.addCardOnTheTable(card);
        try {
            Thread.sleep(1000);
            card.animateToTable(tableImageView);
            //tableImageView.setImage(card.getImage());
            machinePlayer.removeCard(index);
        } catch (InterruptedException e){
            System.out.println(e.getCause());
        }

        Platform.runLater(() -> {
            try {
                gameUnoController.printCardsMachinePlayer();
                gameUnoController.handleCardAction(gameUno.getHumanPlayer(), card);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void setPlayerPlaying(boolean playerPlaying){
        this.playerPlaying = playerPlaying;
    }
}
