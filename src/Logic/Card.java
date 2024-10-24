package Logic;

public class Card {
    private final Suit suit;
    private final Rank rank;

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

    public Suit getSuit() { return suit; }

    public Rank getRank() { return rank; }
}

