package Logic;

import java.util.ArrayList;
import java.util.Collections;

public class CrazyEights {

    private Deck deck;
    private ArrayList<Card> dealerHand;
    private ArrayList<Card> playerHand;
    private ArrayList<Card> discardPile;
    //This is needed for the crazy 8 aspect of the game
    private Card.Suit currentSuit;
    private boolean playerTurnFlag;
    private boolean gameOverFlag;
    private String gameMessage = "Welcome to Crazy Eights!!!";

    public CrazyEights() {
        deck = new Deck();
        dealerHand = new ArrayList<>();
        playerHand = new ArrayList<>();
        discardPile = new ArrayList<>();
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
        discardPile.add(deck.pop());
        currentSuit = discardPile.getFirst().getSuit();

        //Initiate Player Turn first
        playerTurnFlag = true;
        gameMessage = "Your Turn!! Play a card to draw.";
    }

    //Logic for when a card is clicked to play
    public boolean playCard(Card card, Card.Suit chosenSuit) {
        if(isValidMove(card)) {
            playerHand.remove(card);
            discardPile.add(card);

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

            return true;
        } else {
            gameMessage = "Invalid Move! Try Again.";
            return false;
        }
    }

    //Method for Drawing a Card that's not from og deck, aka C8 specific draw
    public void drawCard(){
        if (playerTurnFlag) {
            //Case where og deck is exhausted of cards
            if(deck.isEmpty()) {
                reshuffleDiscardPile();
            }
            if (!deck.isEmpty()) {
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
        boolean cardPlayed = false;
        //This computer finds first hit, simple
        for (Card card : dealerHand) {
            if (isValidMove(card)) {
                dealerHand.remove(card);
                discardPile.add(card);

                if (card.getRank() == Card.Rank.EIGHT) {
                    // For now, just set to a random suit
                    currentSuit = Card.Suit.values()[(int) (Math.random() * 4)];
                    gameMessage = "Computer played an Eight and changed suit to " + currentSuit + ".";
                } else {
                    currentSuit = card.getSuit();
                    gameMessage = "Computer played " + card + ".";
                }
                cardPlayed = true;
                break;
            }
        }
        //forced check for card played, like draw for player
        if (!cardPlayed) {
            if (deck.isEmpty()) {
                reshuffleDiscardPile();
            }
            if (!deck.isEmpty()) {
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
        }
    }

    //Helper Methods

    //Checks to see if a card is valid to be placed
    //Aka same suit or number, or an 8
    private boolean isValidMove(Card card) {
        Card topCard = discardPile.getLast();
        return card.getRank() == Card.Rank.EIGHT ||
                card.getSuit() == currentSuit ||
                card.getRank() == topCard.getRank();
    }

    //Reshuffle for when deck is exhausted
    private void reshuffleDiscardPile() {
        if (!discardPile.isEmpty()) {
            //Rules state leaving the first card
            Card topCard = discardPile.removeLast();

            //Shuffle ArrayList of Cards
            Collections.shuffle(discardPile);

            //Recreate deck to pull from and clear discard
            deck.addAll(discardPile);
            discardPile.clear();
            discardPile.add(topCard);
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
        return discardPile.getLast();
    }

    public String getGameMessage() {
        return gameMessage;
    }

    public boolean isPlayerTurn() {
        return playerTurnFlag;
    }

    public boolean isGameOver() {
        return gameOverFlag;
    }

    // Reset method (You'll likely need this for your GUI)
    public void resetGame() {
        deck = new Deck();
        dealerHand.clear();
        playerHand.clear();
        discardPile.clear();
        currentSuit = null;
        gameMessage = "Welcome to Crazy Eights!!!";
        playerTurnFlag = true;
        gameOverFlag = false;
        playGame();
    }

}
