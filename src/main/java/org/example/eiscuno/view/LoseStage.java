package org.example.eiscuno.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.ImageCursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.example.eiscuno.controller.LoseWinController;

import java.io.IOException;

/**
 * The {@code LoseStage} class represents the game status window for the Battleship application.
 * It extends {@code Stage} and handles the visual aspects and lifecycle of the welcome interface.
 */
public class LoseStage extends Stage {
    private LoseWinController loseWinController;

    /**
     * Constructs a new {@code LoseStage} instance, initializes the user interface
     * by loading the FXML layout, and sets the stage properties.
     *
     * @throws IOException If an error occurs during the loading of the FXML file.
     */
    public LoseStage() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/eiscuno/fxml/lose-view.fxml"));
        Parent root;
        try {
            root = loader.load();
        } catch (IOException e) {
            // Re-throwing the caught IOException
            throw new IOException("Error while loading FXML file", e);
        }
        this.loseWinController = loader.getController();

        Scene scene = new Scene(root);
        // Configuring the stage
        setTitle("EISC Uno"); // Sets the title of the stage
        setScene(scene); // Sets the scene for the stage
        initStyle(StageStyle.UNDECORATED);
        scene.getStylesheets().add(getClass().getResource("/org/example/eiscuno/styles/lose-win-view-style.css").toExternalForm());
        setResizable(false); // Disallows resizing of the stage

        ImageCursor customCursor = new ImageCursor(new Image(getClass().getResource("/org/example/eiscuno/images/cursor.png").toExternalForm()));
        scene.setCursor(customCursor);
        show(); // Displays the stage
    }

    /**
     * Holder class for implementing the Singleton pattern for {@code LoseStage}.
     */
    private static class LoseStageHolder {
        private static LoseStage INSTANCE;
    }

    /**
     * Retrieves the singleton instance of {@code LoseStage}. If it doesn't exist,
     * a new instance will be created.
     *
     * @return The singleton instance of {@code LoseStage}.
     * @throws IOException If an error occurs during the loading of the FXML file.
     */
    public static LoseStage getInstance() throws IOException {
        LoseStageHolder.INSTANCE =
                LoseStageHolder.INSTANCE != null ? LoseStageHolder.INSTANCE : new LoseStage();
        return LoseStageHolder.INSTANCE;
    }

    /**
     * Closes the current instance of {@code LoseStage} and sets it to null.
     */
    public static void closeInstance() {
        LoseStageHolder.INSTANCE.close();
        LoseStageHolder.INSTANCE = null;
    }

    /**
     * Retrieves the {@code LoseWinController} associated with this Lose stage.
     *
     * @return The {@code LoseWinController} instance.
     */
    public LoseWinController getLoseWinController() {
        return loseWinController;
    }
}
