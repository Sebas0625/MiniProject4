package org.example.eiscuno.model.card;

import javafx.animation.*;
import javafx.geometry.Bounds;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import javafx.animation.RotateTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class SpecialCardRenderer implements CardRenderer {

    @Override
        public void animateToTable (ImageView cardImageView, ImageView tableImageView, Image cardImage){
        ImageView animatedCard = new ImageView(cardImageView.getImage());
        animatedCard.setFitWidth(cardImageView.getFitWidth());
        animatedCard.setFitHeight(cardImageView.getFitHeight());

        Bounds startBounds = cardImageView.localToScene(cardImageView.getBoundsInLocal());
        Bounds endBounds = tableImageView.localToScene(tableImageView.getBoundsInLocal());

        animatedCard.setLayoutX(startBounds.getMinX());
        animatedCard.setLayoutY(startBounds.getMinY());

        ((Pane) cardImageView.getScene().getRoot()).getChildren().add(animatedCard);

        TranslateTransition move = new TranslateTransition(Duration.seconds(0.5), animatedCard);
        move.setFromX(0);
        move.setFromY(0);
        move.setToX(endBounds.getMinX() - startBounds.getMinX());
        move.setToY(endBounds.getMinY() - startBounds.getMinY());

        move.setOnFinished(event -> {
            RotateTransition rotate = new RotateTransition(Duration.seconds(0.5), tableImageView);
            rotate.setByAngle(360);

            ScaleTransition scale = new ScaleTransition(Duration.seconds(0.5), tableImageView);
            scale.setFromX(0.5);
            scale.setFromY(0.5);
            scale.setToX(1.0);
            scale.setToY(1.0);

            rotate.setOnFinished(rotateEvent -> {
                tableImageView.setImage(cardImage);
                ((Pane) animatedCard.getParent()).getChildren().remove(animatedCard); // Quitar la carta animada
                scale.play();
            });

            rotate.play();
        });

        move.play();
    }
}
