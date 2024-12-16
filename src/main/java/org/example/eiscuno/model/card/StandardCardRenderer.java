package org.example.eiscuno.model.card;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class StandardCardRenderer implements CardRenderer{

    @Override
    public void animateToTable(ImageView tableImageView, Image cardImage) {
        FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.5), tableImageView);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);

        fadeOut.setOnFinished(event -> {
            tableImageView.setImage(cardImage);

            FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.5), tableImageView);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);
            fadeIn.play();
        });

        fadeOut.play();
    }

}
