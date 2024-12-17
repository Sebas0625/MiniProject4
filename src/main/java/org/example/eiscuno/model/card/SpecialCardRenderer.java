package org.example.eiscuno.model.card;

import javafx.animation.*;
import javafx.geometry.Bounds;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

/**
 * A specialized implementation of the {@link CardRenderer} interface that provides
 * animated transitions for playing a card from the player's hand to the table.
 * This class uses animations such as translation, rotation, and scaling
 * to enhance the visual experience of the game.
 */
public class SpecialCardRenderer implements CardRenderer {

    /**
     * Animates the movement of a card from a player's hand to the table.
     * Includes a translation animation, followed by rotation and scaling effects
     * to create a visually appealing card placement.
     *
     * @param cardImageView  the ImageView displaying the card to be animated.
     * @param tableImageView the ImageView representing the table where the card will be placed.
     * @param cardImage      the Image of the card to be displayed on the table.
     */
    @Override
    public void animateToTable(ImageView cardImageView, ImageView tableImageView, Image cardImage) {
        // Create a copy of the card's ImageView for animation
        ImageView animatedCard = new ImageView(cardImageView.getImage());
        animatedCard.setFitWidth(cardImageView.getFitWidth());
        animatedCard.setFitHeight(cardImageView.getFitHeight());

        // Calculate the starting and ending bounds for the animation
        Bounds startBounds = cardImageView.localToScene(cardImageView.getBoundsInLocal());
        Bounds endBounds = tableImageView.localToScene(tableImageView.getBoundsInLocal());

        // Set the initial position of the animated card
        animatedCard.setLayoutX(startBounds.getMinX());
        animatedCard.setLayoutY(startBounds.getMinY());

        // Add the animated card to the scene
        ((Pane) cardImageView.getScene().getRoot()).getChildren().add(animatedCard);

        // Create a translation animation to move the card to the table
        TranslateTransition move = new TranslateTransition(Duration.seconds(0.5), animatedCard);
        move.setFromX(0);
        move.setFromY(0);
        move.setToX(endBounds.getMinX() - startBounds.getMinX());
        move.setToY(endBounds.getMinY() - startBounds.getMinY());

        // On completion of the translation, perform rotation and scaling animations
        move.setOnFinished(event -> {
            RotateTransition rotate = new RotateTransition(Duration.seconds(0.5), tableImageView);
            rotate.setByAngle(360);

            ScaleTransition scale = new ScaleTransition(Duration.seconds(0.5), tableImageView);
            scale.setFromX(0.5);
            scale.setFromY(0.5);
            scale.setToX(1.0);
            scale.setToY(1.0);

            // On completion of the rotation, update the table's ImageView
            rotate.setOnFinished(rotateEvent -> {
                tableImageView.setImage(cardImage);

                // Remove the animated card from the scene
                ((Pane) animatedCard.getParent()).getChildren().remove(animatedCard);

                // Play the scaling animation
                scale.play();
            });

            // Play the rotation animation
            rotate.play();
        });

        // Play the translation animation
        move.play();
    }
}

