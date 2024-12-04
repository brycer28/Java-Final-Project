package GUI;

import javax.swing.*;
import Logic.*;
import java.awt.*;

public class MenuPanel extends JPanel {

    private JLabel gameTitle;
    private JLabel gameSubtitle;
    private JButton pokerButton;
    private JButton solitaireButton;
    private JButton blackjackButton;
    private JButton game4Button;
    //private TexasHoldEmPanel pokerPanel;
    private SolitairePanel solitairePanel;
    private BlackjackPanel blackjackPanel;
    //private Game4Panel game4Panel;


    public MenuPanel() {
        super();
        this.setLayout(null);
        this.setBackground(new Color(0,100,0));
        this.setVisible(true);

        // Game title
        gameTitle = new JLabel("52");
        gameTitle.setFont(new Font("Serif", Font.BOLD, 80));
        gameTitle.setBounds(700, 50, 200, 200);
        gameTitle.setForeground(Color.WHITE);
        this.add(gameTitle);

        // Game subtitle
        gameSubtitle = new JLabel("Card Game Suite");
        gameSubtitle.setFont(new Font("Serif", Font.BOLD, 40));
        gameSubtitle.setBounds(585,120, 800, 200);
        gameSubtitle.setForeground(Color.WHITE);
        this.add(gameSubtitle);

    }

    private void startPoker() {

    }

    private void startSolitaire() {

    }

    private void startBlackjack() {

    }

    private void startGame4() {

    }

}
