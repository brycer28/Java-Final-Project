package Logic;

import java.util.ArrayList;

public class Solitaire {
    Deck deck;
    ArrayList<ArrayList<Card>> foundation;
    ArrayList<ArrayList<Card>> column;
    int deckIndex = 0; // index number for the currently displayed card, -1 if none

    public Solitaire() {
        deck = new Deck();
        initFoundation();
        initColumn();

    }

    /**
     * Gets the current top of the deck without incrementing deck
     */
    public Card peekTopCard() {
        return deck.get(deckIndex);
    }

    /**
     * Gets the current top of the deck without incrementing deck
     */
    public Card peekDisplayedCard() {
        if (deckIndex == -1) {
            return null;
        }
        return deck.get(deckIndex);
    }

    /**
     * Revoves the card that was most recently fliped
     */
    public Card removeDisplayedCard() {
        if (deckIndex == -1) {
            System.out.println("Error getting card, deck is empty");
            return null;
        }
        System.out.println("deck index is " + deckIndex);
        Card card = deck.get(deckIndex);
        deck.remove(deckIndex);
        deckIndex += 1;
        if (deckIndex >= deck.size()) {
            deckIndex = 0;
        }
        return card;

    }

    /**
     * Removes the top card off of the deck
     * Increments the index
     */
    public Card removeTopCard() {
        if (deck.size() < 1) {
            System.out.println("Error getting card, deck is empty");
            return null;
        }
        System.out.println("deck index is " + deckIndex);
        Card card = deck.get(deckIndex);
        deck.remove(deckIndex);
        if (deckIndex >= deck.size()) {
            deckIndex = 0;
        }
        return card;
    }

    /**
     * increments the card index and returns the card that was on top
     *
     * @return card that was taken off of the top of the deck
     */
    public Card flipTopCard() {
        if (deck.size() < 1) {
            System.out.println("Error getting card, deck is empty");
        }
        System.out.println("deck index is " + deckIndex);
        Card card = deck.get(deckIndex);
        deckIndex += 1;
        if (deckIndex >= deck.size()) {
            deckIndex = 0;
        }
        return card;
    }

    public int getDeckIndex() {
        return deckIndex;
    }

    /**
     * Intitializes the foundation ArrayList
     */
    private void initFoundation() {
        foundation = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            foundation.add(new ArrayList<>());
        }
    }

    /**
     * Intitializes the column ArrayList
     */
    private void initColumn() {
        column = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            column.add(new ArrayList<>());
        }
    }
}
