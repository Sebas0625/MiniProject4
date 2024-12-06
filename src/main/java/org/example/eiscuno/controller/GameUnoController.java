package org.example.eiscuno.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Arc;
import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.deck.Deck;
import org.example.eiscuno.model.game.GameUno;
import org.example.eiscuno.model.machine.ThreadPlayMachine;
import org.example.eiscuno.model.player.Player;
import org.example.eiscuno.model.table.Table;

import javax.sound.sampled.FloatControl;
import java.util.Objects;

/**
 * Controller class for the Uno game.
 */
public class GameUnoController {

    @FXML
    private GridPane gridPaneCardsMachine;

    @FXML
    private GridPane gridPaneCardsPlayer;

    @FXML
    private ImageView tableImageView;

    @FXML
    private Label machineCardsLabel;

    @FXML
    private Label humanPlayerCardsLabel;

    @FXML
    private AnchorPane pieAnchorPane;

    private Player humanPlayer;
    private Player machinePlayer;
    private Deck deck;
    private Table table;
    private GameUno gameUno;
    private int posInitCardToShow;
    private ThreadPlayMachine threadPlayMachine;

    /**
     * Initializes the controller.
     */
    @FXML
    public void initialize() {
        initVariables();
        printCardsHumanPlayer();
        printCardsMachinePlayer();
        Card firstCard = deck.takeCard();
        table.addCardOnTheTable(firstCard);
        tableImageView.setImage(firstCard.getImage());
        threadPlayMachine.start();
    }

    /**
     * Initializes the variables for the game.
     */
    private void initVariables() {
        this.humanPlayer = new Player("HUMAN_PLAYER");
        this.machinePlayer = new Player("MACHINE_PLAYER");
        this.deck = new Deck();
        this.table = new Table();
        this.gameUno = new GameUno(this.humanPlayer, this.machinePlayer, this.deck, this.table);
        this.posInitCardToShow = 0;
        this.threadPlayMachine = new ThreadPlayMachine(gameUno, tableImageView);
    }

    /**
     * Prints the human player's cards on the grid pane.
     */
    private void printCardsHumanPlayer() {
        this.gridPaneCardsPlayer.getChildren().clear();
        Card[] currentVisibleCardsHumanPlayer = this.gameUno.getCurrentVisibleCardsHumanPlayer(this.posInitCardToShow);

        for (int i = 0; i < currentVisibleCardsHumanPlayer.length; i++) {
            Card card = currentVisibleCardsHumanPlayer[i];
            ImageView cardImageView = card.getCard();

            cardImageView.setOnMouseClicked((MouseEvent event) -> {
                if (isCardPosible(card, table)){
                    gameUno.playCard(card);
                    tableImageView.setImage(card.getImage());
                    humanPlayer.removeCard(findPosCardsHumanPlayer(card));
                    printCardsHumanPlayer();

                    applyPower(machinePlayer, card);
                }
            });
            this.gridPaneCardsPlayer.add(cardImageView, i, 0);
        }
        humanPlayerCardsLabel.setText("Tus cartas: " + humanPlayer.getCardsPlayer().size());
    }

    public void printCardsMachinePlayer(){
        Card[] currentVisibleCardsMachinePlayer = this.gameUno.getCurrentVisibleCardsMachinePLayer();
            gridPaneCardsMachine.getChildren().clear();
            for (int i = 0; i < currentVisibleCardsMachinePlayer.length; i++){
                Card card = currentVisibleCardsMachinePlayer[i];
                ImageView cardImageView = card.getCard();

                gridPaneCardsMachine.add(cardImageView, i , 0);

                machineCardsLabel.setText("Cartas de la máquina: " + machinePlayer.getCardsPlayer().size());
            }
    }

    public boolean isCardPosible(Card card, Table table){
        return Objects.equals(table.getCurrentColor(), card.getColor())
                || Objects.equals(table.getCurrentNum(), card.getValue())
                || Objects.equals(card.getValue(), "WILD")
                || Objects.equals(card.getValue(), "FOUR");
    }

    public void applyPower(Player targetPlayer, Card card){
        switch (card.getValue()) {
            case "FOUR":
                gameUno.eatCard(targetPlayer, 4);
                pieAnchorPane.setVisible(true);
                freezePlayMachineThread();
                break;
            case "TWO":
                gameUno.eatCard(targetPlayer, 2);
                // Crear un thread para cuando se lanzan cartas de comer
                break;
            case "SKIP":
                if (targetPlayer == humanPlayer){
                    threadPlayMachine.setHasPLayerPlayed(true);
                }
                break;
            case "WILD":
                pieAnchorPane.setVisible(true);
                freezePlayMachineThread();
                break;
            case "REVERSE":
                break;
            default:
                threadPlayMachine.setHasPLayerPlayed(true);
                System.out.println("La carta no tiene ninguna característica");
        }
    }

    /**
     * Finds the position of a specific card in the human player's hand.
     *
     * @param card the card to find
     * @return the position of the card, or -1 if not found
     */
    private Integer findPosCardsHumanPlayer(Card card) {
        for (int i = 0; i < this.humanPlayer.getCardsPlayer().size(); i++) {
            if (this.humanPlayer.getCardsPlayer().get(i).equals(card)) {
                return i;
            }
        }
        return -1;
    }

    public void setDisableHumanPlayerCards(boolean value){
        gridPaneCardsPlayer.setDisable(value);
    }

    public void handleColorSelection(ActionEvent event){
        Arc arc = (Arc) event.getSource();
        Paint color = arc.getFill();
        if (color == Color.RED){
            table.setCurrentColor("RED");
        } else if(color == Color.BLUE){
            table.setCurrentColor("BLUE");
        } else if(color == Color.GREEN){
            table.setCurrentColor("GREEN");
        } else if(color == Color.YELLOW){
            table.setCurrentColor("YELLOW");
        }
        pieAnchorPane.setVisible(false);
        notifyPlayMachineThread();
    }

    private void freezePlayMachineThread(){
        synchronized (threadPlayMachine){
            try{
                threadPlayMachine.wait();
            } catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    private void notifyPlayMachineThread(){
        synchronized (threadPlayMachine){
            threadPlayMachine.notify();
        }
    }

    /**
     * Handles the "Back" button action to show the previous set of cards.
     *
     * @param event the action event
     */
    @FXML
    void onHandleBack(ActionEvent event) {
        if (this.posInitCardToShow > 0) {
            this.posInitCardToShow--;
            printCardsHumanPlayer();
        }
    }

    /**
     * Handles the "Next" button action to show the next set of cards.
     *
     * @param event the action event
     */
    @FXML
    void onHandleNext(ActionEvent event) {
        if (this.posInitCardToShow < this.humanPlayer.getCardsPlayer().size() - 4) {
            this.posInitCardToShow++;
            printCardsHumanPlayer();
        }
    }

    @FXML
    void onHandleTakeCard() {
        gameUno.eatCard(humanPlayer, 1);
        printCardsHumanPlayer();
    }

    /**
     * Handles the action of saying "Uno".
     *
     * @param event the action event
     */
    @FXML
    void onHandleUno(ActionEvent event) {
        // Implement logic to handle Uno event here
    }
}
