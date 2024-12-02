/*
Game 3 - Blackjack
Created by Mason Simpson
 */

package Logic;

import java.util.ArrayList;
import java.util.List;

public class Blackjack {

    private Deck deck;
    private List<Card> playerHand;
    private List<Card> dealerHand;

    public Blackjack() {
        deck = new Deck();
        playerHand = new ArrayList<>();
        dealerHand = new ArrayList<>();
    }

    public void startBlackjackGame() {

    }
}
