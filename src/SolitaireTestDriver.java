
import java.util.Scanner;

import Logic.*;
import GUI.*;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Dimension;

public class SolitaireTestDriver extends JPanel {
    final static int SCREENWIDTH = 1280, SCREENHEIGHT = 720;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Solitaire");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(new Dimension(SCREENWIDTH, SCREENHEIGHT));
        frame.setPreferredSize(new Dimension(SCREENWIDTH, SCREENHEIGHT));

        SolitaireTestDriver screenPanel = new SolitaireTestDriver();
        screenPanel.setSize(new Dimension(frame.getWidth(), frame.getHeight()));
        frame.add(screenPanel);
        frame.pack();
        frame.setVisible(true);

    }

    private JButton solitaireButton;
    private SolitairePanel solitairePanel;

    public SolitaireTestDriver() {
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
        initGameButtons();
        this.repaint();

    }

    /**
     * Initializes the buttons that start the different games
     */
    private void initGameButtons() {
        int x = 200;
        int y = 200;
        solitaireButton = new JButton("Start Solitaire");
        solitaireButton.addActionListener(e -> startSolitaire());
        solitaireButton.setLocation(x, y);
        solitaireButton.setSize(100, 30);
        this.add(solitaireButton);
    }

    /**
     * When called, clears out all of the game panels
     */
    public void endGame(JPanel panel) {
        this.remove(panel);
        this.showMenu();
    }

    private void startSolitaire() {
        this.removeAll();
        solitairePanel = new SolitairePanel();
        solitairePanel.setSize(new Dimension(this.getWidth(), this.getHeight()));
        this.add(solitairePanel);
        solitairePanel.start();
    }

}
