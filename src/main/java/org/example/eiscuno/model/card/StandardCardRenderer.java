package org.example.eiscuno.model.card;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.animation.ScaleTransition;
import javafx.geometry.Bounds;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class StandardCardRenderer implements CardRenderer{

    @Override
    public void animateToTable(ImageView cardImageView, ImageView tableImageView, Image cardImage) {
        Bounds startBounds = cardImageView.localToScene(cardImageView.getBoundsInLocal());
        Bounds endBounds = tableImageView.localToScene(tableImageView.getBoundsInLocal());

        ImageView animatedCard = new ImageView(cardImageView.getImage());
        animatedCard.setFitWidth(cardImageView.getFitWidth());
        animatedCard.setFitHeight(cardImageView.getFitHeight());
        animatedCard.setLayoutX(startBounds.getMinX());
        animatedCard.setLayoutY(startBounds.getMinY());

        ((Pane) cardImageView.getScene().getRoot()).getChildren().add(animatedCard);

        TranslateTransition translate = new TranslateTransition(Duration.seconds(1), animatedCard);
        translate.setFromX(0);
        translate.setFromY(0);
        translate.setToX(endBounds.getMinX() - startBounds.getMinX());
        translate.setToY(endBounds.getMinY() - startBounds.getMinY());

        FadeTransition fade = new FadeTransition(Duration.seconds(3.5), animatedCard);
        fade.setFromValue(1.0);
        fade.setToValue(0.0);

        ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(0.5), animatedCard);
        scaleTransition.setFromX(1.0);
        scaleTransition.setFromY(1.0);
        scaleTransition.setToX(1.5);
        scaleTransition.setToY(1.5);
        scaleTransition.setCycleCount(1);
        scaleTransition.setAutoReverse(false);

        translate.setOnFinished(event -> {
            tableImageView.setImage(cardImage);
            tableImageView.setOpacity(0.0);

            FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.5), tableImageView);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);
            fadeIn.play();

            ((Pane) animatedCard.getParent()).getChildren().remove(animatedCard);
        });

        ParallelTransition parallel = new ParallelTransition(translate, fade, scaleTransition);
        parallel.play();
    }
}
