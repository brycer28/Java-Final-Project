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
        hitButton.setForeground(Color.WHITE);
        hitButton.setBounds(50, 470, 100, 30);
        hitButton.addActionListener(e -> handleHit());
        this.add(hitButton);

        // Stand button
        standButton = new JButton("Stand");
        standButton.setForeground(Color.WHITE);
        standButton.setBounds(160, 470, 100, 30);
        standButton.addActionListener(e -> handleStand());
        this.add(standButton);

        // Initialize display
        updateDisplay(false);
    }

    private void handleHit() {
        // code to handle hit button
    }

    private void handleStand() {
        // code to handle stand button
    }

    public void updateDisplay(boolean showDealerHand) {
        // code to update display
    }

}
