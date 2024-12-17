package org.example.eiscuno.model.card;

import javafx.animation.*;
import javafx.geometry.Bounds;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

/**
 * Implementation of the {@link CardRenderer} interface for animating
 * standard cards as they move from the player's hand to the table.
 * This renderer applies animations such as translation, fading, and scaling
 * to provide a smooth and visually appealing card placement effect.
 */
public class StandardCardRenderer implements CardRenderer {

    /**
     * Animates the movement of a card from the player's hand to the table.
     * The animation includes translation to the table's position, a fading effect,
     * and scaling the card during the animation.
     *
     * @param cardImageView  the ImageView displaying the card to animate.
     * @param tableImageView the ImageView representing the table where the card will be placed.
     * @param cardImage      the Image of the card to be displayed on the table.
     */
    @Override
    public void animateToTable(ImageView cardImageView, ImageView tableImageView, Image cardImage) {
        // Determine the starting and ending positions for the card animation
        Bounds startBounds = cardImageView.localToScene(cardImageView.getBoundsInLocal());
        Bounds endBounds = tableImageView.localToScene(tableImageView.getBoundsInLocal());

        // Create a copy of the card's ImageView for the animation
        ImageView animatedCard = new ImageView(cardImageView.getImage());
        animatedCard.setFitWidth(cardImageView.getFitWidth());
        animatedCard.setFitHeight(cardImageView.getFitHeight());
        animatedCard.setLayoutX(startBounds.getMinX());
        animatedCard.setLayoutY(startBounds.getMinY());

        // Add the animated card to the scene
        ((Pane) cardImageView.getScene().getRoot()).getChildren().add(animatedCard);

        // Create a translation animation to move the card to the table's position
        TranslateTransition translate = new TranslateTransition(Duration.seconds(1), animatedCard);
        translate.setFromX(0);
        translate.setFromY(0);
        translate.setToX(endBounds.getMinX() - startBounds.getMinX());
        translate.setToY(endBounds.getMinY() - startBounds.getMinY());

        // Create a fade-out animation for the animated card
        FadeTransition fade = new FadeTransition(Duration.seconds(3.5), animatedCard);
        fade.setFromValue(1.0);
        fade.setToValue(0.0);

        // Create a scaling animation for the animated card
        ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(0.5), animatedCard);
        scaleTransition.setFromX(1.0);
        scaleTransition.setFromY(1.0);
        scaleTransition.setToX(1.5);
        scaleTransition.setToY(1.5);
        scaleTransition.setCycleCount(1);
        scaleTransition.setAutoReverse(false);

        // Handle the end of the translation animation
        translate.setOnFinished(event -> {
            // Update the table's ImageView with the card image
            tableImageView.setImage(cardImage);
            tableImageView.setOpacity(0.0);

            // Create a fade-in effect for the table's ImageView
            FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.5), tableImageView);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);
            fadeIn.play();

            // Remove the animated card from the scene
            ((Pane) animatedCard.getParent()).getChildren().remove(animatedCard);
        });

        // Combine the animations into a parallel transition and play them together
        ParallelTransition parallel = new ParallelTransition(translate, fade, scaleTransition);
        parallel.play();
    }
}
