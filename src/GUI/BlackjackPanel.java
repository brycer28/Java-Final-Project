package GUI;

import Logic.*;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class BlackjackPanel extends JPanel {

    private Blackjack game;
    private JPanel topPanel;
    private JPanel playerPanel;
    private JPanel dealerPanel;
    private JLabel playerValueLabel;
    private JLabel dealerValueLabel;
    private JLabel messageLabel;
    private JButton hitButton;
    private JButton standButton;
    private JButton playAgain;
    private final int CARD_WIDTH = 130;

    public BlackjackPanel(Blackjack game) {
        super();
        this.game = game;
        this.setLayout(null);
        this.setBackground(new Color(0, 100, 0));

        // Label at top for game message, hit button and stand button
        topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout());
        topPanel.setBounds(300,50,300,100);
        topPanel.setOpaque(false);
        this.add(topPanel);

        // Message label
        messageLabel = new JLabel(game.getGameMessage());
        messageLabel.setForeground(Color.WHITE);
        messageLabel.setSize(300,30);
        topPanel.add(messageLabel);

        // Hit button
        hitButton = new JButton("Hit");
        hitButton.setSize(100,30);
        hitButton.addActionListener(e -> handleHit());
        topPanel.add(hitButton);

        // Stand button
        standButton = new JButton("Stand");
        standButton.setSize(100,30);
        standButton.addActionListener(e -> handleStand());
        topPanel.add(standButton);

        // Play again button
        playAgain = new JButton("Play Again");
        playAgain.setSize(100,30);
        playAgain.setVisible(false);
        playAgain.addActionListener(e -> resetGame());
        topPanel.add(playAgain);

        // Dealer panel
        dealerPanel = new JPanel();
        dealerPanel.setLayout(new FlowLayout(FlowLayout.LEFT,5,5));
        dealerPanel.setBounds(200, 200, 900, 200);
        dealerPanel.setOpaque(false);
        this.add(dealerPanel);

        // Dealer value label
        dealerValueLabel = new JLabel("Dealer Value: 0");
        dealerValueLabel.setForeground(Color.white);
        dealerValueLabel.setBounds(60,280,100,20);
        this.add(dealerValueLabel);

        // Player panel
        playerPanel = new JPanel();
        playerPanel.setLayout(new FlowLayout(FlowLayout.LEFT,5,5));
        playerPanel.setBounds(200, 500, 900, 200);
        playerPanel.setOpaque(false);
        this.add(playerPanel);

        // Player value label
        playerValueLabel = new JLabel("Player Value: " + game.getPlayerValue());
        playerValueLabel.setForeground(Color.white);
        playerValueLabel.setBounds(60,570,100,20);
        this.add(playerValueLabel);

        // Initialize display
        updatePlayerPanel();
        updateDealerPanel(false);
    }

    private void handleHit() {
        game.playerHit();
        updatePlayerPanel();
        if (game.isGameOver())
            endGame();
    }

    private void handleStand() {
        game.playerStand();
        updateDealerPanel(true);
        updatePlayerPanel();
        endGame();
    }

    public void updateDealerPanel(boolean showDealerHand) {
        // Clear panel
        dealerPanel.removeAll();

        // Update dealer's hand if showDealerHand is true
        List<Card> dealerHand = game.getDealerHand();
        if (showDealerHand) {
            for (Card card : dealerHand) {
                CardPanel cardToAdd = new CardPanel(card, CARD_WIDTH);
                if(!card.isFaceUp())
                    cardToAdd.flipCard();
                dealerPanel.add(cardToAdd);
            }
            dealerValueLabel.setText("Dealer Value: " + game.getDealerValue());
        } else {
            // Shows the first 2 cards of the dealer hand but 2nd card is face down; standard in all casinos
            CardPanel cardToAdd = new CardPanel(dealerHand.getFirst(), CARD_WIDTH);
            dealerPanel.add(cardToAdd);
            cardToAdd = new CardPanel(CardType.BACK, CARD_WIDTH);
            dealerPanel.add(cardToAdd);
            dealerValueLabel.setText("Dealer Value: ?");
        }
    }
    public void updatePlayerPanel() {
        // Clear panel
        playerPanel.removeAll();

        // Update player's hand
        List<Card> playerHand = game.getPlayerHand();
        for (Card card : playerHand) {
            CardPanel cardToAdd = new CardPanel(card, CARD_WIDTH);
            if(!card.isFaceUp())
                cardToAdd.flipCard();
            playerPanel.add(cardToAdd);
        }
        playerValueLabel.setText("Player Value: " + game.getPlayerValue());

        // Update game message
        messageLabel.setText(game.getGameMessage());

        // Refresh panels
        playerPanel.revalidate();
        playerPanel.repaint();
        dealerPanel.revalidate();
        dealerPanel.repaint();
    }

    private void endGame() {
        hitButton.setEnabled(false);
        standButton.setEnabled(false);
        playAgain.setVisible(true);
        messageLabel.setText(game.getGameMessage());
    }

    private void resetGame() {
        // Reset game logic
        game.reset();
        updatePlayerPanel();
        updateDealerPanel(false);

        // Re-enable buttons
        hitButton.setEnabled(true);
        standButton.setEnabled(true);
        playAgain.setVisible(false);

    }

}
