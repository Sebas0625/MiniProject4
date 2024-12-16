package org.example.eiscuno.model.card;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public interface CardRenderer {
    void animateToTable(ImageView cardImageView, ImageView tableImageView, Image cardImage);
}
