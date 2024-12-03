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
    private String gameMessage = "Welcome to Blackjack!"; // Display string for BlackjackPanel; initialized with simple welcome message
    private int playerValue;
    private int dealerValue;
    private boolean gameOver = false;

    public Blackjack() {
        deck = new Deck();
        playerHand = new ArrayList<>();
        dealerHand = new ArrayList<>();
        startGame();
    }

    public void startGame() {
        // Both the dealer and player start the game with 2 cards
        playerHand.add(deck.pop());
        playerHand.add(deck.pop());
        dealerHand.add(deck.pop());
        dealerHand.add(deck.pop());
        gameMessage = "Hit or Stand?";
        System.out.println(playerHand);
        System.out.println(dealerHand);
    }

    // Logic for the hit button
    public void playerHit() {
        playerHand.add(deck.pop());
        System.out.println(playerHand);
        playerValue = calculateHandValue(playerHand);
        if (playerValue > 21) {
            gameMessage = "Bust - dealer wins.";
            gameOver = true;
        } else {
            gameMessage = "Hit or Stand?";
        }
    }

    // Logic for the stand button
    public void playerStand() {
        // Dealer has to keep hitting until they either beat the player or bust
        while (calculateHandValue(dealerHand) <= calculateHandValue(playerHand)) {
            dealerHand.add(deck.pop());
        }
        determineWinner();
    }

    // Determines the winner of the game after the player chooses to stand
    public void determineWinner() {
        playerValue = calculateHandValue(playerHand);
        dealerValue = calculateHandValue(dealerHand);
        System.out.println(playerHand);
        System.out.println(dealerHand);

        if (dealerValue > 21 || playerValue > dealerValue) {
            gameMessage = "Player wins!";
        } else {
            gameMessage = "Dealer wins!";
        }
        gameOver = true;
    }

    // Getter for the player's hand
    public List<Card> getPlayerHand() {
        return playerHand;
    }

    // Getter for the dealer's hand
    public List<Card> getDealerHand() {
        return dealerHand;
    }

    // Getter for the game message
    public String getGameMessage() {
        return gameMessage;
    }

    // Getter for game over boolean
    public boolean isGameOver() {
        return gameOver;
    }

    // Getter for player hand value
    public int getPlayerValue() {
        return playerValue;
    }

    // Getter for dealer hand value
    public int getDealerValue() {
        return dealerValue;
    }
    // Calculates the value of a given hand
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
