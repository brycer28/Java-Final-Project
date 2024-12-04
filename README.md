52 - Java Final Project

52 is a card game suite containing 4 card games. The four games are: Texas Hold'em, Blackjack, Baccarat, and Solitaire. More information for each game will be listed below.

This project is done in Java 22 for Object-Oriented Programming w/ Java at University of Central Arkansas Fall 2024.


Game 1 - Texas Hold'em - Bryce Ritter

Each player is dealt 2 cards, then 5 community cards are dealt in 3 stages. Each stage players have the option to check, call, raise, or fold. Players seek the best five-card poker hand out of the 7 cards, building and betting on their hand as the round progresses. A player wins the round if all other players fold, or if there are other players after the final betting round initiating a showdown. Upon the showdown, players compare their best 5 card and the winner earns the pot containing all bets from the current round.

Betting rounds
- Preflop - after players have only seen their 2-card hand
- Flop - after the first 3 community cards have been dealt (the flop)
- Turn - after the 4th card has been dealt (turn)
- River - after the 5th and final card has been dealt (river)

Betting options:
- Call - match the bet for the current round to continue to play
- Raise - raise the bet amount for the current round
- Check - opt to not raise and continue to play(only if no previous players have raised)
- Fold - forfeit your hand and the current round 

Hand Values (ranked low-high):
- High-Card - the highest value of any card in a players hand 
- Pair - two cards of the same value (ex. J, J)
- Two Pair - two sets of two cards with the same value (ex. 6, 6, K, K)
- Three of a Kind - three cards of the same value (ex. 9, 9, 9)
- Straight - a sequence of five cards, not of the same suit (ex. 8, 9, 10, J, Q)
- Flush - five cards of the same suit (ex. 4, 9, K, 2, A all hearts)
- Full House - one pair and three of a kind (ex. 5, 5, A, A, A)
- Four of a Kind - four cards of the same value (ex. 3, 3, 3, 3)
- Straight Flush - a straight of the same suit (ex. 4, 5, 6, 7, 8 all spades)
- Royal Flush - a straight from ten to ace of the same suit (ex. 10, J, Q, K, A all diamonds)
Game - Solitare - Travis Clark

Game 3 - Blackjack - Mason Simpson

The objective of blackjack is to beat the dealer; you aren't competing with anybody else sitting at the table, just the dealer. The game itself is very simple; you want your hand to be closer to 21 than the dealer's hand without exceeding 21. Each card has a point value: numbered cards are worth their face value, face cards (jack, queen, king) are worth 10 points, and an ace is worth either 1 point or 11 points depending on which one meets your goals the most. The game begins with the player and dealer receiving 2 cards. The player can then choose to "hit" (take another card), or "stand" (keep their current hand). Whenever the player stands, the dealer with then keep hitting until they either win or bust.

Game 4 - Crazy Eights - Logan Flora
 Kind of like uno with playing cards, where you play with a computer taking turns placing cards from your hand to the playing deck, only if its the same suit or number as the card faced up
- Player and AI start with 7 cards, 2 play game
- Player first, pick a card that matches either suit or number to the face up card, pulling 1 card from the deck in unable
- Computer's Turn, they will do the same
-     Computer choses first match in ArrayList
-     Computer choses Crazy 8 suit randomly
- U can also place an 8 of the same suit, changing the suit to what you like, allowing for the next player to pick any number of that suit
- Winner is the first to empty their hand
- If deck is all in hands of player/dealer, winner is one with the least amount of cards (Very Unlikely in this version of the game) 