package org.example.eiscuno.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;
import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.deck.Deck;
import org.example.eiscuno.model.game.GameUno;
import org.example.eiscuno.model.machine.ThreadSingUNOMachine;
import org.example.eiscuno.model.player.Player;
import org.example.eiscuno.model.table.Table;

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
    private Button buttonUNO;

    @FXML
    private ImageView adviseUnoPlayer;

    @FXML
    private ImageView adviseUnoMachine;

    private Player humanPlayer;
    private Player machinePlayer;
    private Deck deck;
    private Table table;
    private GameUno gameUno;
    private int posInitCardToShow;

    private ThreadSingUNOMachine threadSingUNOMachine;
    /**
     * Initializes the controller.
     */
    @FXML
    public void initialize() {
        initVariables();
        this.gameUno.startGame();
        printCardsHumanPlayer();
        buttonUNO.setDisable(true);

        threadSingUNOMachine = new ThreadSingUNOMachine(this.gameUno, this);
        Thread t = new Thread(threadSingUNOMachine, "ThreadSingUNO");
        // Convert the thread to a daemon thread
        t.setDaemon(true); // Alternativa para que el programa cierre junto con el hilo.
        t.start();
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
                // Aqui deberian verificar si pueden en la tabla jugar esa carta
                gameUno.playCard(card);
                tableImageView.setImage(card.getImage());
                humanPlayer.removeCard(findPosCardsHumanPlayer(card));

                // Se verifica el numero de cartas del jugador
                checkNumberCards(humanPlayer.getCardsPlayer().size(), humanPlayer.getTypePlayer());
                // se verifica el numero de cartas de la maquina, pero este metodo debe ser llamado desde
                // el metodo donde la maquina agrega sus sus cartas, !!!!!!!!!!
                checkNumberCards(machinePlayer.getCardsPlayer().size(), machinePlayer.getTypePlayer());

                printCardsHumanPlayer();
            });

            this.gridPaneCardsPlayer.add(cardImageView, i, 0);
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
     * Handles the action of taking a card.
     *
     * @param event the action event
     */
    @FXML
    void onHandleTakeCard(ActionEvent event) {
        // Implement logic to take a card here
    }

    /**
     * Handles the action of saying "Uno".
     *
     * @param event the action event
     */
    @FXML
    void onHandleUno(ActionEvent event) {
        // Se impide que en la Clase ThreadSingUnoMachine, se agregue una carta, ya que el boton uno ha sido presionado
        threadSingUNOMachine.setButtonUNOPressed(true);
        showAdviseUnoTemporarily(adviseUnoPlayer);
    }

    /**
     * Updates visible cards in the player's hand
     */
    public void updateVisibleCardsHumanPlayer() {
        printCardsHumanPlayer();
    }

    // Verifica que el numero de cartas del jugador humano o maquina tengan una sola carta
    public void checkNumberCards(int numberCards, String typePlayer) {
        if(numberCards == 1){
            if(Objects.equals(typePlayer, "HUMAN_PLAYER")){
                // Se habilita el boton uno
                setDisableButton(false);
                // Activa la ejecución del hilo
                threadSingUNOMachine.setRunning(true);
                // Se da permiso a la variable booleana en la Clase ThreadSingUnoMachine, para que el metodo pueda agregar
                // a la mano del jugador una carta si este no ha presionado el boton uno.
                threadSingUNOMachine.setButtonUNOPressed(false);
            }
            else{
                showAdviseUnoTemporarily(adviseUnoMachine);
            }
        }
    }

    public void setDisableButton(boolean disable){
        buttonUNO.setDisable(disable);
    }

    // Muestra un aviso temporal si el jugador ha presionado uno o la maquina ha dicho uno
    private void showAdviseUnoTemporarily(ImageView adviseUno) {
        adviseUno.setVisible(true);
        Timeline timeline = new Timeline(new KeyFrame(
                Duration.seconds(2),
                event -> adviseUno.setVisible(false)
        ));
        timeline.setCycleCount(1);
        timeline.play(); // Start animation
    }
}
