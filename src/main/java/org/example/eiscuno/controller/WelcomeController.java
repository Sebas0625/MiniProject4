package org.example.eiscuno.controller;


import javafx.event.ActionEvent;
import org.example.eiscuno.view.GameUnoStage;
import org.example.eiscuno.view.LoseStage;
import org.example.eiscuno.view.WelcomeStage;
import org.example.eiscuno.view.WinStage;

import java.io.IOException;

public class WelcomeController {

    public void handlePlay(ActionEvent event) throws IOException {
        GameUnoStage.getInstance();
        WelcomeStage.closeInstance();
    }

    public void handleCredits(ActionEvent event) throws IOException {
        WinStage.getInstance();
    }

    public void handleExit(ActionEvent event){
        WelcomeStage.deleteInstance();
    }
}
