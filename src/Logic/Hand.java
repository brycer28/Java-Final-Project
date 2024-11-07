package Logic;

import java.util.ArrayList;

public class Hand extends ArrayList<Card>{
    private HandRanks rank;

    public enum HandRanks {
        HI_CARD, PAIR, TWO_PAIR, THREE_OF_A_KIND,
        STRAIGHT, FLUSH, FULL_HOUSE, FOUR_OF_A_KIND,
        STRAIGHT_FLUSH, ROYAL_FLUSH;
    }

    public Hand() {
        super();
    }

    // ex Hand - 5S, 5C, 8D, 8C, 8S

    public int evaluateHand() {

        // using helper methods (ex. isRoyalFlush), check hand and return ordinal value of HandRank enum



        return 0;
    }

    public void isRoyalFlush() {

    }

    public void isStraightFlush() {

    }

    public void isFourOfAKind() {

    }

    public void isFullHouse() {

    }

    public void isFlush() {

    }

    public void isStraight() {

    }

    public void isThreeOfAKind() {

    }

    public void isTwoPair() {

    }

    public void isPair() {

    }

    public void isHighCard() {

    }

    public void setRank(HandRanks handRank) {
        this.rank = handRank;
    }








}
