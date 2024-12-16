package org.example.eiscuno.model.card;

import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class SpecialCardRenderer implements CardRenderer {

    @Override
    public void animateToTable(ImageView tableImageView, Image cardImage) {
        RotateTransition rotate = new RotateTransition(Duration.seconds(0.5), tableImageView);
        rotate.setByAngle(360);
        rotate.setOnFinished(event -> {
            ScaleTransition scale = new ScaleTransition(Duration.seconds(0.5), tableImageView);
            scale.setFromX(0.5);
            scale.setFromY(0.5);
            scale.setToX(1.0);
            scale.setToY(1.0);
            scale.setOnFinished(scaleEvent -> {
                tableImageView.setImage(cardImage);
            });
            scale.play();
        });

        rotate.play();
    }
}
