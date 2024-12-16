package org.example.eiscuno.model.card;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class StandardCardRenderer implements CardRenderer{

    @Override
    public void animateToTable(ImageView cardImageView, ImageView tableImageView, Image cardImage) {
        double startX = cardImageView.getLayoutX();
        double startY = cardImageView.getLayoutY();

        double endX = tableImageView.getLayoutX();
        double endY = tableImageView.getLayoutY();

        TranslateTransition translate = new TranslateTransition(Duration.seconds(0.5), cardImageView);
        translate.setFromX(0);
        translate.setFromY(0);
        translate.setToX(endX - startX);
        translate.setToY(endY - startY);

        FadeTransition fade = new FadeTransition(Duration.seconds(0.5), cardImageView);
        fade.setFromValue(1.0);
        fade.setToValue(0.0);

        translate.setOnFinished(event -> {
            tableImageView.setImage(cardImage);
            tableImageView.setOpacity(0.0);

            FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.5), tableImageView);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);
            fadeIn.play();
        });

        ParallelTransition parallel = new ParallelTransition(translate, fade);
        parallel.play();
    }
}
