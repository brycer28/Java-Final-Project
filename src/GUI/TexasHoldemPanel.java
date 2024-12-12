package GUI;

import Logic.TexasHoldem;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

import Logic.*;

public class TexasHoldemPanel extends JPanel {
    private TexasHoldem logic;
    private ArrayList<JComponent> options;
    JPanel playerHandPanel = new JPanel();
    JPanel dealerHandPanel = new JPanel();
    JPanel communityCardsPanel = new JPanel();
    JPanel optionsPanel = new JPanel();
    JPanel statsPanel = new JPanel();
    final int CARD_WIDTH = 100;
    final int GAME_WIDTH = 1500;
    final int GAME_HEIGHT = 1000;
    final int HAND_WIDTH = 220;
    final int HAND_HEIGHT = 150;
    final int CC_WIDTH = 550;
    final int CC_HEIGHT = 150;
    final int BUTTON_WIDTH = 50;
    final int BUTTON_HEIGHT = 20;
    final int OPT_WIDTH = 500;
    final int OPT_HEIGHT = 70;
    final int STATS_WIDTH = 120;
    final int STATS_HEIGHT = 200;
    final int LABEL_WIDTH = 100;
    final int LABEL_HEIGHT = 40;

    public TexasHoldemPanel(TexasHoldem logic) {
        super();
        this.logic = logic;

        // initalize game panel
        setLayout(null);
        setPreferredSize(new Dimension(GAME_WIDTH, GAME_HEIGHT));
        setBackground(new Color(0, 100, 0));

        // create and add hand panels and community cards
        List<JPanel> handPanels = initHands();
        for (JPanel panel : handPanels) {
            add(panel);
        }

        // create and add an options panel
        optionsPanel = initOptions();
        optionsPanel.setBounds((GAME_WIDTH/2-OPT_WIDTH/2), 40, OPT_WIDTH, OPT_HEIGHT);
        optionsPanel.setBackground(new Color(0, 100, 0));
        add(optionsPanel);

        // create and add a stats panel to show current stats
        statsPanel.setBounds(20, 20, STATS_WIDTH, STATS_HEIGHT);
        statsPanel.setBackground(new Color(0, 150, 0));
        add(statsPanel);
        updateStats();
    }

    public List<JPanel> initHands() {
        // create and add hand panels and community cards panel
        playerHandPanel.setBounds((GAME_WIDTH/2-HAND_WIDTH/2), 600, HAND_WIDTH, HAND_HEIGHT);
        playerHandPanel.setBackground(new Color(0, 150, 0));
        add(playerHandPanel);

        dealerHandPanel.setBounds((GAME_WIDTH/2-HAND_WIDTH/2), 200, HAND_WIDTH, HAND_HEIGHT);
        dealerHandPanel.setBackground(new Color(0, 150, 0));
        add(dealerHandPanel);

        communityCardsPanel.setBounds((GAME_WIDTH/2-CC_WIDTH/2), 400, CC_WIDTH, CC_HEIGHT);
        communityCardsPanel.setBackground(new Color(0, 150, 0));
        add(communityCardsPanel);

        return Arrays.asList(playerHandPanel, dealerHandPanel, communityCardsPanel);
    }

    public JPanel initOptions() {
        options = new ArrayList<>();

        // create check button
        JButton checkButton = new JButton("Check");
        checkButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        checkButton.addActionListener(e -> {
            logic.setPlayerDecision(0);
        });

        // create call button
        JButton callButton = new JButton("Call");
        callButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        callButton.addActionListener(e -> {
            logic.setPlayerDecision(1);
        });

        // create fold button
        JButton foldButton = new JButton("Fold");
        foldButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        foldButton.addActionListener(e -> {
            logic.setPlayerDecision(3);
        });

        // create raise field and button to submit
        JTextField raiseField = new JTextField(5);
        raiseField.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));

        // create submit raise field
        JButton raiseButton = new JButton("Raise");
        raiseButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        raiseButton.addActionListener(e -> {
            String raiseAmt = raiseField.getText();
            try {
                int raiseInt = Integer.parseInt(raiseAmt);
                if (raiseInt < 0) {
                    JOptionPane.showMessageDialog(null, "You can't raise a negative number", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    logic.setRaiseAmount(raiseInt);
                    logic.setPlayerDecision(2);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "You can't raise a negative number", "Error", JOptionPane.ERROR_MESSAGE);
            }
            raiseField.setText("");
        });

        options.add(checkButton);
        options.add(callButton);
        options.add(foldButton);
        options.add(raiseField);
        options.add(raiseButton);

        JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(new GridLayout(1, 5, 20, 0));
        for (JComponent option : options) {
            optionsPanel.add(option);
        }

        return optionsPanel;
    }

    // update all components of the GUI
    public void updateDisplay() {
        SwingUtilities.invokeLater(() -> {
            updateStats();
            updateCommunityCards();

            System.out.println(logic.getPlayerHand());
        });
    }

    // remove and redraw all stats
    public void updateStats() {
        SwingUtilities.invokeLater(() -> {
            statsPanel.removeAll();
            statsPanel.setLayout(new GridLayout(5,1,20,0));

            JLabel potLabel = new JLabel("Current Pot: " + logic.getPot());
            potLabel.setPreferredSize(new Dimension(LABEL_WIDTH,LABEL_HEIGHT));
            statsPanel.add(potLabel);

            JLabel betLabel = new JLabel("Current Bet: " + logic.getCurrentBet());
            betLabel.setPreferredSize(new Dimension(LABEL_WIDTH,LABEL_HEIGHT));
            statsPanel.add(betLabel);

            JLabel playerChipsLabel = new JLabel("Player Chips: " + logic.getPlayerChips());
            playerChipsLabel.setPreferredSize(new Dimension(LABEL_WIDTH,LABEL_HEIGHT));
            statsPanel.add(playerChipsLabel);

            JLabel dealerChipsLabel = new JLabel("Dealer Chips: " + logic.getDealerChips());
            dealerChipsLabel.setPreferredSize(new Dimension(LABEL_WIDTH,LABEL_HEIGHT));
            statsPanel.add(dealerChipsLabel);

            JLabel dealerDecisionLabel = new JLabel("Dealer Decision: " + logic.dealerDecision);
            dealerDecisionLabel.setPreferredSize(new Dimension(LABEL_WIDTH,LABEL_HEIGHT));
            statsPanel.add(dealerDecisionLabel);

            repaint();
            revalidate();
        });
    }

    // remove and redraw all cards in player and dealer hands
    public void updateHands() {
        SwingUtilities.invokeLater(() -> {
            playerHandPanel.removeAll();
            dealerHandPanel.removeAll();

            for (Card card : logic.getPlayerHand()) {
                card.setFaceUp();
                CardPanel cardPanel = new CardPanel(card, CARD_WIDTH);
                playerHandPanel.add(cardPanel);
            }

            for (Card card : logic.getDealerHand()) {
                CardPanel cardPanel = new CardPanel(card, CARD_WIDTH);
                dealerHandPanel.add(cardPanel);
            }

            repaint();
            revalidate();
        });
    }

    // remove and redraw all community cards
    public void updateCommunityCards() {
        SwingUtilities.invokeLater(() -> {
            communityCardsPanel.removeAll();

            for (Card card : logic.getCommunityCards()) {
                card.setFaceUp();
                CardPanel cardPanel = new CardPanel(card, CARD_WIDTH);
                communityCardsPanel.add(cardPanel);
            }

            repaint();
            revalidate();
        });
    }

    public void resetGUI() {
        playerHandPanel.removeAll();
        dealerHandPanel.removeAll();
        communityCardsPanel.removeAll();
        updateStats();
        updateHands();
        updateCommunityCards();

        revalidate();
        repaint();
    }

    public void displayGameOver() {
        JOptionPane.showMessageDialog(null, "Game Over", "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void showDealerHand() {
        SwingUtilities.invokeLater(() -> {
            dealerHandPanel.removeAll();

            for (Card card : logic.getDealerHand()) {
                card.toggleFaceUp();
                CardPanel cardPanel = new CardPanel(card, CARD_WIDTH);
                dealerHandPanel.add(cardPanel);
            }

            repaint();
            revalidate();
        });
    }


    public boolean displayReplayPrompt() {
        int choice = JOptionPane.showConfirmDialog(
                this,
                "Would you like to play another round?",
                "Play Again?",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );
        return choice == JOptionPane.YES_OPTION;
    }

    public JFrame getFrame() {
        return (JFrame) SwingUtilities.getWindowAncestor(this);
    }


}
