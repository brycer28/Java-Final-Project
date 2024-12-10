package Logic;

public class Card {
    private final Suit suit;
    private final Rank rank;
    private final String imgPath;
    private boolean faceUp = false;

    public enum Suit {
        CLUBS, DIAMONDS, HEARTS, SPADES;
    }

    // eventually ace will need to represent 1 and 11
    public enum Rank {
        TWO, THREE, FOUR, FIVE, SIX, SEVEN,
        EIGHT, NINE, TEN, JACK, QUEEN, KING, ACE;
    }

    public Card(Suit suit, Rank rank) {
        this.suit = suit;
        this.rank = rank;
        this.imgPath = getImgPath();
    }

    public String getImgPath() {
        char s = suit.name().toLowerCase().charAt(0);

        String r = null;
        if (rank == Rank.TWO) {
            r = "2";
        } else if (rank == Rank.THREE) {
            r = "3";
        } else if (rank == Rank.FOUR) {
            r = "4";
        } else if (rank == Rank.FIVE) {
            r = "5";
        } else if (rank == Rank.SIX) {
            r = "6";
        } else if (rank == Rank.SEVEN) {
            r = "7";
        } else if (rank == Rank.EIGHT) {
            r = "8";
        } else if (rank == Rank.NINE) {
            r = "9";
        } else if (rank == Rank.TEN) {
            r = "10";
        } else if (rank == Rank.JACK) {
            r = "j";
        } else if (rank == Rank.QUEEN) {
            r = "q";
        } else if (rank == Rank.KING) {
            r = "k";
        } else if (rank == Rank.ACE) {
            r = "a";
        }

        try {
            return (r + s);
        } catch (NullPointerException e) {
            return null;
        }
    }

    @Override
    public String toString() {
        return this.getRank().toString() + " of " + this.getSuit().toString();
    }

    public Suit getSuit() {
        return suit;
    }

    public Rank getRank() {
        return rank;
    }

    public boolean isFaceUp() {
        return faceUp;
    }

    public void toggleFaceUp() {
        faceUp = !faceUp;
    }

    public void setFaceUp() {faceUp = true;}
}
