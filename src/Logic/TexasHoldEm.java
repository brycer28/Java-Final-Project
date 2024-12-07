package Logic;

/*
Game 1 : Texas Hold'em
 */

public class TexasHoldEm {
    private Deck deck;
    private Hand playerHand = new Hand();
    private Hand dealerHand = new Hand();
    private Hand communityCards = new Hand();

    // runner class for an instance of the game
    public TexasHoldEm() {
        deck = new Deck();
        deck.printDeck();

        // deal cards to players (2 each)
        dealCard(playerHand);
        dealCard(dealerHand);
        dealCard(playerHand);
        dealCard(dealerHand);

        // first betting round (pre-flop)
        // int getPlayerDecision() - 0:check, 1:call, 2:raise, 3:fold
        // will need more logic in the case of raising or folding

        // deal first 3 community cards (flop)
        for (int i=0; i<3; i++) {
            dealCard(communityCards);
        }

        // second betting round (post-flop)


        // deal the 4th community card (turn)
        dealCard(communityCards);

        // third betting round (after turn)


        // deal the final community card (river)
        dealCard(communityCards);

        // final betting round (showdown)


        // evaluate player hands - will need to copy community cards to each hand
        // to make the best 5 card hand out of the 7 available
        int playerHandValue = playerHand.evaluateHand();
        int dealerHandValue = dealerHand.evaluateHand();

        // print hands for testing
        System.out.println(playerHand.toString() + "\n" + dealerHand.toString() + "\n" + communityCards.toString());
    }

    public void dealCard(Hand hand) {
        // add the top card in the deck to a players hand
        hand.add(deck.peek());

        // remove top card from stack so it is not used again
        deck.pop();
    }

    // takes in a hand and returns an integer the 'weight' of the hand
    // weight will be according to an enum of hands (hi-card, pair, set, full house, etc.)
    public int evaluateHand(Hand hand) {

        return 0;
    }


}
