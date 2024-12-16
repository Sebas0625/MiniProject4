package org.example.eiscuno.controller;

import javafx.animation.PauseTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Arc;
import javafx.util.Duration;
import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.deck.Deck;
import org.example.eiscuno.model.exception.GameException;
import org.example.eiscuno.model.game.GameUno;
import org.example.eiscuno.model.gameState.GameState;
import org.example.eiscuno.model.gameState.PlayerTurnState;
import org.example.eiscuno.model.machine.ThreadSingUNOMachine;
import org.example.eiscuno.model.machine.ThreadPlayMachine;
import org.example.eiscuno.model.player.Player;
import org.example.eiscuno.model.table.Table;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import org.example.eiscuno.view.GameUnoStage;
import org.example.eiscuno.view.LoseStage;
import org.example.eiscuno.view.WinStage;

import javax.swing.*;
import java.io.IOException;
import java.util.Objects;
import java.util.Random;

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
    private Button buttonUNO;

    @FXML
    private ImageView adviseUnoPlayer;

    @FXML
    private ImageView adviseUnoMachine;

    @FXML
    private AnchorPane pieAnchorPane;

    private Player humanPlayer;
    private Player machinePlayer;
    private Deck deck;
    private Table table;
    private GameUno gameUno;
    private int posInitCardToShow;
    private ThreadPlayMachine threadPlayMachine;
    private ThreadSingUNOMachine threadSingUNOMachine;
    private GameState currentState;

    /**
     * Initializes the controller.
     */
    @FXML
    public void initialize() {
        initVariables();
        startGameWithAnimation();
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
        this.currentState = new PlayerTurnState();
        this.threadPlayMachine = new ThreadPlayMachine(gameUno, tableImageView);
        this.buttonUNO.setDisable(true);
        this.threadSingUNOMachine = new ThreadSingUNOMachine(this.gameUno, this);
        Thread t = new Thread(threadSingUNOMachine, "ThreadSingUNO");
        t.setDaemon(true);
        t.start();
    }


    private void startGameWithAnimation() {
        gameUno.startGame();

        for (int i = 0; i < 4; i++) {
            Card humanCard = humanPlayer.getCard(i);
            animateCardDeal(humanCard, gridPaneCardsPlayer, i);
        }
        for (int i = 0; i < 4; i++) {
            Card machineCard = machinePlayer.getCard(i);
            animateCardDeal(machineCard, gridPaneCardsMachine, i);
        }

        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(event -> {
            printCardsHumanPlayer();
            try {
                printCardsMachinePlayer();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            Card firstCard;
            boolean isCardPosible;
            do {
                isCardPosible = true;
                firstCard = gameUno.getDeck().takeCard();
                if (Objects.equals(firstCard.getValue(), "FOUR")
                    || Objects.equals(firstCard.getValue(), "TWO")
                    || Objects.equals(firstCard.getValue(), "SKIP")
                    || Objects.equals(firstCard.getValue(), "REVERSE")
                    || Objects.equals(firstCard.getValue(), "WILD")){
                    isCardPosible = false;
                    gameUno.getDeck().retournCard(firstCard);
                }
            } while (!isCardPosible);

            table.addCardOnTheTable(firstCard);
            tableImageView.setImage(firstCard.getImage());
            threadPlayMachine.start();
        });
        pause.play();
    }

    /**
     * Prints the human player's cards on the grid pane.
     */
    private void printCardsHumanPlayer() {
        this.gridPaneCardsPlayer.getChildren().clear();
        Card[] currentVisibleCardsHumanPlayer = humanPlayer.getCurrentVisibleCards(this.posInitCardToShow);

        for (int i = 0; i < currentVisibleCardsHumanPlayer.length; i++) {
            Card card = currentVisibleCardsHumanPlayer[i];
            ImageView cardImageView = card.getCard();

            cardImageView.setOnMouseClicked((MouseEvent event) -> {
                if (isCardPosible(card, table) && getCurrentTurn() == 0){
                    gameUno.playCard(card);
                    //tableImageView.setImage(card.getImage());
                    card.animateToTable(tableImageView);
                    humanPlayer.removeCard(findPosCardsHumanPlayer(card));
                    printCardsHumanPlayer();

                    try {
                        threadPlayMachine.setPlayerPlaying(true);
                        currentState.nexTurn(this);
                        handleCardAction(machinePlayer, card);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }

                    setDisableButton(true);

                    try {
                        checkNumberCards(humanPlayer.getCardsPlayer().size(), humanPlayer.getTypePlayer());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            this.gridPaneCardsPlayer.add(cardImageView, i, 0);
        }
        humanPlayerCardsLabel.setText("Tus cartas: " + humanPlayer.getCardsPlayer().size());
    }

    public void printCardsMachinePlayer(){
        Card[] currentVisibleCardsMachinePlayer = machinePlayer.getCurrentVisibleCards(0);
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

    public void handleCardAction(Player targetPlayer, Card card) throws Exception {
        switch (card.getValue()) {
            case "FOUR":
                gameUno.eatCard(targetPlayer, 4);
                pieAnchorPane.setVisible(true);
                tableImageView.setVisible(false);
                showUpMessage("+4");
                if (targetPlayer == humanPlayer){
                    printCardsHumanPlayer();
                    handleMachineColorSelection();
                } else {
                    printCardsMachinePlayer();
                }
                break;
            case "TWO":
                gameUno.eatCard(targetPlayer, 2);
                if (targetPlayer == humanPlayer){
                    printCardsHumanPlayer();
                } else {
                    threadPlayMachine.setPlayerPlaying(false);
                    printCardsMachinePlayer();
                }
                break;
            case "WILD":
                pieAnchorPane.setVisible(true);
                tableImageView.setVisible(false);
                showUpMessage("+2");
                if (targetPlayer == humanPlayer){ handleMachineColorSelection(); }
                break;
            case "SKIP":
                showUpMessage("TURNO SALTADO");
                currentState.nexTurn(this);
                break;
            case "REVERSE":
                showUpMessage("SENTIDO CAMBIADO");
                currentState.nexTurn(this);
                break;
            default:
                System.out.println("La carta no tiene ninguna característica");
                threadPlayMachine.setPlayerPlaying(false);
                break;
        }
        gameUno.getDeck().retournCard(card);
        checkNumberCards(machinePlayer.getCardsPlayer().size(), machinePlayer.getTypePlayer());
    }

    /**
     * Finds the position of a specific card in the human player's hand.
     *
     * @param card the card to find
     * @return the position of the card, or -1 if not found
     */
    public Integer findPosCardsHumanPlayer(Card card) {
        for (int i = 0; i < this.humanPlayer.getCardsPlayer().size(); i++) {
            if (this.humanPlayer.getCardsPlayer().get(i).equals(card)) {
                return i;
            }
        }
        return -1;
    }

    private void handleMachineColorSelection(){
        Random rand = new Random();
        int num = rand.nextInt(4);
        switch (num){
            case 0:
                setBlueColor();
                break;
            case 1:
                setRedColor();
                break;
            case 2:
                setYellowColor();
                break;
            case 3:
                setGreenColor();
                break;
        }
    }

    @FXML
    private void setBlueColor(){
        if (table.getCurrentCardOnTheTable().getValue() == "WILD"){
            tableImageView.setImage(new Image(getClass().getResource("/org/example/eiscuno/cards-uno/BLUE_wild.png").toExternalForm()));
        } else{
            tableImageView.setImage(new Image(getClass().getResource("/org/example/eiscuno/cards-uno/BLUE_4_wild_draw.png").toExternalForm()));
        }
        this.table.setCurrentColor("BLUE");
        this.pieAnchorPane.setVisible(false);
        tableImageView.setVisible(true);
        threadPlayMachine.setPlayerPlaying(false);
    }

    @FXML
    private void setRedColor(){
        if (table.getCurrentCardOnTheTable().getValue() == "WILD"){
            tableImageView.setImage(new Image(getClass().getResource("/org/example/eiscuno/cards-uno/RED_wild.png").toExternalForm()));
        } else{
            tableImageView.setImage(new Image(getClass().getResource("/org/example/eiscuno/cards-uno/RED_4_wild_draw.png").toExternalForm()));
        }
        this.table.setCurrentColor("RED");
        this.pieAnchorPane.setVisible(false);
        tableImageView.setVisible(true);
        threadPlayMachine.setPlayerPlaying(false);
    }

    @FXML
    private void setYellowColor(){
        if (table.getCurrentCardOnTheTable().getValue() == "WILD"){
            tableImageView.setImage(new Image(getClass().getResource("/org/example/eiscuno/cards-uno/YELLOW_wild.png").toExternalForm()));
        } else{
            tableImageView.setImage(new Image(getClass().getResource("/org/example/eiscuno/cards-uno/YELLOW_4_wild_draw.png").toExternalForm()));
        }
        this.table.setCurrentColor("YELLOW");
        this.pieAnchorPane.setVisible(false);
        tableImageView.setVisible(true);
        threadPlayMachine.setPlayerPlaying(false);
    }

    @FXML
    private void setGreenColor(){
        if (table.getCurrentCardOnTheTable().getValue() == "WILD"){
            tableImageView.setImage(new Image(getClass().getResource("/org/example/eiscuno/cards-uno/GREEN_wild.png").toExternalForm()));
        } else{
            tableImageView.setImage(new Image(getClass().getResource("/org/example/eiscuno/cards-uno/GREEN_4_wild_draw.png").toExternalForm()));
        }
        this.table.setCurrentColor("GREEN");
        this.pieAnchorPane.setVisible(false);
        tableImageView.setVisible(true);
        threadPlayMachine.setPlayerPlaying(false);
    }

    public void setState(GameState state) {
        this.currentState = state;
    }

    public GameState getCurrentState() {
        return currentState;
    }

    public int getCurrentTurn(){
        return currentState instanceof PlayerTurnState ? 0 : 1;
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
        try {
            if (deck.isEmpty()){
                throw new GameException("El mazo de cartas se encuentra vacío");
            } else {
                gameUno.eatCard(humanPlayer, 1);
                setDisableButton(true);
                if (this.posInitCardToShow < this.humanPlayer.getCardsPlayer().size() - 4) {
                    posInitCardToShow = humanPlayer.getCardsPlayer().size() - 4;
                }
                printCardsHumanPlayer();
            }
        } catch (GameException e){
            System.out.println(e.getCause());
        }
    }

    /**
     * Handles the action of saying "Uno".
     *
     * @param event the action event
     */
    @FXML
    void onHandleUno(ActionEvent event) {
        threadSingUNOMachine.setButtonUNOPressed(true);
        showAdviseUnoTemporarily(adviseUnoPlayer);
    }

    /**
     * Updates visible cards in the player's hand
     */
    public void updateVisibleCardsHumanPlayer() {
        printCardsHumanPlayer();
    }

    public void checkNumberCards(int numberCards, String typePlayer) throws Exception{
        if(numberCards == 1){
            if(Objects.equals(typePlayer, "HUMAN_PLAYER")){
                setDisableButton(false);
                threadSingUNOMachine.setRunning(true);
                threadSingUNOMachine.setButtonUNOPressed(false);
            }
            else{
                showAdviseUnoTemporarily(adviseUnoMachine);
            }
        }
        else if(numberCards == 0){
            GameUnoStage.closeInstance();
            if(Objects.equals(typePlayer, "HUMAN_PLAYER")){
                WinStage.getInstance();
            }
            else{
                LoseStage.getInstance();
            }
        }
    }

    public void setDisableButton(boolean disable){
        buttonUNO.setDisable(disable);
    }

    private void showAdviseUnoTemporarily(ImageView adviseUno) {
        adviseUno.setVisible(true);
        Timeline timeline = new Timeline(new KeyFrame(
                Duration.seconds(3),
                event -> adviseUno.setVisible(false)
        ));
        timeline.setCycleCount(1);
        timeline.play(); // Start animation
    }

    public void discardCard(Card card) {
        animateCardDiscard(card);
    }

    private void animateCardDiscard(Card card) {
        ImageView cardImageView = card.getCard();
        cardImageView.setOpacity(1);
        tableImageView.setImage(card.getImage());

        TranslateTransition translate = new TranslateTransition(Duration.seconds(0.5), cardImageView);
        translate.setFromX(tableImageView.getLayoutX());
        translate.setFromY(tableImageView.getLayoutY());
        translate.setToX(tableImageView.getLayoutX() + 50);
        translate.setToY(tableImageView.getLayoutY() + 50);
        translate.play();
    }

    private void animateCardDeal(Card card, GridPane gridPane, int position) {
        ImageView cardImageView = card.getCard();
        cardImageView.setOpacity(0);
        gridPane.add(cardImageView, position, 0);

        FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.5), cardImageView);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.setDelay(Duration.seconds(position * 0.2)); // Retraso para cada carta
        fadeIn.play();
    }

    private void showUpMessage(String message){
        Label messageLabel = new Label();
        messageLabel.setAlignment(Pos.CENTER);
        messageLabel.setText(message);
    }

    public ThreadPlayMachine getThreadPlayMachine(){ return threadPlayMachine; }
}
