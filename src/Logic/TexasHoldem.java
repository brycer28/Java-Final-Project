package Logic;

/*
Game 1 : Texas Hold'em
Author: brycer28
 */

import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;

import static Logic.Hand.evaluateHand;

public class TexasHoldem {
    private Deck deck;
    private Hand playerHand = new Hand();
    private Hand dealerHand = new Hand();
    private Hand communityCards = new Hand();
    private int playerChips = 1000;
    private int dealerChips = 1000;
    private int pot = 0;
    private int currentBet = 0;
    private int playerDecision = -1;
    private boolean validDecision = false;
    private boolean decisionMade = false;
    private int raiseAmount = 0;
    private CountDownLatch latch;


    public TexasHoldem() {
        latch = new CountDownLatch(1); // tells game to listen for one response
        deck = new Deck();

        // deal two cards to each player
        dealCard(playerHand);
        dealCard(dealerHand);
        dealCard(playerHand);
        dealCard(dealerHand);

        // first betting round (pre-flop)
        //logic.executeBettingRound();

        // deal three community cards (flop)
        for (int i=0; i<3; i++) {
            dealCard(communityCards);
            try { Thread.sleep(500); } catch (InterruptedException e) {}
        }

        // second betting round (post-flop)
        //wait for GUI action
        //logic.executeBettingRound();

        // deal fourth community card (turn)
        dealCard(communityCards);

        // third betting round (turn)
        //wait for GUI action
        //logic.executeBettingRound();

        // deal final community card (river)
        dealCard(communityCards);

        // final betting round (showdown)
        //wait for GUI action
        //logic.executeBettingRound();

        // evaluate each players hand
        Hand.HandRanks playerHandValue = Hand.evaluateHand(playerHand);
        Hand.HandRanks dealerHandValue = Hand.evaluateHand(dealerHand);

        determineWinner(playerHandValue, dealerHandValue);

        System.out.println(playerHand.toString());
        System.out.println(dealerHand.toString());
    }

    public void dealCard(Hand hand) {
        // add the top card in the deck to a players hand
        hand.add(deck.peek());

        // remove top card from stack so it is not used again
        deck.pop();
    }

    public void executeBettingRound() {
        currentBet = 0;

        waitForResponse(); // waits for user to select one of the GUI options

        while (!validDecision) {
            playerDecision = getPlayerDecision();

            switch (playerDecision) {
                case 0: // check
                    handleCheck();
                    break;
                case 1:
                    handleCall();
                    break;
                case 2:
                    handleRaise();
                    break;
                case 3:
                    handleFold();
                    break;

            }
        }
    }

    public void waitForResponse() {
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void handleCheck() {
        if (currentBet == 0) { validDecision = true; }
    }

    public void handleCall() {
        if (playerChips >= currentBet) {
            playerChips -= currentBet;
            pot += currentBet;
            validDecision = true;
        }
    }

    public void handleRaise() {

    }

    public void handleFold() {
        playerChips -= currentBet;
        dealerChips += pot;
        //more
    }


    public void determineWinner(Hand.HandRanks player, Hand.HandRanks dealer) {
        if (player.ordinal() > dealer.ordinal()) {
            playerChips += pot;
            dealerChips -= currentBet;
            System.out.println("You win this round!");
        } else if (player.ordinal() < dealer.ordinal()) {
            dealerChips += pot;
            playerChips -= currentBet;
            System.out.println("Dealer wins this round!");
        }
    }

    public void setPlayerDecision(int decision) {
        playerDecision = decision;;
        latch.countDown();
    }

    public int getPlayerDecision() {
        while (!decisionMade) {
            try { wait(); }
            catch (InterruptedException e) {
                 e.printStackTrace();
            }
        }
        decisionMade = false;
        return playerDecision;
    }

    public int getPlayerDecisionAfterRaise() {
        System.out.println("Enter Decision [0:call, 1:fold]");
        Scanner in = new Scanner(System.in);
        return in.nextInt();
    }

    public int getPot() {return pot;}
    public int getCurrentBet() {return currentBet;}
    public int getDealerChips() {return dealerChips;}
    public int getPlayerChips() {return playerChips;}
    public void setRaiseAmount(int raiseInt) {raiseAmount = raiseInt;}
    public int getRaiseAmount() {return raiseAmount;}








    // runner class for an instance of the game in the console
    public void TexasHoldEmConsole() {
        deck = new Deck();

        // deal cards to players (2 each)
        dealCard(playerHand);
        dealCard(dealerHand);
        dealCard(playerHand);
        dealCard(dealerHand);

        // first betting round (pre-flop)
        System.out.println("FIRST BETTING ROUND");
        executeBettingRound();

        // deal first 3 community cards (flop)
        for (int i=0; i<3; i++) {
            dealCard(communityCards);
        }

        // second betting round (post-flop)
        System.out.println("SECOND BETTING ROUND");
        executeBettingRound();

        // deal the 4th community card (turn)
        dealCard(communityCards);

        // third betting round (after turn)
        System.out.println("THIRD BETTING ROUND");
        executeBettingRound();

        // deal the final community card (river)
        dealCard(communityCards);

        // final betting round (showdown)
        System.out.println("FINAL BETTING ROUND");
        executeBettingRound();

        // add community cards to each hand
        for (Card card : communityCards) {
            playerHand.add(card);
            dealerHand.add(card);
        }

        System.out.println(playerHand.toString());
        System.out.println(dealerHand.toString());

        // evaluate player hands - will need to copy community cards to each hand
        // to make the best 5 card hand out of the 7 available
        Hand.HandRanks playerHandValue = evaluateHand(playerHand);
        Hand.HandRanks dealerHandValue = evaluateHand(dealerHand);

        if (playerHandValue.ordinal() > dealerHandValue.ordinal()) {
            System.out.println("YOU WIN!");
        } else {
            System.out.println("YOU LOSE!");
        }

        System.out.println("You had a " + playerHandValue.toString() + ". Dealer had a " + dealerHandValue.toString() + ".");
    }

    public void oldExecuteBettingRound() {
        // first show player their hand as well as current community cards
        System.out.println("\nYOUR HAND: " + playerHand.toString());
        System.out.println("COMMUNITY: " + communityCards.toString() + "\n");

        boolean validDecision = false;
        currentBet = 0; // reset bet each round

        // loop for validation of option
        while (!validDecision) {
            int playerDecision = getPlayerDecision(); // waits for GUI response

            switch (playerDecision) {
                case 0: // check
                    // if no bet, player can check. Else must re-select option
                    if (currentBet == 0) {
                        System.out.println("You checked.");
                        validDecision = true;
                    } else {
                        System.out.println("Unable to check. Select call, raise, or fold");
                    } break;

                case 1: // call
                    // if there is a current bet, ensure player has enough chips, then move chips from player to pot
                    if (currentBet > 0) {
                        if (playerChips >= currentBet) {
                            playerChips -= currentBet;
                            pot += currentBet;
                            System.out.println("You called the current bet of " + currentBet + ".");
                            validDecision = true;
                        } else {
                            System.out.println("You do not have enough chips to call.");
                        }
                    } else {
                        System.out.println("No bet to call. You checked.");
                        validDecision = true;
                    } break;

                case 2: // raise
                    // get the amount to raise, validate that player has enough chips, then raise the bet accordingly
                    int raiseAmount = getRaiseAmount();

                    if (playerChips >= currentBet + raiseAmount) {
                        currentBet += raiseAmount;
                        playerChips -= raiseAmount;
                        pot += raiseAmount;
                        System.out.println("You raised by " + raiseAmount + ". Current bet is now " + currentBet + ".");
                        validDecision = true;
                    } else {
                        System.out.println("You don't have enough chips to raise. Try again with a lower raise.");
                    } break;

                case 3: // fold
                    System.out.println("You folded. Dealer wins this round!");
                    validDecision = true;
                    break;

                default:
                    System.out.println("Invalid input. Please select a valid decision");
            }
        }

        // perform the same logic for the dealers option
        boolean validDealerDecision = false;
        while (!validDealerDecision) {
            // get dealer decison (check if no bet
            int dealerDecision = 2;

            switch (dealerDecision) {
                case 0: // check
                    System.out.println("Dealer checked.");
                    validDealerDecision = true;
                    break;

                case 1: // call
                    if (currentBet > 0) {
                        if (dealerChips >= currentBet) {
                            dealerChips -= currentBet;
                            pot += currentBet;
                            System.out.println("Dealer called the bet of " + currentBet + ".");
                            validDealerDecision = true;
                        } else {
                            System.out.println("Dealer does not have enough chips to call. Re-selecting option");
                        }
                    } break;

                case 2: // raise
                    // generate a random value 5-100 in increments of 5 for dealer bet
                    Random r = new Random();
                    int dealerRaiseAmount = 0;
                    while (dealerRaiseAmount <= 0 || dealerRaiseAmount > dealerChips - currentBet) {
                        dealerRaiseAmount = 5 + r.nextInt(19) * 5;
                    }

                    if (dealerChips >= currentBet + dealerRaiseAmount) {
                        currentBet += dealerRaiseAmount;
                        dealerChips -= currentBet;
                        pot += currentBet;
                        System.out.println("Dealer raised by " + dealerRaiseAmount + ". Current bet is now " + currentBet + ".");
                        validDealerDecision = true;

                        validDecision = false;
                        while (!validDecision) {
                            int playerDecisionAfterRaise = getPlayerDecisionAfterRaise();

                            switch (playerDecisionAfterRaise) {
                                case 0:
                                    playerChips -= dealerRaiseAmount;
                                    pot += dealerRaiseAmount;
                                    System.out.println("You called the raise of " + dealerRaiseAmount + ".");
                                    validDecision = true;
                                    break;
                                case 1:
                                    System.out.println("You folded. Dealer wins this round!");
                                    validDecision = true;
                                    break;

                            }
                        }

                    } else {
                        System.out.println("You don't have enough chips to raise. Try again with a lower raise.");
                    } break;


                case 3: // fold
                    System.out.println("Dealer folded. You win this round!");
                    playerChips -= pot;
                    pot = 0;
                    validDealerDecision = true;
                    break;
            }
        }
    }
}
