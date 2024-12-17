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
/**
 * A thread that handles the machine player's turn in the UNO game.
 * Manages the machine's card-playing behavior and ensures the game progresses
 * when it is the machine's turn.
 */
public class ThreadPlayMachine extends Thread{
    private final GameUno gameUno;
    private final ImageView tableImageView;
    private volatile boolean running = true;
    private volatile boolean machinePlaying = false;
    private volatile boolean playerPlaying;

    /**
     * Constructs a ThreadPlayMachine with the given game logic, table image, and grid pane for the machine's cards.
     *
     * @param gameUno The {@code GameUno} instance managing the game logic.
     * @param tableImageView The {@code ImageView} representing the table where cards are played.
     */
    public ThreadPlayMachine(GameUno gameUno, ImageView tableImageView) {
        this.gameUno = gameUno;
        this.tableImageView = tableImageView;
    }
    /**
     * Stops the execution of the thread by setting the {@code running} flag to {@code false}.
     */
    public void stopThread(){
        running = false;
    }

    /**
     * The main logic for the thread, executed when the thread is running.
     * Continuously checks if it is the machine's turn to play. If the conditions are met:
     *     Sets the {@code machinePlaying} flag to {@code true}.
     *     Waits for a random duration between 2 and 4 seconds before playing a card.
     *     Places a card on the table using {@code putCardOnTheTable()}.
     *     Advances the game turn to the next player.
     *     Sets the {@code machinePlaying} flag back to {@code false}.
     * The thread continues running until the {@code running} flag is set to {@code false}.
     */
    public void run(){
        while (running){
            try {
                GameUnoController gameUnoController = GameUnoStage.getInstance().getGameUnoController();
                int currentTurn = gameUnoController.getCurrentTurn();

                if (currentTurn == 1 && !machinePlaying && !playerPlaying){
                    machinePlaying = true;
                    Thread.sleep((long) (2000 + Math.random() * 2000));
                    putCardOnTheTable();
                    gameUnoController.getCurrentState().nexTurn(gameUnoController);
                    machinePlaying = false;
                }
            } catch (IOException | InterruptedException e){
                e.printStackTrace();
            }
        }
    }
    /**
     * Places a valid card from the machine player's hand onto the table.
     *
     * <p>The method attempts to find a valid card that the machine can play
     * according to the current game state. It first tries with random cards from
     * the machine player's hand. If no valid card is found after checking all
     * cards randomly, it searches systematically through the player's cards.
     *
     * @throws IOException If an I/O error occurs while interacting with the game controller.
     */

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

        // if machine hasn't found any valid card, eats one card
        if (!foundValidCard){
            gameUno.eatCard(machinePlayer, 1);
            System.out.println("La máquina ha comido una carta");
            Platform.runLater(() -> {
                gameUnoController.showUpMessage("MACHINE_TAKES");
                gameUnoController.updateCardsLabel("MACHINE_PLAYER");
                gameUnoController.printCardsMachinePlayer();
            });
        } else{
            Card card = machinePlayer.getCardsPlayer().get(index);
            table.addCardOnTheTable(card);
            tableImageView.setImage(card.getImage());
            machinePlayer.removeCard(index);

            Platform.runLater(() -> {
                try {
                    gameUnoController.checkNumberCards(machinePlayer.getCardsPlayer().size(), machinePlayer.getTypePlayer(), 1);
                    gameUnoController.printCardsMachinePlayer();
                    gameUnoController.handleCardAction(gameUno.getHumanPlayer(), card);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    /**
     * Sets the playing state of the player.
     *
     * <p>This method updates the {@code playerPlaying} flag to indicate whether
     * the player is currently taking their turn in the game or not.
     *
     * @param playerPlaying {@code true} if the player is currently playing their turn,
     *                      {@code false} if the player is not playing.
     */
    public void setPlayerPlaying(boolean playerPlaying){
        this.playerPlaying = playerPlaying;
    }
}
