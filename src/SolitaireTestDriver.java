import java.util.Scanner;

import Logic.*;
import GUI.*;
import javax.swing.JFrame;

import java.awt.Color;
import java.awt.Dimension;

public class SolitaireTestDriver {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = "";
        SolitairePanel panel = new SolitairePanel();
        panel.setPreferredSize(new Dimension(1500, 1000));
        panel.setBackground(new Color(0, 100, 0));

        JFrame frame = new JFrame("Data Visualization Tool");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(1500, 1000));
        frame.add(panel);

        frame.pack();
        frame.setVisible(true);

        scanner.close();
    }
}
