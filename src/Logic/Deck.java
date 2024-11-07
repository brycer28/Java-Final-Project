package Logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class Deck extends Stack<Card> {

    public Deck() {
        super();
        createDeck();
        shuffle();
    }

    public void shuffle() {
        Collections.shuffle(this);
    }

    public void createDeck() {
        for (Card.Suit suit : Card.Suit.values()) {
            for(Card.Rank rank : Card.Rank.values()) {
                add(new Card(suit, rank));
            }
        }
    }

    public void printDeck() {
        for (Card card : this) {
            System.out.println(card.toString());
        }
    }
}
