package org.example.eiscuno.model.table;

import org.example.eiscuno.model.card.Card;

import java.util.ArrayList;

/**
 * Represents the table in the Uno game where cards are played.
 */
public class Table {
    private final ArrayList<Card> cardsTable;
    private String currentColor;
    private String currentNum;

    /**
     * Constructs a new Table object with no cards on it.
     */
    public Table(){ this.cardsTable = new ArrayList<Card>(); }

    /**
     * Adds a card to the table.
     *
     * @param card The card to be added to the table.
     */
    public void addCardOnTheTable(Card card){
        this.cardsTable.add(card);
        currentColor = card.getColor();
        currentNum = card.getValue();
    }

    /**
     * Retrieves the current card on the table.
     *
     * @return The card currently on the table.
     * @throws IndexOutOfBoundsException if there are no cards on the table.
     */
    public Card getCurrentCardOnTheTable() throws IndexOutOfBoundsException {
        if (cardsTable.isEmpty()) {
            throw new IndexOutOfBoundsException("There are no cards on the table.");
        }
        return this.cardsTable.get(this.cardsTable.size()-1);
    }

    public void setCurrentColor(String color){
        this.currentColor = color;
    }

    public String getCurrentColor(){ return currentColor; }

    public String getCurrentNum(){ return currentNum; }
}
