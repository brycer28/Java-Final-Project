package Main;

import Logic.*;

// this class is intended to test various logic portions before implementing graphics

import Logic.Hand;

public class Main {

    public static void main(String[] args) {
        // create cards
        Card c1 = new Card(Card.Suit.SPADES, Card.Rank.ACE);
        Card c2 = new Card(Card.Suit.DIAMONDS, Card.Rank.TWO);
        Card c3 = new Card(Card.Suit.DIAMONDS, Card.Rank.THREE);
        Card c4 = new Card(Card.Suit.DIAMONDS, Card.Rank.FOUR);
        Card c5 = new Card(Card.Suit.DIAMONDS, Card.Rank.FIVE);
        Card c6 = new Card(Card.Suit.DIAMONDS, Card.Rank.SIX);
        Card c7 = new Card(Card.Suit.DIAMONDS, Card.Rank.EIGHT);

        // create hand
        Hand hand = new Hand();
        hand.add(c1);
        hand.add(c2);
        hand.add(c3);
        hand.add(c4);
        hand.add(c5);
        hand.add(c6);
        hand.add(c7);

        //System.out.println(hand.toString());


        System.out.println("PAIR: " + Logic.Hand.isPair(hand));
        System.out.println("TWO PAIR: " + Logic.Hand.isTwoPair(hand));
        System.out.println("THREE OF A KIND: " + Logic.Hand.isThreeOfAKind(hand));
        System.out.println("STRAIGHT: " + Logic.Hand.isStraight(hand));
        System.out.println("FLUSH: " + Logic.Hand.isFlush(hand));
        System.out.println("FULL HOUSE: " + Logic.Hand.isFullHouse(hand));
        System.out.println("FOUR OF A KIND: " + Logic.Hand.isFourOfAKind(hand));
        System.out.println("STRAIGHT FLUSH: " + Logic.Hand.isStraightFlush(hand));
        System.out.println("ROYAL FLUSH: " + Logic.Hand.isRoyalFlush(hand));

        System.out.println("\nBEST HAND: " + Logic.Hand.evaluateHand(hand));


    }
}
