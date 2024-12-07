package Main;

import GUI.BlackjackPanel;
import GUI.MenuPanel;
import Logic.Blackjack;

import javax.swing.*;

public class MainGraphics {
    public static void main(String[] args) {
        MenuPanel menu = new MenuPanel();

        JFrame frame = new JFrame("52 - Card Game Suite");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1500, 1000);

        menu.setSize(1500, 1000);
        frame.add(menu);
        frame.setVisible(true);
    }
}
