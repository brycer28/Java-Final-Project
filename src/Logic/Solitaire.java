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
     * Checks to see if a card can be added to a column. If it can, the card
     * is added and true is returned. If it cannot, the card is not added and
     * false if returned
     */
    public boolean addToColumn(int columnNum, Card card) {
        // first checks if the column is empty and if so adds the card
        if (column.get(columnNum).size() == 0) {
            column.get(columnNum).add(card);
            return true;
        }
        // checks of the card is the oppisite suit and return false if not
        if (!isOppositeSuit(column.get(columnNum).getLast(), card)) {
            return false;
        }

        // checks if the card is the next lowest card and returns ture if yes
        if (isNextLowestCard(column.get(columnNum).getLast(), card)) {
            column.get(columnNum).add(card);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Checks if the new card is the next lowest numbered card after the
     * column card
     */
    private boolean isNextLowestCard(Card columnCard, Card newCard) {
        Card.Rank lower_rank = switch (columnCard.getRank()) {
            case Card.Rank.ACE -> null;
            case Card.Rank.TWO -> Card.Rank.ACE;
            case Card.Rank.THREE -> Card.Rank.TWO;
            case Card.Rank.FOUR -> Card.Rank.THREE;
            case Card.Rank.FIVE -> Card.Rank.FOUR;
            case Card.Rank.SIX -> Card.Rank.FIVE;
            case Card.Rank.SEVEN -> Card.Rank.SIX;
            case Card.Rank.EIGHT -> Card.Rank.SEVEN;
            case Card.Rank.NINE -> Card.Rank.EIGHT;
            case Card.Rank.TEN -> Card.Rank.NINE;
            case Card.Rank.JACK -> Card.Rank.TEN;
            case Card.Rank.QUEEN -> Card.Rank.JACK;
            case Card.Rank.KING -> Card.Rank.QUEEN;
        };

        if (newCard.getRank() != lower_rank) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Checks to see if a card is cab be appended to a column
     */
    private boolean isOppositeSuit(Card columnCard, Card newCard) {
        if (isRedSuit(columnCard)) {
            return !isRedSuit(newCard);
        } else {
            return isRedSuit(newCard);
        }
    }

    /**
     * helper function to check if red suit
     */
    private boolean isRedSuit(Card card) {
        if (card.getSuit() == Card.Suit.HEARTS
                || card.getSuit() == Card.Suit.DIAMONDS) {
            return true;
        } else
            return false;

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
