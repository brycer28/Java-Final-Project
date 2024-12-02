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
    private boolean win; // If true, the player wins. If not, the dealer wins.

    public Blackjack() {
        deck = new Deck();
        playerHand = new ArrayList<>();
        dealerHand = new ArrayList<>();
    }

    public void startBlackjackGame() {
        // Both the dealer and player start the game with 2 cards
        playerHand.add(deck.pop());
        playerHand.add(deck.pop());
        dealerHand.add(deck.pop());
        dealerHand.add(deck.pop());

    }

    private int calculateHandValue(List<Card> hand) {
        int value = 0;
        int aceCount = 0; // This will be used to help determine if the ace will count as 1 or 11

        for (Card card : hand) {
            switch (card.getRank()) {
                case TWO: value += 2; break;
                case THREE: value += 3; break;
                case FOUR: value += 4; break;
                case FIVE: value += 5; break;
                case SIX: value += 6; break;
                case SEVEN: value += 7; break;
                case EIGHT: value += 8; break;
                case NINE: value += 9; break;
                case TEN: case JACK: case QUEEN: case KING: value += 10; break; // Face cards count as 10
                case ACE:
                    value += 11;
                    aceCount++;
                    break;
            }
        }

        // This will change an aces value from 11 to 1 to prevent busts from drawing an ace
        while (value > 21 && aceCount > 0) {
            value -= 10;
            aceCount--;
        }
        return value;
    }

}
