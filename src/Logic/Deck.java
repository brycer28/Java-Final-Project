package Logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
    private List<Card> cards;

    public Deck() {
        cards = new ArrayList<Card>();
        createDeck();
        shuffle();
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public void createDeck() {
        for (Card.Suit suit : Card.Suit.values()) {
            for(Card.Rank rank : Card.Rank.values()) {
                cards.add(new Card(suit, rank));
            }
        }
    }
}
