package Logic;

/**
Game 1 : Texas Hold'em
Author: brycer28
**/

import GUI.TexasHoldemPanel;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

import static Logic.Hand.evaluateHand;

public class TexasHoldem {
    private TexasHoldemPanel GUI;
    private Deck deck;
    private Hand playerHand = new Hand();
    private Hand dealerHand = new Hand();
    private Hand communityCards = new Hand();
    private int playerChips = 1000;
    private int dealerChips = 1000;
    private int pot = 0;
    private int currentBet = 0;
    public int playerDecision = -1;
    public int dealerDecision = -1;
    private boolean validDecision = false;
    private boolean validRaise = false;
    private int raiseAmount = 0;
    private CountDownLatch latch;
    public boolean gameRunning = true;

    // start up a game of Texas Hold'em
    public TexasHoldem() {
        GUI = new TexasHoldemPanel(this);
        startGame();
    }

    // start the game in a loop, prompting to replay after each round
    public void startGame() {
        gameRunning = true;
        reset();

        new Thread(() -> {
            while (gameRunning) {
                playRound();

                boolean playAgain = true;
                playAgain = GUI.displayReplayPrompt();
                if (!playAgain) {
                    // prompt for next round?
                    gameRunning = false;
                    break;
                }
                gameRunning = true;
                reset();
            }
            GUI.endGame();
        }).start();
    }

    // logic to play one round of Texas Hold'em
    public void playRound() {
        // deal two cards to each player
        dealCard(playerHand);
        dealCard(dealerHand);
        dealCard(playerHand);
        dealCard(dealerHand);
        GUI.updateHands();

        // perform first betting round (pre-flop)
        executeBettingRound();
        GUI.updateStats();
        if (!gameRunning) return;

        // deal three community cards (flop)
        for (int i = 0; i < 3; i++) {
            dealCard(communityCards);
        }
        GUI.updateCommunityCards();

        // perform second betting round (post-flop)
        executeBettingRound();
        GUI.updateStats();
        if (!gameRunning) return;

        // deal fourth community card (turn)
        dealCard(communityCards);
        GUI.updateCommunityCards();

        // perform third betting round (turn)
        executeBettingRound();
        GUI.updateStats();
        if (!gameRunning) return;

        // deal final community card (river)
        dealCard(communityCards);
        GUI.updateCommunityCards();

        // perform final betting round (showdown)
        executeBettingRound();
        GUI.updateStats();
        if (!gameRunning) return;

        // evaluate each players hand and determine a winner
        determineWinner();
    }

    // deal a card to the specified player
    public void dealCard(Hand hand) {
        // add the top card in the deck to a players hand
        hand.add(deck.peek());

        // remove top card from stack so it is not used again
        deck.pop();
    }

    // execute a betting round by getting each players decision from the GUI and handling accordingly
    public void executeBettingRound() {
        currentBet = 0;
        // tells the program to wait and listen for 1 response from GUI, which sets the playerDecision
        this.latch = new CountDownLatch(1);
        playerDecision = -1;
        waitForResponse();

        validDecision = false;
        while (!validDecision) {
            playerDecision = getPlayerDecision();

            switch (playerDecision) {
                case 0: // check
                    handleCheck();
                    break;
                case 1:
                    handleCall(true);
                    break;
                case 2:
                    handleRaise(true);
                    break;
                case 3:
                    handleFold();
                    break;
            }
        }

        // if either player folds or runs out of chips, the game/round is over
        if (playerDecision == 3 || dealerDecision == 3 || playerChips <= 0 || dealerChips <= 0) {
            gameRunning = false;
            GUI.displayGameOver();
            return;
        }

        validDecision = false;
        while (!validDecision) {
            dealerDecision = getDealerDecision();

            switch (dealerDecision) {
                case 0: // check
                    handleCheck();
                    break;
                case 1:
                    handleCall(false);
                    break;
                case 2:
                    handleRaise(false);
                    break;
                case 3:
                    handleFold();
                    break;
            }
        }

        if (playerDecision == 3 || dealerDecision == 3 || playerChips <= 0 || dealerChips <= 0) {
            gameRunning = false;
            GUI.displayGameOver();
        }
    }

    public void handleCheck() {
        // as long as the current bet is 0, player may check
        if (currentBet == 0) validDecision = true;
        else validDecision = false;
    }

    public void handleCall(boolean isPlayer) {
        // if the first player has placed a bet and other has enough chips, the other player may call/match that bet
        if (currentBet > 0) {
            if (isPlayer) {
                if (playerChips > currentBet) {
                    playerChips -= currentBet;
                    pot += currentBet;
                    validDecision = true;
                }
            } else {
                if (dealerChips > currentBet) {
                    dealerChips -= currentBet;
                    pot += currentBet;
                    validDecision = true;
                }
            }
        }
        else validDecision = false;
    }

    public void handleRaise(boolean isPlayer) {
        // if the player can match the current bet, player may raise the bet if they have enough chips
        int chips = isPlayer ? playerChips : dealerChips;

        if (chips >= currentBet) {
            // get the raise amount entered in JTextBox
            while (!validRaise) {
                raiseAmount = getRaiseAmount();
                if (raiseAmount > 0 && playerChips >= currentBet + raiseAmount) {
                    validRaise = true;
                }
            }
            currentBet += raiseAmount;
            pot += raiseAmount;

            if (isPlayer) playerChips -= raiseAmount;
            else dealerChips -= raiseAmount;

            validDecision = true;
        }
        else validDecision = false;
    }

    public void handleFold() {
        // player always has the option to fold their hand and forfeit the pot
        validDecision = true;

        if (playerDecision == 3) {
            dealerChips += pot;
            currentBet = 0;
        }
        else if (dealerDecision == 3) {
            playerChips += pot;
            currentBet = 0;
        }
        pot = 0;
    }

    public int getDealerDecision() {
        Random rand = new Random();
        int prob = rand.nextInt(20) + 1;
        if (prob == 1) return 3; // dealer has a 1/20 chance of folding regardless

        // if playerDecison raises, dealer has another 1/10 chance to fold
        if (playerDecision == 2) {
            prob = rand.nextInt(10) + 1;
            if (prob == 1) return 3; // 1/10 chance to fold on raise
            else return 1; // 9/10 chance to call raise
        }

        // if playerDecision == 0, dealer must check as well
        return 0;
    }

    // determine the winner after each round
    public void determineWinner() {
        // add community cards to each players hand to evaluate full set
        for (Card card : communityCards) {
            playerHand.add(card);
            dealerHand.add(card);
        }

        Hand.HandRanks playerHandValue = Hand.evaluateHand(playerHand);
        Hand.HandRanks dealerHandValue = Hand.evaluateHand(dealerHand);

        if (playerHandValue.ordinal() > dealerHandValue.ordinal()) {
            playerChips += pot;
            dealerChips -= currentBet;
            System.out.println("You win this round!");
        } else if (playerHandValue.ordinal() < dealerHandValue.ordinal()) {
            dealerChips += pot;
            playerChips -= currentBet;
            System.out.println("Dealer wins this round!");
        } else {
            System.out.println("NO WINNER");
        }

        // flip dealer cards for player to see
        GUI.showDealerHand();
    }

    public void waitForResponse() {
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // reset the game after each round
    public void reset() {
        deck = new Deck();
        playerHand = new Hand();
        dealerHand = new Hand();
        communityCards = new Hand();
        pot = 0;
        GUI.resetGUI();
    }

    // getters and setters
    public int getPot() {return pot;}
    public int getCurrentBet() {return currentBet;}
    public int getDealerChips() {return dealerChips;}
    public int getPlayerChips() {return playerChips;}
    public void setRaiseAmount(int raiseInt) {raiseAmount = raiseInt;}
    public int getRaiseAmount() {return raiseAmount;}
    public TexasHoldemPanel getGui() {return GUI;}
    public Hand getPlayerHand() {return playerHand;}
    public Hand getDealerHand() {return dealerHand;}
    public Hand getCommunityCards() {return communityCards;}

    public void setPlayerDecision(int decision) {
        playerDecision = decision;
        latch.countDown();
    }

    public int getPlayerDecision() {
        return playerDecision;
    }


    /* ******************************* CONSOLE METHODS *********************************** */

    /*
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

    public void consoleExecuteBettingRound() {
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
    */
}
