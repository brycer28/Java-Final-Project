package Logic;

import java.util.ArrayList;
import java.util.Collections;

public class CrazyEights {

    private Deck deck;
    private final ArrayList<Card> dealerHand;
    private final ArrayList<Card> playerHand;
    private final ArrayList<Card> playedPile;
    //This is needed for the crazy 8 aspect of the game
    private Card.Suit currentSuit;
    private boolean playerTurnFlag;
    private boolean gameOverFlag;
    private String gameMessage = "Welcome to Crazy Eights!!!";

    public CrazyEights() {
        deck = new Deck();
        dealerHand = new ArrayList<>();
        playerHand = new ArrayList<>();
        playedPile = new ArrayList<>();
        playGame();

    }

    public void playGame() {
        //Initial Deal (Rules Based on Wiki)
        for (int i = 0; i < 7; i++) {
            playerHand.add(deck.pop());
            dealerHand.add(deck.pop());
        }

        //This Version Reshuffles a Discard Pile
        // instead of points if pile is exhausted like some
        playedPile.add(deck.pop());
        currentSuit = playedPile.getFirst().getSuit();

        //Initiate Player Turn first
        playerTurnFlag = true;
        gameMessage = "Your Turn!! Play a card to draw.";
    }

    //Logic for when a card is clicked to play
    public void playCard(Card card, Card.Suit chosenSuit) {
        if(isValidMove(card)) {
            playerHand.remove(card);
            playedPile.add(card);

            //Special Card Handling
            if(card.getRank() == Card.Rank.EIGHT) {
                //This section allows user to pick suit
                currentSuit = chosenSuit;
                gameMessage = "You played an Eight!! Current Suit is Still " + currentSuit + ".";
            } else {
                currentSuit = card.getSuit();
                gameMessage = "You played " + card + "!";
            }

            //Check if Player Won
            if (playerHand.isEmpty()) {
                gameMessage = "You Won!";
                gameOverFlag = true;
            } else {
                playerTurnFlag = false;
                //Passes to Computer Dealer If Not
                dealerTurn();
            }

        } else {
            gameMessage = "Invalid Move! Try Again.";
        }
    }

    //Method for Drawing a Card from the Deck
    public void drawCard(){
        if (playerTurnFlag) {
            //Case where og deck is exhausted of cards
            if(deck.isEmpty()) {
                reshufflePlayedPile();
            } else {
                playerHand.add(deck.pop());
                gameMessage = "You Drew a Card. Dealer's Turn.";
                playerTurnFlag = false;
                dealerTurn();
            }
        }
    }

    //Computer/Dealers Turn Method
    //Similar to Methods above that work together for player turn
    private void dealerTurn() {
        //Decides if dealer draws a card or not
        //based on a card being able to be played
        boolean cardPlayed = false;
        //Dealer Plays a Card
        //This computer finds first hit, simple
        for (Card card : dealerHand) {
            if (isValidMove(card)) {
                dealerHand.remove(card);
                playedPile.add(card);

                //C8 Scenario Check (Dealer chooses by random)
                if (card.getRank() == Card.Rank.EIGHT) {
                    // For now, just set to a random suit
                    currentSuit = Card.Suit.values()[(int) (Math.random() * 4)];
                    gameMessage = "Computer played an Eight and changed suit to " + currentSuit + ".";
                } else {
                    currentSuit = card.getSuit();
                    gameMessage = "Computer played " + card + ".";
                }
                //Setting True skips the draw if below
                cardPlayed = true;
                break;
            }
        }
        //forced check for card played, like draw for player
        if (!cardPlayed) {
            if (deck.isEmpty()) {
                reshufflePlayedPile();
            } else {
                dealerHand.add(deck.pop());
                gameMessage = "Computer drew a card.";
            }
        }

        // Check for win
        if (dealerHand.isEmpty()) {
            gameMessage = "Computer wins!";
            gameOverFlag = true;
        } else {
            playerTurnFlag = true;
            //Since User clicks on cards, this flag enables that ability
            //And click calls the next player turn
        }
    }

    //Helper Methods

    //Checks to see if a card is valid to be placed
    //Aka same suit or number, or an 8
    private boolean isValidMove(Card card) {
        Card topCard = playedPile.getLast();
        //An eight is always valid
        return card.getRank() == Card.Rank.EIGHT ||
                //Check for suit
                card.getSuit() == currentSuit ||
                //Check for #
                card.getRank() == topCard.getRank();
    }

    //Reshuffle for when deck is exhausted
    private void reshufflePlayedPile() {
        if (!playedPile.isEmpty()) {
            //Rules state leaving the first card
            Card topCard = playedPile.removeLast();

            //Shuffle ArrayList of Cards
            Collections.shuffle(playedPile);

            //Recreate deck to pull from and clear discard
            deck.empty();
            deck.addAll(playedPile);
            playedPile.clear();
            playedPile.add(topCard);
            currentSuit = topCard.getSuit();
            gameMessage = "Deck Exhausted! Discard Pile shuffled and put into play.";
        } else {
            gameMessage = "Deck Empty! Game Over.";
            //Could Add Point System Here to Determine Winner
            gameOverFlag = true;
        }
    }

    // Getters for GUI
    public ArrayList<Card> getPlayerHand() {
        return playerHand;
    }

    public ArrayList<Card> getDealerHand() {
        return dealerHand;
    }

    public Card getTopDiscardCard() {
        return playedPile.getLast();
    }

    public String getGameMessage() {
        return gameMessage;
    }

    public boolean isPlayerTurn() {
        return playerTurnFlag;
    }

    public boolean isGameOver() {
        return !gameOverFlag;
    }

    // Reset method
    public void resetGame() {
        deck = new Deck();
        dealerHand.clear();
        playerHand.clear();
        playedPile.clear();
        currentSuit = null;
        gameMessage = "Welcome to Crazy Eights!!!";
        playerTurnFlag = true;
        gameOverFlag = false;
        playGame();
    }

}
