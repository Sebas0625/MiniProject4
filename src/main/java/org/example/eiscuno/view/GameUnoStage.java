package org.example.eiscuno.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import org.example.eiscuno.controller.GameUnoController;
import org.example.eiscuno.controller.WelcomeController;

import java.io.IOException;

/**
 * Represents the main stage of the Uno game application.
 * This stage displays the game interface to the user.
 */
public class GameUnoStage extends Stage {
    private GameUnoController gameUnoController;

    /**
     * Constructs a new instance of GameUnoStage.
     *
     * @throws IOException if an error occurs while loading the FXML file for the game interface.
     */
    public GameUnoStage() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/eiscuno/fxml/game-uno-view.fxml"));
        Parent root;
        try {
            root = loader.load();
        } catch (IOException e) {
            // Re-throwing the caught IOException
            throw new IOException("Error while loading FXML file", e);
        }
        this.gameUnoController = loader.getController();

        Scene scene = new Scene(root);
        // Configuring the stage
        setTitle("EISC Uno"); // Sets the title of the stage
        setScene(scene); // Sets the scene for the stage
        scene.getStylesheets().add(getClass().getResource("/org/example/eiscuno/styles/game-uno-view-style.css").toExternalForm());
        setResizable(false); // Disallows resizing of the stage
        setOnCloseRequest(WindowEvent -> {
            WindowEvent.consume();
            deleteInstance();
        });
        show(); // Displays the stage
    }

    /**
     * Holder class for the singleton instance of GameUnoStage.
     * This class ensures lazy initialization of the singleton instance.
     */
    private static class GameUnoStageHolder {
        private static GameUnoStage INSTANCE;
    }

    /**
     * Retrieves the singleton instance of GameUnoStage.
     *
     * @return the singleton instance of GameUnoStage.
     * @throws IOException if an error occurs while creating the instance.
     */
    public static GameUnoStage getInstance() throws IOException {
        return GameUnoStageHolder.INSTANCE != null ?
                GameUnoStageHolder.INSTANCE :
                (GameUnoStageHolder.INSTANCE = new GameUnoStage());
    }

    /**
     * Closes the instance of GameUnoStage.
     * This method is used to clean up resources when the game stage is no longer needed.
     */
    public static void deleteInstance() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(null);
        alert.setHeaderText("¿Seguro que desea cerrar la ventana?");
        alert.setContentText("Perderá el progreso actual.");
        if (alert.showAndWait().get() == ButtonType.OK) {
            GameUnoStageHolder.INSTANCE.close();
            GameUnoStageHolder.INSTANCE = null;
        }
    }

    /**
     * Closes the current instance of {@code WelcomeStage} and sets it to null.
     */
    public static void closeInstance() {
        GameUnoStage.GameUnoStageHolder.INSTANCE.close();
        GameUnoStage.GameUnoStageHolder.INSTANCE = null;
    }

    public GameUnoController getGameUnoController() {
        return gameUnoController;
    }
}
