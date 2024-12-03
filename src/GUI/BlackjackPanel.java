package GUI;

import Logic.*;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class BlackjackPanel extends JPanel {

    private Blackjack game;
    private JPanel playerPanel;
    private JPanel dealerPanel;
    private JLabel playerValueLabel;
    private JLabel dealerValueLabel;
    private JLabel messageLabel;
    private JButton hitButton;
    private JButton standButton;

    public BlackjackPanel(Blackjack game) {
        super();
        this.game = game;
        this.setLayout(null);
        this.setBackground(new Color(0, 100, 0));

        // Dealer panel
        dealerPanel = new JPanel();
        dealerPanel.setLayout(new FlowLayout(FlowLayout.LEFT,5,5));
        dealerPanel.setBounds(200, 50, 900, 300);
        dealerPanel.setOpaque(false);
        this.add(dealerPanel);

        // Dealer value label
        dealerValueLabel = new JLabel("Dealer Value: 0");
        dealerValueLabel.setForeground(Color.white);
        dealerValueLabel.setBounds(60,110,100,20);
        this.add(dealerValueLabel);

        // Player panel
        playerPanel = new JPanel();
        playerPanel.setLayout(new FlowLayout(FlowLayout.LEFT,5,5));
        playerPanel.setBounds(200, 400, 900, 300);
        playerPanel.setOpaque(false);
        this.add(playerPanel);

        // Player value label
        playerValueLabel = new JLabel("Player Value: 0");
        playerValueLabel.setForeground(Color.white);
        playerValueLabel.setBounds(60,470,100,20);
        this.add(playerValueLabel);

        // Message label
        messageLabel = new JLabel(game.getGameMessage());
        messageLabel.setForeground(Color.WHITE);
        messageLabel.setBounds(50, 420, 600, 30);
        this.add(messageLabel);

        // Hit button
        hitButton = new JButton("Hit");
        hitButton.setBounds(50, 470, 100, 30);
        hitButton.addActionListener(e -> handleHit());
        this.add(hitButton);

        // Stand button
        standButton = new JButton("Stand");
        standButton.setBounds(160, 470, 100, 30);
        standButton.addActionListener(e -> handleStand());
        this.add(standButton);

        // Initialize display
        updateDisplay(false);
    }

    private void handleHit() {
        game.playerHit();
        updateDisplay(false);
        if (game.isGameOver())
            endGame();
    }

    private void handleStand() {
        game.playerStand();
        updateDisplay(true);
        endGame();
    }

    public void updateDisplay(boolean showDealerHand) {
        // Clear both panels
        playerPanel.removeAll();
        dealerPanel.removeAll();

        // Update dealer's hand if showDealerHand is true
        List<Card> dealerHand = game.getDealerHand();
        if (showDealerHand) {
            for (Card card : dealerHand) {
                if(card.isFaceUp())
                    card.toggleFaceUp();
                dealerPanel.add(new CardPanel(card));
            }
            dealerValueLabel.setText("Dealer Value: " + game.getDealerValue());
        } else {
            // Shows the first 2 cards of the dealer hand but 2nd card is face down; standard in all casinos
            dealerPanel.add(new CardPanel(dealerHand.getFirst()));
            dealerPanel.add(new CardPanel(CardType.BACK));
            dealerValueLabel.setText("Dealer Value: ?");
        }

        // Update player's hand
        List<Card> playerHand = game.getPlayerHand();
        for (Card card : playerHand) {
            CardPanel cardToAdd = new CardPanel(card);
            if(!card.isFaceUp())
                card.toggleFaceUp();
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
        messageLabel.setText(game.getGameMessage());
    }

}
