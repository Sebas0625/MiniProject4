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
    public void animateToTable(ImageView cardImageView, ImageView tableImageView, Image cardImage) {
        // Get absolute bounds
        Bounds startBounds = cardImageView.localToScene(cardImageView.getBoundsInLocal());
        Bounds endBounds = tableImageView.localToScene(tableImageView.getBoundsInLocal());

        // Make a copy of the card
        ImageView animatedCard = new ImageView(cardImageView.getImage());
        animatedCard.setLayoutX(startBounds.getMinX());
        animatedCard.setLayoutY(startBounds.getMinY());
        animatedCard.setFitWidth(cardImageView.getFitWidth());
        animatedCard.setFitHeight(cardImageView.getFitHeight());

        // Add copy to root layout
        ((Pane) cardImageView.getScene().getRoot()).getChildren().add(animatedCard);

        // Translate transition
        TranslateTransition move = new TranslateTransition(Duration.seconds(0.5), animatedCard);
        move.setFromX(0);
        move.setFromY(0);
        move.setToX(endBounds.getMinX() - startBounds.getMinX());
        move.setToY(endBounds.getMinY() - startBounds.getMinY());

        // Scale transition
        ScaleTransition scale = new ScaleTransition(Duration.seconds(0.5), tableImageView);
        scale.setFromX(0.5);
        scale.setFromY(0.5);
        scale.setToX(1.0);
        scale.setToY(1.0);

        move.setOnFinished(event -> {
            RotateTransition rotate = new RotateTransition(Duration.seconds(0.5), tableImageView);
            rotate.setByAngle(360);

            rotate.setOnFinished(rotateEvent -> {
                tableImageView.setImage(cardImage);
                ((Pane) animatedCard.getParent()).getChildren().remove(animatedCard); // Quitar la carta animada
            });

            rotate.play();
        });

        ParallelTransition parallel = new ParallelTransition(move, scale);
        parallel.play();
    }
}
