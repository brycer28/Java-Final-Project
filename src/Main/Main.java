package Main;

import Logic.*;

// this class is intended to test various logic portions before implementing graphics

import Logic.Hand;

public class Main {

    public static void main(String[] args) {
        // create cards
        Card c1 = new Card(Card.Suit.SPADES, Card.Rank.TWO);
        Card c2 = new Card(Card.Suit.DIAMONDS, Card.Rank.TEN);
        Card c3 = new Card(Card.Suit.DIAMONDS, Card.Rank.JACK);
        Card c4 = new Card(Card.Suit.DIAMONDS, Card.Rank.QUEEN);
        Card c5 = new Card(Card.Suit.DIAMONDS, Card.Rank.KING);
        Card c6 = new Card(Card.Suit.DIAMONDS, Card.Rank.ACE);
        Card c7 = new Card(Card.Suit.HEARTS, Card.Rank.EIGHT);

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

        System.out.println(Logic.Hand.isRoyalFlush(hand));



    }
}
