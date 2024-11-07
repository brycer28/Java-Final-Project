package Logic;

public class Card {
    private final Suit suit;
    private final Rank rank;
    private boolean faceUp = false;

    public enum Suit {
        CLUBS, DIAMONDS, HEARTS, SPADES;
    }

    //eventually ace will need to represent 1 and 11
    public enum Rank{
        TWO, THREE, FOUR, FIVE, SIX, SEVEN,
        EIGHT, NINE, TEN, JACK, QUEEN, KING, ACE;
    }

    public Card(Suit suit, Rank rank) {
        this.suit = suit;
        this.rank = rank;
    }

    @Override
    public String toString() {
        return this.getRank().toString() + " of " + this.getSuit().toString();
    }

    public Suit getSuit() { return suit; }

    public Rank getRank() { return rank; }

    public boolean isFaceUp() { return faceUp; }

    public void toggleFaceUp() { faceUp = !faceUp; }
}

