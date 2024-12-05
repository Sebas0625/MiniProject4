package org.example.eiscuno.controller;


import javafx.event.ActionEvent;
import org.example.eiscuno.view.GameUnoStage;
import org.example.eiscuno.view.WelcomeStage;

import java.io.IOException;

public class WelcomeController {

    public void handlePlay(ActionEvent event) throws IOException {
        GameUnoStage.getInstance();
        WelcomeStage.closeInstance();
    }

    public void handleCredits(ActionEvent event){

    }

    public void handleExit(ActionEvent event){
        WelcomeStage.deleteInstance();
    }
}
