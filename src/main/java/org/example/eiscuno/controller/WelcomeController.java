package org.example.eiscuno.controller;


import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.example.eiscuno.view.GameUnoStage;
import org.example.eiscuno.view.LoseStage;
import org.example.eiscuno.view.WelcomeStage;
import org.example.eiscuno.view.WinStage;

import java.io.IOException;

public class WelcomeController {
    /**
     * Handles the play button action to start the game.
     * This method is triggered when the user clicks the play button. It initializes the game by calling
     * {@code getInstance} on the {@link GameUnoStage} class to display the game stage and then closes
     * the welcome stage using {@code closeInstance} from the {@link WelcomeStage} class.
     *
     * @param event the event that triggered the method, typically a button click.
     * @throws IOException if an input or output error occurs while initializing the game stage.
     */
    public void handlePlay(ActionEvent event) throws IOException {
        GameUnoStage.getInstance();
        WelcomeStage.closeInstance();
    }

    /**
     * Displays a window with the names of the UNO game creators and their respective codes
     * @param event Wait for a click event
     * @throws IOException Resource Loading
     */
    public void handleCredits(ActionEvent event) throws IOException {
        Stage creditsStage = new Stage();
        creditsStage.setTitle("Créditos");

        creditsStage.initModality(Modality.WINDOW_MODAL);
        creditsStage.initStyle(StageStyle.UNDECORATED);

        Label titleLabel = new Label("CRÉDITOS");
        titleLabel.setStyle("-fx-text-fill: white;" +
                "-fx-font-size: 24px;" +
                "-fx-font-weight: bold;" +
                "-fx-padding: 10px;");

        Label creditLabel1 = new Label("Desarrollado por: Juan Sebástian Sierra, Maycol Andrés Taquez y Santiago Valencia ");
        Label creditLabel2 = new Label("Códigos: 202343656 - 202375000 - 202343334");
        Label creditLabel4 = new Label("Materia: Programación Orientada a Eventos - 2024-2");

        String labelStyle = "-fx-text-fill: white; -fx-text-alignment: center;";
        creditLabel1.setStyle(labelStyle);
        creditLabel2.setStyle(labelStyle);
        creditLabel4.setStyle(labelStyle);

        creditLabel1.setWrapText(true);
        creditLabel2.setWrapText(true);
        creditLabel4.setWrapText(true);

        Button closeButton = new Button("Cerrar");
        closeButton.setOnAction(e -> creditsStage.close());
        closeButton.setStyle(
                "-fx-background-color: #c900ff;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-size: 14px;" +
                        "-fx-border-color: white;" +
                        "-fx-border-width: 2px;" +
                        "-fx-border-radius: 10px;" +
                        "-fx-background-radius: 10px;" +
                        "-fx-padding: 5px 10px;"
        );
        VBox layout = new VBox(10);
        layout.getChildren().addAll(titleLabel, creditLabel1, creditLabel2, creditLabel4, closeButton);
        layout.setStyle("-fx-background-color: rgba(0,0,0,0.4);");
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 350, 250);
        creditsStage.setScene(scene);
        creditsStage.showAndWait();
    }

    /**
     *
     Close the WelcomeStage instance
     * @param event Wait for a click event
     */
    public void handleExit(ActionEvent event){
        WelcomeStage.deleteInstance();
    }
}
