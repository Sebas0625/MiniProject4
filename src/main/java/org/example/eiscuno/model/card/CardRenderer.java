package org.example.eiscuno.model.card;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Interface that defines the behavior for rendering and animating a card
 * from the player's hand to the table.
 */
public interface CardRenderer {

    /**
     * Animates the movement of a card image from a source ImageView
     * to the table ImageView.
     *
     * @param cardImageView  the ImageView displaying the card to be animated.
     * @param tableImageView the ImageView representing the table where the card will be placed.
     * @param cardImage      the Image of the card to be displayed on the table.
     */
    void animateToTable(ImageView cardImageView, ImageView tableImageView, Image cardImage);
}
