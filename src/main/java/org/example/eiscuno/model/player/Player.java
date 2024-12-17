package org.example.eiscuno.model.player;

import org.example.eiscuno.model.card.Card;

import java.util.ArrayList;

/**
 * Represents a player in the Uno game.
 */
public class Player extends APlayer {
    /**
     * Constructs a new Player object with an empty hand of cards.
     *
     * @param typePlayer
     */
    public Player(String typePlayer) {
        super(typePlayer);
    }
}