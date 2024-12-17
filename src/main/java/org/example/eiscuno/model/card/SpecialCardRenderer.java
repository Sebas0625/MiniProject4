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

/**
 * Renderer class for special cards in a card game.
 * This class provides animations for rendering special cards on the game table,
 * including rotation and scaling effects.
 *
 * @author Juan Sebástian Sierra Cruz
 * @code 202343656
 * @author Maycol Andres Taquez Carlosama
 * @code 202375000
 * @author Santiago Valencia Aguiño
 * @code 202343334
 * @version 1.0
 * @since 1.0
 * @see CardRenderer
 * @see RotateTransition
 * @see ScaleTransition
 * @see ImageView
 */
public class SpecialCardRenderer implements CardRenderer {

    /**
     * Animates a special card moving to the table with rotation and scaling effects.
     * The animation consists of a rotation transition, which rotates the table's {@link ImageView}
     * by 360 degrees, followed by a scaling transition that resizes it to its original size.
     *
     * @param cardImageView the {@link ImageView} representing the card being animated.
     * @param tableImageView the {@link ImageView} of the game table where the card is placed.
     * @param cardImage the {@link Image} representing the card to be displayed on the table.
     * @since 1.0
     * @serialData The animations in this method do not serialize any data.
     * @see RotateTransition
     * @see ScaleTransition
     */
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
