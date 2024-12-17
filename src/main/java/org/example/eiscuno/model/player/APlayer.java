package org.example.eiscuno.model.player;

import org.example.eiscuno.model.card.Card;

import java.util.ArrayList;

public class APlayer implements IPlayer{
    private ArrayList<Card> cardsPlayer;
    private String typePlayer;

    /**
     * Constructs a new Player object with an empty hand of cards.
     */
    public APlayer(String typePlayer){
        this.cardsPlayer = new ArrayList<Card>();
        this.typePlayer = typePlayer;
    };

    /**
     * Adds a card to the player's hand.
     *
     * @param card The card to be added to the player's hand.
     */
    @Override
    public void addCard(Card card){
        cardsPlayer.add(card);
    }

    /**
     * Retrieves all cards currently held by the player.
     *
     * @return An ArrayList containing all cards in the player's hand.
     */
    @Override
    public ArrayList<Card> getCardsPlayer() {
        return cardsPlayer;
    }

    /**
     * Removes a card from the player's hand based on its index.
     *
     * @param index The index of the card to remove.
     */
    @Override
    public void removeCard(int index) {
        cardsPlayer.remove(index);
    }

    /**
     * Retrieves a card from the player's hand based on its index.
     *
     * @param index The index of the card to retrieve.
     * @return The card at the specified index in the player's hand.
     */
    @Override
    public Card getCard(int index){
        return cardsPlayer.get(index);
    }

    /**
     * Retrieves an array of the currently visible cards for a player, starting from a given position.
     * This method returns up to 4 cards, depending on the total number of cards the player holds
     * and the specified starting position.
     *
     * @param posInitCardToShow The starting position in the player's card list from which
     *                          visible cards should be retrieved.
     * @return An array of {@code Card} objects representing the visible cards.
     *         The array will contain up to 4 cards or fewer if the starting position leaves
     *         fewer than 4 cards available.
     */
    @Override
    public Card[] getCurrentVisibleCards(int posInitCardToShow){
        int totalCards = cardsPlayer.size();
        int numVisibleCards = Math.min(4, totalCards - posInitCardToShow);
        Card[] cards = new Card[numVisibleCards];

        for (int i = 0; i < numVisibleCards; i++) {
            cards[i] = getCard(posInitCardToShow + i);
        }

        return cards;
    }

    public String getTypePlayer() {
        return typePlayer;
    }
}
