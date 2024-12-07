package Main;

import java.util.Scanner;

import Logic.*;
import GUI.*;
import javax.swing.JFrame;

import java.awt.Color;
import java.awt.Dimension;

public class SolitaireTestDriver {
    final static int SCREENWIDTH = 1500, SCREENHEIGHT = 1000;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Solitaire");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        SolitairePanel panel = new SolitairePanel();
        panel.setPreferredSize(new Dimension(SCREENWIDTH, SCREENHEIGHT));

        frame.add(panel);

        frame.pack();
        frame.setVisible(true);

    }
}
