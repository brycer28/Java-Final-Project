package GUI;

import Logic.*;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class BlackjackPanel extends JPanel {

    private Blackjack game;
    private JPanel playerPanel;
    private JPanel dealerPanel;
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
        dealerPanel.setLayout(new FlowLayout());
        dealerPanel.setBounds(50, 50, 600, 150);
        dealerPanel.setOpaque(false);
        this.add(dealerPanel);

        // Player panel
        playerPanel = new JPanel();
        playerPanel.setLayout(new FlowLayout());
        playerPanel.setBounds(50, 250, 600, 150);
        playerPanel.setOpaque(false);
        this.add(playerPanel);

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
        // Update dealer's hand if showDealerHand is true
        List<Card> dealerHand = game.getDealerHand();
        if (showDealerHand) {
            for (Card card : dealerHand) {
                dealerPanel.add(new CardPanel(card));
            }
        } else {
            // Shows the first 2 cards of the dealer hand but 2nd card is face down; standard in all casinos
            dealerPanel.add(new CardPanel(dealerHand.getFirst()));
            dealerPanel.add(new CardPanel(CardType.BACK));
        }

        // Update player's hand
        List<Card> playerHand = game.getPlayerHand();
        for (Card card : playerHand) {
            playerPanel.add(new CardPanel(card));
        }

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
