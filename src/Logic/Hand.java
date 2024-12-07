package Logic;

import java.util.*;
import java.util.stream.Collectors;

import Logic.Card.*;

public class Hand extends ArrayList<Card> {

    public enum HandRanks {
        HI_CARD, PAIR, TWO_PAIR, THREE_OF_A_KIND,
        STRAIGHT, FLUSH, FULL_HOUSE, FOUR_OF_A_KIND,
        STRAIGHT_FLUSH, ROYAL_FLUSH
    }

    public Hand() {
        super();
    }

    public HandRanks evaluateHand(Hand hand) {
        // check that hand is not null or empty
        if (hand == null || hand.isEmpty()) {
            throw new IllegalArgumentException("Hand is null or empty");
        }

        // using helper methods (ex. isRoyalFlush), check hand and return respective
        // HandRank
        if (isRoyalFlush(hand)) {
            return HandRanks.ROYAL_FLUSH;
        } else if (isStraightFlush(hand)) {
            return HandRanks.STRAIGHT_FLUSH;
        } else if (isFourOfAKind(hand)) {
            return HandRanks.FOUR_OF_A_KIND;
        } else if (isFullHouse(hand)) {
            return HandRanks.FULL_HOUSE;
        } else if (isFlush(hand)) {
            return HandRanks.FLUSH;
        } else if (isStraight(hand)) {
            return HandRanks.STRAIGHT;
        } else if (isThreeOfAKind(hand)) {
            return HandRanks.THREE_OF_A_KIND;
        } else if (isTwoPair(hand)) {
            return HandRanks.TWO_PAIR;
        } else if (isPair(hand)) {
            return HandRanks.PAIR;
        } else {
            return HandRanks.HI_CARD;
        }
    }

    public static boolean isRoyalFlush(Hand hand) {
        // same concept as straight flush except added constraint of containing
        // 10,J,Q,K,A
        if (!isStraightFlush(hand)) {
            return false;
        }

        // get the suit that makes the flush
        Suit flushSuit = Suit.values()[getFlushSuit(hand)];

        // create a smaller set of cards that only belong to the flushSuit
        Hand flushSuitOnly = hand.stream()
                .filter(card -> card.getSuit().equals(flushSuit))
                .collect(Collectors.toCollection(Hand::new));

        // creat royal flush list
        List<Rank> royalStraight = Arrays.asList(Rank.TEN, Rank.JACK, Rank.QUEEN, Rank.KING, Rank.ACE);

        // check that flushSuitOnly contains 10,J,Q,K,A
        List<Rank> flushSuitOnlyRanks = flushSuitOnly.stream()
                .map(Card::getRank)
                .toList();

        return flushSuitOnlyRanks.containsAll(royalStraight);
    }

    public static boolean isStraightFlush(Hand hand) {
        /*
         * Ex. [3C, 6C, 7C, 8C, 9C, 10D, 4S]
         * 
         * This hand has a straight (6,7,8,9,10) AND a flush (5 Clubs)
         * but the set of cards that make the straight is not the same
         * as the flush set
         */

        // first, check if the hand is both a straight and a flush
        if (!isStraight(hand) || !isFlush(hand)) {
            return false;
        }

        // get the suit that makes the flush
        Suit flushSuit = Suit.values()[getFlushSuit(hand)];

        // create a smaller set of cards that only belong to the flushSuit
        Hand flushSuitOnly = hand.stream()
                .filter(card -> card.getSuit().equals(flushSuit))
                .collect(Collectors.toCollection(Hand::new));

        // check that the new subset is a straight
        return isStraight(flushSuitOnly);
    }

    // check if there are 4 cards of the same rank
    public static boolean isFourOfAKind(Hand hand) {
        // use a stream to map each rank to its frequency
        Map<Rank, Long> rankCount = hand.stream()
                .collect(Collectors.groupingBy(Card::getRank, Collectors.counting()));

        return rankCount.values().stream().anyMatch(count -> count >= 4);
    }

    // check if there is both a pair and a set in the same hand and that they are of
    // 2 distinct ranks
    public static boolean isFullHouse(Hand hand) {
        // use a stream to map each rank to its frequency
        Map<Rank, Long> rankCount = hand.stream()
                .collect(Collectors.groupingBy(Card::getRank, Collectors.counting()));

        // get the rank corresponding to the pair
        Optional<Rank> pairRank = rankCount.entrySet().stream()
                .filter(entry -> entry.getValue() == 2)
                .map(Map.Entry::getKey)
                .findFirst(); // maybe try findAny

        // get the rank corresponding to the set
        Optional<Rank> setRank = rankCount.entrySet().stream()
                .filter(entry -> entry.getValue() == 3)
                .map(Map.Entry::getKey)
                .findFirst();

        // ensure that there is a pair and set, and that the pair and set are not of the
        // same rank
        return (isPair(hand) && isThreeOfAKind(hand) && (pairRank != setRank));
    }

    // check if there are 5 cards of the same suit
    public static boolean isFlush(Hand hand) {
        // use a stream to map each suit to its frequency in the hand
        Map<Suit, Long> suitCount = hand.stream()
                .collect(Collectors.groupingBy(Card::getSuit, Collectors.counting()));

        // if any suitCount >= 5, return true
        return suitCount.values().stream().anyMatch(count -> count >= 5);
    }

    /*
     * The helper method can only check 5 cards at a time, meaning we will have to
     * check subsets of the sorted hand.
     * If the first 5 are not a straight, remove the first card and try again.
     * Repeat to check last subset.
     * Ex. First, check hand[0-4], then check hand[1-5], and finally check hand
     * [2-6]
     */
    public static boolean isStraight(Hand hand) {
        // sort the values in the hand by their ordinal value
        hand.sort(Comparator.comparing(Card::getRank));

        // check if first subset is consecutive
        boolean isStraight = hasFiveConsecutive(hand);

        // if the first is not a straight, remove first element and check again
        if (!isStraight) {
            hand.removeFirst();
            isStraight = hasFiveConsecutive(hand);
        }

        // if second is not a straight, remove first element and check a final time
        if (!isStraight) {
            hand.removeFirst();
            isStraight = hasFiveConsecutive(hand);
        }

        return isStraight;
    }

    // helper method for isStraight, checks if first 5 elements of a subarray are
    // consecutive
    public static boolean hasFiveConsecutive(Hand hand) {
        for (int i = 0; i < 4; i++) {
            if (hand.get(i + 1).getRank().ordinal() != hand.get(i).getRank().ordinal() + 1) {
                return false;
            }
        }
        return true;
    }

    // check if there are 3 cards of the same rank
    public static boolean isThreeOfAKind(Hand hand) {
        // use a stream to map each rank to its frequency
        Map<Rank, Long> rankCount = hand.stream()
                .collect(Collectors.groupingBy(Card::getRank, Collectors.counting()));

        // if any rankCount >= 3, return true
        return rankCount.values().stream().anyMatch(count -> count >= 3);
    }

    // check if there are 2 pairs, each of a distinct rank
    public static boolean isTwoPair(Hand hand) {
        // use a stream to map each rank to its frequency
        Map<Rank, Long> rankCount = hand.stream()
                .collect(Collectors.groupingBy(Card::getRank, Collectors.counting()));

        // count the number of pairs in the hand
        int pairCount = (int) rankCount.values().stream()
                .filter(count -> count >= 2)
                .count();

        return pairCount == 2;
    }

    // check if there are 2 cards of the same rank
    public static boolean isPair(Hand hand) {
        // use a stream to map each rank to its frequency
        Map<Rank, Long> rankCount = hand.stream()
                .collect(Collectors.groupingBy(Card::getRank, Collectors.counting()));

        // if rankCount >= 2, return true
        return rankCount.values().stream().anyMatch(count -> count >= 2);
    }

    public static int getFlushSuit(Hand hand) {
        if (!isFlush(hand)) {
            return -1;
        }

        // use a stream to map each suit to its frequency in the hand
        Map<Suit, Long> suitCount = hand.stream()
                .collect(Collectors.groupingBy(Card::getSuit, Collectors.counting()));

        // return the ordinal value of the most frequent suit
        return Collections.max(suitCount.entrySet(), Map.Entry.comparingByValue()).getKey().ordinal();
    }
}
