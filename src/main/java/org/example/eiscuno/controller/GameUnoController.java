package org.example.eiscuno.controller;

import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
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

import javafx.scene.layout.Pane;
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

    @FXML
    private ImageView deckImageView;

    @FXML
    private ImageView messageImageView;

    @FXML
    private Pane pane;

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

    /**
     * Starts the game with an animated card dealing sequence for both the human player and the machine.
     * This method initializes the game by:
     *     Starting the game logic via the {@code gameUno} object.
     *     Animating the dealing of 4 cards to the human player.
     *     Animating the dealing of 4 face-down cards to the machine player.
     *     Pausing for 2 seconds to allow the animation to complete before proceeding.
     *     Printing the human player's cards and attempting to print the machine player's cards.
     *     Drawing the first valid card for the table while ensuring it is not a special card.
     * Once a valid card is drawn, it is placed on the table, its image is updated, and the machine's play thread is started.
     *
     * @throws RuntimeException if an error occurs while printing the machine player's cards.
     */
    private void startGameWithAnimation() {
        // Start the game logic
        gameUno.startGame();
        // Animate card dealing for the human player
        for (int i = 0; i < 4; i++) {
            Card humanCard = humanPlayer.getCard(i);
            animateCardDeal(humanCard.getCard(), gridPaneCardsPlayer, i);
        }
        // Animate card dealing for the machine player (face-down cards)
        for (int i = 0; i < 4; i++) {
            ImageView machineCardImageView = new ImageView(new Image(getClass().getResource("/org/example/eiscuno/cards-uno/card_uno.png").toExternalForm()));
            machineCardImageView.setY(16);
            machineCardImageView.setFitHeight(90);
            machineCardImageView.setFitWidth(70);
            animateCardDeal(machineCardImageView, gridPaneCardsMachine, i);
        }

        // Pause for 2 seconds to allow animations to complete
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

            // Place the valid card on the table
            table.addCardOnTheTable(firstCard);
            tableImageView.setImage(firstCard.getImage());
            threadPlayMachine.start();
        });
        pause.play();
    }

    /**
     * Prints the human player's cards on the grid pane.
     */
    public void printCardsHumanPlayer() {
        this.gridPaneCardsPlayer.getChildren().clear();
        Card[] currentVisibleCardsHumanPlayer = humanPlayer.getCurrentVisibleCards(this.posInitCardToShow);

        for (int i = 0; i < currentVisibleCardsHumanPlayer.length; i++) {
            Card card = currentVisibleCardsHumanPlayer[i];
            ImageView cardImageView = card.getCard();

            cardImageView.setOnMouseClicked((MouseEvent event) -> {
                if (isCardPosible(card, table) && getCurrentTurn() == 0){
                    gameUno.playCard(card);

                    card.animateToTable(cardImageView, tableImageView);

                    humanPlayer.removeCard(findPosCardsHumanPlayer(card));
                    printCardsHumanPlayer();

                    try {
                        threadPlayMachine.setPlayerPlaying(true);
                        checkNumberCards(humanPlayer.getCardsPlayer().size(), humanPlayer.getTypePlayer(), getCurrentTurn());
                        currentState.nexTurn(this);
                        handleCardAction(machinePlayer, card);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            this.gridPaneCardsPlayer.add(cardImageView, i, 0);
        }
        updateCardsLabel("HUMAN_PLAYER");
    }

    /**
     * Displays the machine player's visible cards on the game board.
     * This method retrieves the current visible cards of the machine player and updates the
     * {@code gridPaneCardsMachine} to display them. Each card is represented visually with a face-down
     * card image. The method also updates the card count label for the machine player.
     *     Retrieves the machine player's visible cards using {@code getCurrentVisibleCards}.
     *     Clears the current content of the {@code gridPaneCardsMachine}.
     *     Adds face-down card images to the grid pane, representing each visible card.
     *     Updates the machine player's card count label.
     */
    public void printCardsMachinePlayer(){
        // Retrieve the machine player's visible cards
        Card[] currentVisibleCardsMachinePlayer = machinePlayer.getCurrentVisibleCards(0);
        // Clear the current grid pane for machine player's cards
            gridPaneCardsMachine.getChildren().clear();
            for (int i = 0; i < currentVisibleCardsMachinePlayer.length; i++){
                ImageView cardImageView = new ImageView(new Image(getClass().getResource("/org/example/eiscuno/cards-uno/card_uno.png").toExternalForm()));
                cardImageView.setY(16);
                cardImageView.setFitHeight(90);
                cardImageView.setFitWidth(70);
                gridPaneCardsMachine.add(cardImageView, i , 0);

                updateCardsLabel("MACHINE_PLAYER");
            }
    }

    /**
     * Determines if a given card can be played on the current table state.
     * A card is considered playable if it matches one of the following conditions:
     *     The card's color matches the current color on the table.
     *     The card's value matches the current number or value on the table
     *     The card is a special "WILD" card.
     *     The card is a special "FOUR" card.
     * This method checks these conditions and returns {@code true} if any of them are met.
     *
     * @param card  The {@link Card} to be evaluated for playability.
     * @param table The {@link Table} representing the current game state.
     * @return {@code true} if the card can be played; {@code false} otherwise.
     */
    public boolean isCardPosible(Card card, Table table){
        return Objects.equals(table.getCurrentColor(), card.getColor())
                || Objects.equals(table.getCurrentNum(), card.getValue())
                || Objects.equals(card.getValue(), "WILD")
                || Objects.equals(card.getValue(), "FOUR");
    }

    /**
     * Handles the action associated with a specific card played against a target player.
     * This method determines the action to take based on the value of the card:
     *     FOUR: Adds 4 cards to the target player's hand, shows the message "+4",
     *         and triggers color selection for the machine if applicable.
     *     TWO: Adds 2 cards to the target player's hand, shows the message "+2",
     *         and handles card printing based on the current player.
     *     WILD: Triggers color selection and hides the table card image.
     *     SKIP: Displays the "SKIP" message and moves to the next turn.
     *     REVERSE: Displays the "REVERSE" message and moves to the next turn.
     *     Default: If the card has no special action, it logs a message to the console.
     * Finally, the card is returned to the deck after executing its action.
     *
     * @param targetPlayer the {@link Player} who is the target of the card action
     * @param card         the {@link Card} that determines the action
     * @throws Exception if any exception occurs during the handling of the action
     */
    public void handleCardAction(Player targetPlayer, Card card) throws Exception {
        switch (card.getValue()) {
            case "FOUR":
                showUpMessage("+4");
                gameUno.eatCard(targetPlayer, 4);
                showPieAnchorPane();
                tableImageView.setVisible(false);
                if (targetPlayer == humanPlayer){
                    printCardsHumanPlayer();
                    handleMachineColorSelection();
                } else {
                    printCardsMachinePlayer();
                }
                break;
            case "TWO":
                showUpMessage("+2");
                gameUno.eatCard(targetPlayer, 2);
                if (targetPlayer == humanPlayer){
                    printCardsHumanPlayer();
                } else {
                    threadPlayMachine.setPlayerPlaying(false);
                    printCardsMachinePlayer();
                }
                break;
            case "WILD":
                showPieAnchorPane();
                tableImageView.setVisible(false);
                if (targetPlayer == humanPlayer){ handleMachineColorSelection(); }
                break;
            case "SKIP":
                showUpMessage("SKIP");
                currentState.nexTurn(this);
                break;
            case "REVERSE":
                showUpMessage("REVERSE");
                currentState.nexTurn(this);
                break;
            default:
                System.out.println("La carta no tiene ninguna característica");
                threadPlayMachine.setPlayerPlaying(false);
                break;
        }
        gameUno.getDeck().retournCard(card);
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

    /**
     * A number between 1-4 is randomly selected, which corresponds to a color
     */
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
    /**
     * Sets the table's current color to blue and updates the displayed card image accordingly.
     * This method is invoked when a "WILD" or "FOUR" card is played and the color selection is set to blue.
     * It updates the table's current color, sets the corresponding image for the card displayed on the table,
     * and hides the pie anchor pane.
     * After setting the image, the pie anchor pane is hidden, the table image is made visible,
     * and the machine player's turn is ended by setting {@code setPlayerPlaying} to false.
     * </p>
     */
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

    /**
     * Sets the table's current color to red and updates the displayed card image accordingly.
     * This method is invoked when a "WILD" or "FOUR" card is played and the color selection is set to red.
     * It updates the table's current color, sets the corresponding image for the card displayed on the table,
     * and hides the pie anchor pane.
     * After setting the image, the pie anchor pane is hidden, the table image is made visible,
     * and the machine player's turn is ended by setting {@code setPlayerPlaying} to false.
     */
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
    /**
     * Sets the table's current color to yellow and updates the displayed card image accordingly.
     * This method is invoked when a "WILD" or "FOUR" card is played and the color selection is set to yellow
     * It updates the table's current color, sets the corresponding image for the card displayed on the table,
     * and hides the pie anchor pane.
     * After setting the image, the pie anchor pane is hidden, the table image is made visible,
     * and the machine player's turn is ended by setting {@code setPlayerPlaying} to false.
     * </p>
     */
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
    /**
     * Sets the table's current color to green and updates the displayed card image accordingly.
     * This method is invoked when a "WILD" or "FOUR" card is played and the color selection is set to green.
     * It updates the table's current color, sets the corresponding image for the card displayed on the table,
     * and hides the pie anchor pane.
     * After setting the image, the pie anchor pane is hidden, the table image is made visible,
     * and the machine player's turn is ended by setting {@code setPlayerPlaying} to false.
     * </p>
     */
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
    /**
     * Sets the current state of the game.
     * This method updates the game state to the specified {@link GameState}. The state could represent
     * various phases of the game, such as the player's turn or the machine's turn.
     *
     * @param state the {@link GameState} to set as the current game state
     */
    public void setState(GameState state) {
        this.currentState = state;
    }
    /**
     * Retrieves the current state of the game.
     * This method returns the current {@link GameState} of the game, which could represent the current
     * phase, such as the player's turn, machine's turn, or other game states.
     *
     * @return the {@link GameState} representing the current state of the game
     */
    public GameState getCurrentState() {
        return currentState;
    }

    /**
     * Determines whose turn it is based on the current game state.
     * This method checks the current state and returns:
     *     {@code 0} if it is the player's turn (i.e., the state is {@link PlayerTurnState}).
     *     {@code 1} if it is the machine's turn (i.e., the state is not {@link PlayerTurnState}).
     *
     * @return {@code 0} if it's the player's turn, {@code 1} if it's the machine's turn
     */

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
    /**
     * Handles the action when the human player takes a card from the deck.
     * This method is invoked when the human player takes a card. It performs the following actions:
     *     Checks if the deck is empty, throwing a {@link GameException} if true.
     *     If the deck is not empty, a card is drawn by the human player and added to their hand.
     *     A message is displayed indicating the human player has taken a card.
     *     The player's cards are printed, and the position to show the cards is adjusted accordingly.
     *     The game state is transitioned to the next turn.
     *     The machine player's turn is ended.
     *
     * @throws GameException if the deck is empty when trying to draw a card
     */
    @FXML
    void onHandleTakeCard() {
        if (currentState instanceof PlayerTurnState){
            try {
                if (deck.isEmpty()){
                    throw new GameException("El mazo de cartas se encuentra vacío");
                } else {
                    gameUno.eatCard(humanPlayer, 1);
                    showUpMessage("HUMAN_PLAYER_TAKES");
                    if (this.posInitCardToShow < this.humanPlayer.getCardsPlayer().size() - 4) {
                        posInitCardToShow = humanPlayer.getCardsPlayer().size() - 4;
                    }
                    printCardsHumanPlayer();
                    currentState.nexTurn(this);
                    threadPlayMachine.setPlayerPlaying(false);
                }
            } catch (GameException e){
                System.out.println(e.getCause());
            }
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
    }

    /**
     * check the number of cards of the players if it is 1 then the UNO button is enabled, if it
     * is 0 a window of who has won the game is shown
     * @param numberCards Player's current number of cards
     * @param typePlayer Player Type
     * @param currentTurn Current Shift
     * @throws Exception Exception for Resource Load
     */
    public void checkNumberCards(int numberCards, String typePlayer, int currentTurn) throws Exception{
        if(numberCards == 1){
            setDisableUnoButton(false);
            threadSingUNOMachine.setRunning(true);
            threadSingUNOMachine.setCurrentTurn(currentTurn);
            threadSingUNOMachine.setButtonUNOPressed(false);
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

    public void setDisableUnoButton(boolean disable){
        buttonUNO.setDisable(disable);
    }

    /**
     * Displays a temporary announcement of the player who has pressed the UNO button
     * @param type Player's Turn
     */
    public void showAdviseUnoTemporarily(int type) {
        ImageView adviseUno;
        if (type == 0){
            adviseUno = adviseUnoPlayer;
        } else {
            adviseUno = adviseUnoMachine;
        }
        adviseUno.setVisible(true);
        Timeline timeline = new Timeline(new KeyFrame(
                Duration.seconds(3),
                event -> adviseUno.setVisible(false)
        ));
        timeline.setCycleCount(1);
        timeline.play(); // Start timeline
    }

    /**
     * Animates the card dealing process by fading in the card image.
     * This method animates the appearance of a card as it is dealt to the player or machine.
     * The card image starts with zero opacity (invisible) and gradually fades in to full opacity
     * over a duration of 0.5 seconds. The animation for each card is delayed based on its position
     * in the sequence to create a staggered effect.
     *
     * @param cardImageView the {@link ImageView} representing the card to be animated
     * @param gridPane the {@link GridPane} where the card will be added
     * @param position the position in the sequence of cards, used to delay the animation for each card
     */
    private void animateCardDeal(ImageView cardImageView, GridPane gridPane, int position) {
        cardImageView.setOpacity(0);
        gridPane.add(cardImageView, position, 0);

        FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.5), cardImageView);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.setDelay(Duration.seconds(position * 0.2));
        fadeIn.play();
    }

    /**
     * Animates the appearance of the pie anchor pane by scaling it up and fading it in.
     * This method makes the {@link AnchorPane} (pieAnchorPane) visible and animates it by scaling it from
     * half its size (50%) to its full size (100%) over a duration of 0.3 seconds. The opacity of the pane
     * is also adjusted to fade in smoothly. The scaling transition uses an ease-out interpolator for a smooth effect.
     */
    private void showPieAnchorPane(){
        pieAnchorPane.setVisible(true);
        pieAnchorPane.setOpacity(0.0);
        pieAnchorPane.setScaleX(0.5);
        pieAnchorPane.setScaleY(0.5);

        ScaleTransition scale = new ScaleTransition(Duration.seconds(0.3), pieAnchorPane);
        scale.setFromX(0.5);
        scale.setFromY(0.5);
        scale.setToX(1.0);
        scale.setToY(1.0);
        scale.setInterpolator(Interpolator.EASE_OUT);

        pieAnchorPane.setOpacity(1.0);

        scale.play();
    }
    /**
     * Displays a popup image corresponding to the given message.
     * This method checks the provided message and displays an associated image as a popup.
     * The image is selected based on the message content, which represents various actions or events
     * in the game, such as card actions or player activities. The images are loaded from the specified
     * resource folder and displayed to the user.
     *
     * @param message the message that determines which image to display. Possible values include:

     *                  "+4" for a +4 card action
     *                  "+2" for a +2 card action
     *                  "REVERSE" for a reverse card action
     *                  "SKIP" for a skip card action
     *                  "MACHINE_TAKES" when the machine takes a card
     *                  "HUMAN_PLAYER_TAKES" when the human player takes a card
     *                  Any other string will display a default "uno" image
     */
    public void showUpMessage(String message){
        Image popImage;
        String url = "/org/example/eiscuno/images/";

        switch (message){
            case "+4":
                popImage = new Image(getClass().getResource(url + "+4.png").toExternalForm());
                break;
            case "+2":
                popImage = new Image(getClass().getResource(url + "+2.png").toExternalForm());
                break;
            case "REVERSE":
                popImage = new Image(getClass().getResource(url + "reverse.png").toExternalForm());
                break;
            case "SKIP":
                popImage = new Image(getClass().getResource(url + "skip.png").toExternalForm());
                break;
            case "MACHINE_TAKES":
                popImage = new Image(getClass().getResource(url + "machineCardTake.png").toExternalForm());
                break;
            case "HUMAN_PLAYER_TAKES":
                popImage = new Image(getClass().getResource(url + "playerCardTake.png").toExternalForm());
                break;
            default:
                popImage = new Image(url + "uno.png");
        }

        messageImageView.setImage(popImage);
        messageImageView.setVisible(true);
        messageImageView.setOpacity(0.0);
        messageImageView.setScaleX(0.5);
        messageImageView.setScaleY(0.5);
        messageImageView.setFitWidth(200);
        messageImageView.setFitHeight(200);
        messageImageView.setLayoutX((pane.getWidth() - messageImageView.getFitWidth()) / 2);
        messageImageView.setLayoutY((pane.getHeight() - messageImageView.getFitHeight()) / 2);

        ScaleTransition scale = new ScaleTransition(Duration.seconds(0.3), messageImageView);
        scale.setFromX(0.5);
        scale.setFromY(0.5);
        scale.setToX(1.0);
        scale.setToY(1.0);
        scale.setInterpolator(Interpolator.EASE_OUT);

        FadeTransition fade = new FadeTransition(Duration.seconds(0.5), messageImageView);
        fade.setFromValue(1.0);
        fade.setToValue(0.0);
        fade.setDelay(Duration.seconds(0.3));

        messageImageView.setOpacity(1.0);
        scale.setOnFinished(event -> {
            fade.play();
            fade.setOnFinished(event1 -> messageImageView.setVisible(false));
        });
        scale.play();
    }
    /**
     * Updates the label displaying the number of cards the specified player has.
     * This method updates the text of the label for either the human player or the machine player,
     * displaying the number of cards remaining in the player's hand. The label will reflect the
     * appropriate count based on the provided player identifier.
     *
     * @param player the identifier of the player whose card count should be updated.
     *               Should be "HUMAN_PLAYER" for the human player, or any other value
     *               (typically "MACHINE_PLAYER") for the machine player.
     */
    public void updateCardsLabel(String player){
        if (player == "HUMAN_PLAYER"){
            humanPlayerCardsLabel.setText("Tus cartas: " + humanPlayer.getCardsPlayer().size());
        } else{
            machineCardsLabel.setText("Cartas de la máquina: " + machinePlayer.getCardsPlayer().size());
        }
    }

    /**
     * Returns the {@link GameUno} instance associated with the current game.
     * This method provides access to the {@code GameUno} object, which manages the overall state
     * and logic of the Uno game.
     *
     * @return the {@code GameUno} instance.
     */
    public  GameUno getGameUno(){
        return gameUno;
    }

    /**
     * Returns the {@link ThreadPlayMachine} instance managing the machine's actions.
     * This method provides access to the {@code ThreadPlayMachine} object, which controls the
     * behavior of the machine during its turn in the game, such as making decisions and playing cards.
     *
     * @return the {@code ThreadPlayMachine} instance.
     */
    public ThreadPlayMachine getThreadPlayMachine() { return threadPlayMachine; }

    /**
     * Closes the current game stage.
     * This method is triggered to close the game window or stage. It calls the {@code closeInstance}
     * method from the {@link GameUnoStage} class to handle the closing operation.
     */
    @FXML
    public void exit(){
        GameUnoStage.closeInstance();
    }
    /**
     * Handles key press events to trigger specific actions in the game.
     * This method listens for key press events and checks if the pressed key corresponds to a specific
     * action. If the 'T' key is pressed, it invokes the {@code onHandleTakeCard} method to handle the action
     * of taking a card. This method is bound to key press events in the UI.
     *
     * @param event the key event that contains information about the key pressed.
     */
    @FXML
    public void keyPressedHandler(KeyEvent event){
        if(event.getCode() == KeyCode.T){
            onHandleTakeCard();
        }
    }
}
