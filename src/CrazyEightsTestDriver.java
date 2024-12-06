import GUI.CrazyEightsPanel;

import javax.swing.*;

public class CrazyEightsTestDriver {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Crazy Eights");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        CrazyEightsPanel panel = new CrazyEightsPanel();
        frame.add(panel);
        frame.setVisible(true);
    }
}
