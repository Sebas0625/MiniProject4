package org.example.eiscuno.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import org.example.eiscuno.view.GameUnoStage;
import org.example.eiscuno.view.LoseStage;
import org.example.eiscuno.view.WelcomeStage;
import org.example.eiscuno.view.WinStage;

import java.io.IOException;

/**
 * Controller class for managing the actions in the Lose/Win stage of the application.
 */
public class LoseWinController {
    /**
     * Handles the "Exit" button click event when the user loses the game.
     * Closes the LoseStage and opens the WelcomeStage.
     *
     * @param event The {@code ActionEvent} triggered by the button click.
     * @throws IOException If an I/O error occurs during stage handling.
     */
    @FXML
    public void handleClickExitLose(ActionEvent event) throws IOException {
        LoseStage.closeInstance();
        WelcomeStage.getInstance();
    }

    /**
     * Handles the "Exit" button click event when the user wins the game.
     * Closes the WinStage and opens the WelcomeStage.
     *
     * @param event The {@code ActionEvent} triggered by the button click.
     * @throws IOException If an I/O error occurs during stage handling.
     */
    @FXML
    public void handleClickExitWin(ActionEvent event) throws IOException {
        WinStage.closeInstance();
        WelcomeStage.getInstance();
    }
}
