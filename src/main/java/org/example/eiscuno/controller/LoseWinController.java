package org.example.eiscuno.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import org.example.eiscuno.view.GameUnoStage;
import org.example.eiscuno.view.LoseStage;
import org.example.eiscuno.view.WelcomeStage;
import org.example.eiscuno.view.WinStage;

import java.io.IOException;

public class LoseWinController {
    @FXML
    public void handleClickExitLose(ActionEvent event) {
        LoseStage.closeInstance();
    }

    @FXML
    public void handleClickContinueLose(ActionEvent event) throws IOException {
        //GameUnoStage.closeInstance();
        LoseStage.closeInstance();
        //WelcomeStage.getInstance();
    }

    @FXML
    public void handleClickExitWin(ActionEvent event) {
        WinStage.closeInstance();
    }

    @FXML
    public void handleClickContinueWin(ActionEvent event) throws IOException {
        //GameUnoStage.closeInstance();
        WinStage.closeInstance();
        //WelcomeStage.getInstance();
    }
}
