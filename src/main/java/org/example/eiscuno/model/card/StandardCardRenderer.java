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

/**
 * Renderer class for standard cards in a card game.
 * This class provides animations for rendering standard cards on the game table,
 * including movement and fade effects.
 * It uses JavaFX animation components to handle the translation, fading,
 * and parallel execution of animations.
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
 * @see TranslateTransition
 * @see FadeTransition
 * @see ParallelTransition
 */
public class StandardCardRenderer implements CardRenderer{

    /**
     * Animates a standard card moving to the table with translation and fade effects.
     * The animation consists of the card image moving from its current position
     * to the target table position while fading out. Simultaneously, the table image
     * fades in to reveal the card on the table.
     *
     * @param cardImageView the {@link javafx.scene.image.ImageView} representing the card being animated.
     * @param tableImageView the {@link javafx.scene.image.ImageView} of the game table where the card is placed.
     * @param cardImage the {@link javafx.scene.image.Image} representing the card to be displayed on the table.
     * @since 1.0
     * @serialData No serialization is performed during the animation process.
     * @see javafx.animation.TranslateTransition
     * @see javafx.animation.FadeTransition
     * @see javafx.animation.ParallelTransition
     */
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
