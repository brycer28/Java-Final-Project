package GUI;

import javax.swing.*;
import Logic.*;
import java.awt.*;

public class MenuPanel extends JPanel {
    final int BUTTONX = 400, BUTTONY = 500;
    private JLabel gameTitle;
    private JLabel gameSubtitle;
    private JButton pokerButton;
    private JButton solitaireButton;
    private JButton blackjackButton;
    private JButton crazy8Button;
    private TexasHoldem texasHoldemGame;
    private TexasHoldemPanel texasHoldemPanel;
    private SolitairePanel solitairePanel;
    private BlackjackPanel blackjackPanel;
    private Blackjack blackjackGame;
    private CrazyEightsPanel crazy8Panel;

    public MenuPanel() {
        super();
        this.setLayout(null);
        this.setBackground(new Color(0, 100, 0));
        this.setVisible(true);

        showMenu();

    }

    /**
     * Displays the menu
     */
    private void showMenu() {
        // Game title
        gameTitle = new JLabel("52");
        gameTitle.setFont(new Font("Serif", Font.BOLD, 80));
        gameTitle.setBounds(700, 50, 200, 200);
        gameTitle.setForeground(Color.WHITE);
        this.add(gameTitle);

        // Game subtitle
        gameSubtitle = new JLabel("Card Game Suite");
        gameSubtitle.setFont(new Font("Serif", Font.BOLD, 40));
        gameSubtitle.setBounds(585, 120, 800, 200);
        gameSubtitle.setForeground(Color.WHITE);
        this.add(gameSubtitle);

        initGameButtons();
        this.repaint();

    }

    /**
     * Initializes the buttons that start the different games
     */
    private void initGameButtons() {
        int x = BUTTONX;
        int y = BUTTONY;
        solitaireButton = new JButton("Solitaire");
        solitaireButton.addActionListener(e -> startSolitaire());
        solitaireButton.setLocation(x, y);
        solitaireButton.setSize(100, 30);
        this.add(solitaireButton);

        x += 200;

        pokerButton = new JButton("Texas Hold'em");
        pokerButton.addActionListener(e -> startPoker());
        pokerButton.setLocation(x, y);
        pokerButton.setSize(100, 30);
        this.add(pokerButton);

        x += 200;

        blackjackButton = new JButton("Black Jack");
        blackjackButton.addActionListener(e -> startBlackjack());
        blackjackButton.setLocation(x, y);
        blackjackButton.setSize(100, 30);
        this.add(blackjackButton);

        x += 200;

        crazy8Button = new JButton("Crazy 8's");
        crazy8Button.addActionListener(e -> startCrazy8());
        crazy8Button.setLocation(x, y);
        crazy8Button.setSize(100, 30);
        this.add(crazy8Button);
    }

    /**
     * When called, clears out all of the game panels
     */
    public void endGame(JPanel panel) {
        this.remove(panel);
        this.showMenu();
    }

    private void startPoker() {
        this.removeAll();
        texasHoldemGame = new TexasHoldem();
        texasHoldemPanel = texasHoldemGame.getGui();
        texasHoldemPanel.setSize(new Dimension(this.getWidth(), this.getHeight()));
        this.add(texasHoldemPanel);
        this.validate();
        this.repaint();
    }

    private void startSolitaire() {
        this.removeAll();
        solitairePanel = new SolitairePanel();
        solitairePanel.setSize(new Dimension(this.getWidth(), this.getHeight()));
        this.add(solitairePanel);
        solitairePanel.start();
        this.repaint();
        this.revalidate();
    }

    private void startBlackjack() {
        this.removeAll();
        blackjackGame = new Blackjack();
        blackjackPanel = new BlackjackPanel(blackjackGame);
        blackjackPanel.setSize(new Dimension(this.getWidth(), this.getHeight()));
        this.add(blackjackPanel);
        this.validate();
        this.repaint();
    }

    private void startCrazy8() {

    }

}
