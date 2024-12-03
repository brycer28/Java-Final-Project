/*
Test driver for the Blackjack game
Created by Mason Simpson
 */

import Logic.*;
import GUI.*;
import javax.swing.*;

public class BlackjackTestDriver {
    public static void main(String[] args) {
        Blackjack game = new Blackjack();
        BlackjackPanel panel = new BlackjackPanel(game);

        JFrame frame = new JFrame("Blackjack Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200,1000);
        frame.add(panel);
        frame.setVisible(true);
    }
}
