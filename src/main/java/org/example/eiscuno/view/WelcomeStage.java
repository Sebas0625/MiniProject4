package org.example.eiscuno.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import org.example.eiscuno.controller.WelcomeController;

import java.io.IOException;
import javafx.scene.image.Image;
import javafx.scene.ImageCursor;

/**
 * The {@code WelcomeStage} class represents the initial welcome window for the Battleship application.
 * It extends {@code Stage} and handles the visual aspects and lifecycle of the welcome interface.
 */
public class WelcomeStage extends Stage {
    private WelcomeController welcomeController;

    /**
     * Constructs a new {@code WelcomeStage} instance, initializes the user interface
     * by loading the FXML layout, and sets the stage properties.
     *
     * @throws IOException If an error occurs during the loading of the FXML file.
     */
    public WelcomeStage() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/eiscuno/fxml/welcome-view.fxml"));
        Parent root;
        try {
            root = loader.load();
        } catch (IOException e) {
            // Re-throwing the caught IOException
            throw new IOException("Error while loading FXML file", e);
        }
        this.welcomeController = loader.getController();

        Scene scene = new Scene(root);
        // Configuring the stage
        setTitle("EISC Uno"); // Sets the title of the stage
        setScene(scene); // Sets the scene for the stage
        scene.getStylesheets().add(getClass().getResource("/org/example/eiscuno/styles/welcome-view-style.css").toExternalForm());
        setResizable(false); // Disallows resizing of the stage
        setOnCloseRequest(WindowEvent -> {
            WindowEvent.consume();
            deleteInstance();
        });
        ImageCursor customCursor = new ImageCursor(new Image(getClass().getResource("/org/example/eiscuno/images/cursor.png").toExternalForm()));
        scene.setCursor(customCursor);
        show(); // Displays the stage
    }

    /**
     * Holder class for implementing the Singleton pattern for {@code WelcomeStage}.
     */
    private static class LoseStageHolder {
        private static WelcomeStage INSTANCE;
    }

    /**
     * Retrieves the singleton instance of {@code WelcomeStage}. If it doesn't exist,
     * a new instance will be created.
     *
     * @return The singleton instance of {@code WelcomeStage}.
     * @throws IOException If an error occurs during the loading of the FXML file.
     */
    public static WelcomeStage getInstance() throws IOException {
        LoseStageHolder.INSTANCE =
                LoseStageHolder.INSTANCE != null ? LoseStageHolder.INSTANCE : new WelcomeStage();
        return LoseStageHolder.INSTANCE;
    }

    /**
     * Prompts the user for confirmation before closing the welcome stage.
     * If confirmed, the instance will be closed and set to null.
     */
    public static void deleteInstance() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(null);
        alert.setHeaderText("¿Seguro que desea cerrar la ventana?");
        alert.setContentText("Perderá el progreso actual.");
        if (alert.showAndWait().get() == ButtonType.OK) {
            LoseStageHolder.INSTANCE.close();
            LoseStageHolder.INSTANCE = null;
        }
    }

    /**
     * Closes the current instance of {@code WelcomeStage} and sets it to null.
     */
    public static void closeInstance() {
        LoseStageHolder.INSTANCE.close();
        LoseStageHolder.INSTANCE = null;
    }

    /**
     * Retrieves the {@code WelcomeController} associated with this welcome stage.
     *
     * @return The {@code WelcomeController} instance.
     */
    public WelcomeController getWelcomeController() {
        return welcomeController;
    }
}