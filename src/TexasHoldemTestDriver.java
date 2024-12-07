import GUI.TexasHoldemPanel;

import javax.swing.*;
import java.awt.*;

public class TexasHoldemTestDriver {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setTitle("TexasHoldemTest");
        frame.setPreferredSize(new Dimension(1500, 1000));
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        TexasHoldemPanel texasHoldEm = new TexasHoldemPanel();

        frame.add(texasHoldEm);

        frame.setVisible(true);
        frame.pack();
    }
}
