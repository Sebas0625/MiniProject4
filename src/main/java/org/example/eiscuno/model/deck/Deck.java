package org.example.eiscuno.model.deck;

import org.example.eiscuno.model.card.SpecialCardRenderer;
import org.example.eiscuno.model.card.StandardCardRenderer;
import org.example.eiscuno.model.unoenum.EISCUnoEnum;
import org.example.eiscuno.model.card.Card;

import java.util.*;

/**
 * Represents a deck of Uno cards, containing various card types and colors.
 * The deck supports initialization, shuffling, card drawing, and checking if it is empty.
 */
public class Deck {
    private Queue<Card> deckOfCards;

    /**
     * Constructs a new deck of Uno cards and initializes it with all possible card combinations.
     */
    public Deck() {
        deckOfCards = new LinkedList<>();
        initializeDeck();
    }

    /**
     * Initializes the deck by creating cards based on the {@link EISCUnoEnum} values.
     * Cards are assigned a renderer based on their type (Standard or Special).
     * The deck is shuffled after initialization.
     */
    private void initializeDeck() {
        for (EISCUnoEnum cardEnum : EISCUnoEnum.values()) {
            if (cardEnum.name().startsWith("GREEN_") ||
                    cardEnum.name().startsWith("YELLOW_") ||
                    cardEnum.name().startsWith("BLUE_") ||
                    cardEnum.name().startsWith("RED_")) {
                Card card = new Card(cardEnum.getFilePath(),
                        getCardValue(cardEnum.name()), getCardColor(cardEnum.name()), new StandardCardRenderer());
                deckOfCards.add(card);
            } else if (cardEnum.name().startsWith("SKIP_") ||
                    cardEnum.name().startsWith("REVERSE_") ||
                    cardEnum.name().startsWith("TWO_WILD_DRAW_") ||
                    cardEnum.name().equals("FOUR_WILD_DRAW") ||
                    cardEnum.name().equals("WILD")) {
                Card card = new Card(cardEnum.getFilePath(), getCardValue(cardEnum.name()),
                        getCardColor(cardEnum.name()), new SpecialCardRenderer());
                deckOfCards.add(card);
            }
        }
        Collections.shuffle((List<?>) deckOfCards);
    }

    /**
     * Retrieves the value of a card based on its name.
     *
     * @param name the name of the card
     * @return the value of the card, or null if it cannot be determined
     */
    private String getCardValue(String name) {
        if (name.endsWith("0")) {
            return "0";
        } else if (name.endsWith("1")) {
            return "1";
        } else if (name.endsWith("2")) {
            return "2";
        } else if (name.endsWith("3")) {
            return "3";
        } else if (name.endsWith("4")) {
            return "4";
        } else if (name.endsWith("5")) {
            return "5";
        } else if (name.endsWith("6")) {
            return "6";
        } else if (name.endsWith("7")) {
            return "7";
        } else if (name.endsWith("8")) {
            return "8";
        } else if (name.endsWith("9")) {
            return "9";
        } else if (name.startsWith("REVERSE_")) {
            return "REVERSE";
        } else if (name.startsWith("WILD")) {
            return "WILD";
        } else if (name.startsWith("TWO_WILD_DRAW_")) {
            return "TWO";
        } else if (name.equals("FOUR_WILD_DRAW")) {
            return "FOUR";
        } else if (name.startsWith("SKIP_")) {
            return "SKIP";
        } else {
            return null;
        }
    }

    /**
     * Determines the color of a card based on its name.
     *
     * @param name the name of the card
     * @return the color of the card, or null if it cannot be determined
     */
    private String getCardColor(String name) {
        if (name.startsWith("GREEN") || name.endsWith("GREEN")) {
            return "GREEN";
        } else if (name.startsWith("YELLOW") || name.endsWith("YELLOW")) {
            return "YELLOW";
        } else if (name.startsWith("BLUE") || name.endsWith("BLUE")) {
            return "BLUE";
        } else if (name.startsWith("RED") || name.endsWith("RED")) {
            return "RED";
        } else if (name.endsWith("DRAW")) {
            return "DRAW";
        } else if (name.equals("WILD")) {
            return "WILD";
        } else {
            return null;
        }
    }

    /**
     * Takes a card from the top of the deck.
     *
     * @return the card from the top of the deck
     * @throws IllegalStateException if the deck is empty
     */
    public Card takeCard() {
        if (deckOfCards.isEmpty()) {
            throw new IllegalStateException("No hay más cartas en el mazo.");
        }
        return deckOfCards.remove();
    }

    /**
     * Returns a card to the deck.
     *
     * @param card the card to return to the deck
     */
    public void retournCard(Card card) {
        deckOfCards.add(card);
    }

    /**
     * Checks if the deck is empty.
     *
     * @return true if the deck is empty, false otherwise
     */
    public boolean isEmpty() {
        return deckOfCards.isEmpty();
    }
}

