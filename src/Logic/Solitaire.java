package Logic;

import java.util.ArrayList;
import java.util.Iterator;

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
     * Returns the Cards in the column
     */
    public ArrayList<Card> getColumn(int columnNum) {
        return column.get(columnNum);
    }

    /**
     * Moves a card from one column to another. If there are cards connected
     * then it moves those cards as well. Returns true if the cards were
     * moved
     */
    public boolean moveCardToColumn(int origColumn, int newColumn, Card card) {
        // checks if the card is in the original column
        // & tries to add the card to the new column
        if (!column.get(origColumn).contains(card) || !addToColumn(newColumn, card)) {
            return false;
        }

        // adds the remaining cards to the new stack
        int loopSize = column.get(origColumn).size();
        boolean pastCard = false;
        Iterator<Card> it = column.get(origColumn).iterator();
        while (it.hasNext()) {
            Card c = it.next();
            // if the current card is the card that is moving, remove the card
            if (c == card) {
                pastCard = true;
                it.remove();
                continue;
            }

            if (pastCard) {
                column.get(newColumn).add(c);
                it.remove();
            }
        }
        return true;
    }

    /**
     * Checks to see if a card can be added to the foundation. If it can, the
     * card is added and true is returned. If it cannot, the card is not added
     * and false is returned
     */
    public boolean addToFoundation(int foundationNum, int columnNum, Card card) {
        // check if the card is the top of the columns stack
        if (columnNum != -1 && column.get(columnNum).getLast() != card) {
            return false;
        }
        // if the foundation is empty, check if card is A and add if true
        if (foundation.get(foundationNum).size() == 0) {
            if (card.getRank() == Card.Rank.ACE) {
                foundation.get(foundationNum).add(card);
                // checks if a card needs to be removed from a col
                if (columnNum != -1)
                    column.get(columnNum).removeLast();
                return true;
            } else {
                return false;
            }
        }
        // checks if the card is the same suit
        if (isOppositeSuit(card, foundation.get(foundationNum).getLast())) {
            return false;
        }

        // checks if the card is the next largest number
        if (isNextLowestCard(card, foundation.get(foundationNum).getLast())) {
            foundation.get(foundationNum).add(card);
            // checks if a card needs to be removed from a col
            if (columnNum != -1)
                column.get(columnNum).removeLast();
            return true;
        } else {
            return false;
        }

    }

    /**
     * Checks to see if a card stack can be added to a column. If it can, the
     * stack is added and true is returned. If it cannot, the stack is not added and
     * false if returned.
     * The first card in the ArrayList must higher order card
     */
    public boolean addToColumn(int columnNum, ArrayList<Card> cardStack) {
        Card topCard = cardStack.getFirst();
        // first checks if the column is empty and if so adds the card
        if (column.get(columnNum).size() != 0) {
            // checks of the card is the opposite suit and return false if not
            if (!isOppositeSuit(column.get(columnNum).getLast(), topCard)) {
                return false;
            }

            // checks if the card is the next lowest card and returns true if yes
            if (!isNextLowestCard(column.get(columnNum).getLast(), topCard)) {
                return false;
            }
        }

        // adds the stack of cards
        for (Card card : cardStack) {
            column.get(columnNum).add(card);
        }
        return true;
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
        // checks of the card is the opposite suit and return false if not
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
     * Removes the card that was most recently flipped
     */
    public Card removeDisplayedCard() {
        int cardIndex = deckIndex - 1;
        if (deck.size() == 0) {
            System.out.println("Error getting card, deck is empty");
            return null;
        }
        if (cardIndex == -1) {
            cardIndex = deck.size() - 1;
        }
        Card card = deck.get(cardIndex);
        deck.remove(cardIndex);
        if (deckIndex != 0) {
            deckIndex -= 1;
        }
        if (deckIndex >= deck.size()) {
            deckIndex = 0;
        }
        return card;

    }

    /**
     * prints the entire deck to the console
     */
    public void printDeck() {
        int i = 0;
        for (Card card : deck) {
            if (i % 7 == 0 && i != 0)
                System.out.println(i + ": " + card);
            else
                System.out.print("\t" + i + ": " + card);
            i++;
        }
        System.out.println();
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
            return null;
        }
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
     * Initializes the foundation ArrayList
     */
    private void initFoundation() {
        foundation = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            foundation.add(new ArrayList<>());
        }
    }

    /**
     * Initializes the column ArrayList
     */
    private void initColumn() {
        column = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            column.add(new ArrayList<>());
            for (int j = i; j >= 0; j--) {
                column.getLast().add(removeTopCard());
            }
        }

    }

    public void printColumns() {
        for (ArrayList<Card> list : column) {
            for (Card card : list) {
                System.out.print(card + ", ");
            }
            System.out.println();
        }
    }

    /**
     * Gets the number of cards left in the deck
     */
    public int getDeckSize() {
        return deck.size();
    }

}
