package GUI;

import Logic.Card;
import Logic.CardType;
import Logic.CrazyEights;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class CrazyEightsPanel extends JPanel {

    private final CrazyEights gameLogic;
    private final ArrayList<CardPanel> playerCardsPanels;
    private final ArrayList<CardPanel> dealerCardsPanels;
    private CardPanel playedPilePanel;
    //For when an 8 is played
    private final JButton[] suitButtons;
    private final JLabel gameMessageLabel;

    public CrazyEightsPanel() {
        gameLogic = new CrazyEights();
        playerCardsPanels = new ArrayList<>();
        dealerCardsPanels = new ArrayList<>();

        setLayout(null);
        setBackground(new Color(0, 100, 0));

        //Played Pile Panel
        playedPilePanel = new CardPanel(gameLogic.getTopDiscardCard(), 80);
        playedPilePanel.getCard().toggleFaceUp();
        add(playedPilePanel);

        //Draw Button
        JButton drawButton = new JButton("Draw Card");
        add(drawButton);

        //Draw Button Action Listener
        drawButton.addActionListener(e -> {
            //Calls draw method to draw a new card''
            if (gameLogic.isPlayerTurn() && gameLogic.isGameOver()){
                gameLogic.drawCard();
                updateCardPanels();
                //To Ensure it is Clickable, listener called on panel
                addCardPanelMouseListener(playerCardsPanels.size()-1);
            }
        });

        //Game Message for Important Updates to User
        gameMessageLabel = new JLabel(gameLogic.getGameMessage());
        gameMessageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gameMessageLabel.setForeground(Color.WHITE);
        add(gameMessageLabel);

        //Buttons for choosing suit upon placing an 8
        JPanel suitButtonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        suitButtonsPanel.setOpaque(false);
        suitButtons = new JButton[4];
        String[] suits = {"Clubs", "Diamonds", "Hearts", "Spades"};
        for (int i = 0; i < 4; i++) {
            suitButtons[i] = new JButton(suits[i]);
            suitButtonsPanel.add(suitButtons[i]);
            suitButtons[i].setVisible(false);
        }
        add(suitButtonsPanel);

        JButton resetButton = new JButton("Reset Game");
        add(resetButton);
        resetButton.addActionListener(e -> {
            gameLogic.resetGame();
            updateCardPanels();
        });


        //All together in case of needed changes
        //playedPilePanel.setBounds(350, 200, playedPilePanel.getWidth(), playedPilePanel.getHeight());
        gameMessageLabel.setBounds(10, 300, 700, 30);
        suitButtonsPanel.setBounds(720, 150, 100, 200);
        resetButton.setBounds(50, 150, 120, 40);
        drawButton.setBounds(50, 250, 120, 40);

        //Initialize the card panels
        updateCardPanels();

    }

    //Method for Update GUI Aspect of Plays
    //Handles the dealers turn updates as well
    private void updateCardPanels() {
        //Reset Panels
        playerCardsPanels.forEach(this::remove);
        dealerCardsPanels.forEach(this::remove);
        playerCardsPanels.clear();
        dealerCardsPanels.clear();

        //Add Player GUI Cards
        int x = 10;
        int y = 400;
        for (int i = 0; i < gameLogic.getPlayerHand().size(); i++) {
            Card card = gameLogic.getPlayerHand().get(i);
            CardPanel cardPanel = new CardPanel(card, 80);
            cardPanel.getCard().toggleFaceUp();
            playerCardsPanels.add(cardPanel);
            add(cardPanel);
            cardPanel.setBounds(x, y, cardPanel.getWidth(), cardPanel.getHeight());
            x += 90;
            addCardPanelMouseListener(i);
        }

        //Dealer GUI Cards (Just Visual so face down)
        x = 10;
        y = 10;
        for(int i = 0; i < gameLogic.getDealerHand().size(); i++) {
            CardPanel cardPanel = new CardPanel(CardType.BACK, 80);
            dealerCardsPanels.add(cardPanel);
            add(cardPanel);
            cardPanel.setBounds(x, y, cardPanel.getWidth(), cardPanel.getHeight());
            x += 90;
        }

        //Played Pile GUI Cards
        if(playedPilePanel != null) remove(playedPilePanel);
        playedPilePanel = new CardPanel(gameLogic.getTopDiscardCard(), 80);
        playedPilePanel.getCard().toggleFaceUp();
        add(playedPilePanel);
        playedPilePanel.setBounds(325, 175, playedPilePanel.getWidth(),playedPilePanel.getHeight());

        //Game Message Update
        gameMessageLabel.setText(gameLogic.getGameMessage());


        revalidate();
        repaint();
    }

    //This method puts mouse listeners on the card panels to be played by user by clicking them
    private void addCardPanelMouseListener(int index) {
        if(index >= 0 && index < playerCardsPanels.size()) {
            playerCardsPanels.get(index).addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    //Basic Game Checks
                    if (gameLogic.isPlayerTurn() && gameLogic.isGameOver()) {
                        Card selectedCard = gameLogic.getPlayerHand().get(index);

                        //For Crazy 8 Instances (Makes Buttons Visible)
                        if (selectedCard.getRank() == Card.Rank.EIGHT) {
                            //Makes Buttons Visible
                            for (JButton button : suitButtons) {
                                button.setVisible(true);
                            }
                            //Action Listeners for Buttons once Visible
                            for (int suitIndex = 0; suitIndex < 4; suitIndex++) {
                                final int finalSuitIndex = suitIndex;
                                suitButtons[suitIndex].addActionListener(e1 -> {
                                    //Card is played through logic method
                                    // And panel updated to remove card from hand
                                    gameLogic.playCard(selectedCard, Card.Suit.values()[finalSuitIndex]);
                                    updateCardPanels();

                                    //Buttons are Hidden Again
                                    for (JButton button : suitButtons) {
                                        button.setVisible(false);
                                    }
                                });
                            }
                            //If to Crazy 8 Check
                            //Much more simple, logic check and hand panel update
                        } else {
                            gameLogic.playCard(selectedCard, null);
                            updateCardPanels();
                        }
                    }
                }
            });
        }
    }

}
