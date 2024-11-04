package GUI;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.ScrollPane;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import Logic.*;

public class CardPanel extends JPanel {

    public static void main(String[] args) {
        System.out.println("test");

        JFrame frame = new JFrame("Data Visualiztion Tool");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(500, 500));

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setBackground(Color.white);
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 0));
        panel.setBackground(Color.white);

        panel.add(NewCardPanel(CardType.BACK));
        panel.add(NewCardPanel(CardType.JOKER));

        panel.add(NewCardPanel(CardType.ACECLUB));
        panel.add(NewCardPanel(CardType.ACEHEART));
        panel.add(NewCardPanel(CardType.ACESPADE));
        panel.add(NewCardPanel(CardType.ACEDIAMOND));

        panel.add(NewCardPanel(CardType.TWOCLUB));
        panel.add(NewCardPanel(CardType.TWOHEART));
        panel.add(NewCardPanel(CardType.TWOSPADE));
        panel.add(NewCardPanel(CardType.TWODIAMOND));

        panel.add(NewCardPanel(CardType.THREECLUB));
        panel.add(NewCardPanel(CardType.THREEHEART));
        panel.add(NewCardPanel(CardType.THREESPADE));
        panel.add(NewCardPanel(CardType.THREEDIAMOND));

        panel.add(NewCardPanel(CardType.FOURCLUB));
        panel.add(NewCardPanel(CardType.FOURHEART));
        panel.add(NewCardPanel(CardType.FOURSPADE));
        panel.add(NewCardPanel(CardType.FOURDIAMOND));

        panel.add(NewCardPanel(CardType.FIVECLUB));
        panel.add(NewCardPanel(CardType.FIVEHEART));
        panel.add(NewCardPanel(CardType.FIVESPADE));
        panel.add(NewCardPanel(CardType.FIVEDIAMOND));

        panel.add(NewCardPanel(CardType.SIXCLUB));
        panel.add(NewCardPanel(CardType.SIXHEART));
        panel.add(NewCardPanel(CardType.SIXSPADE));
        panel.add(NewCardPanel(CardType.SIXDIAMOND));

        panel.add(NewCardPanel(CardType.SEVENCLUB));
        panel.add(NewCardPanel(CardType.SEVENHEART));
        panel.add(NewCardPanel(CardType.SEVENSPADE));
        panel.add(NewCardPanel(CardType.SEVENDIAMOND));

        panel.add(NewCardPanel(CardType.EIGHTCLUB));
        panel.add(NewCardPanel(CardType.EIGHTHEART));
        panel.add(NewCardPanel(CardType.EIGHTSPADE));
        panel.add(NewCardPanel(CardType.EIGHTDIAMOND));

        panel.add(NewCardPanel(CardType.NINECLUB));
        panel.add(NewCardPanel(CardType.NINEHEART));
        panel.add(NewCardPanel(CardType.NINESPADE));
        panel.add(NewCardPanel(CardType.NINEDIAMOND));

        panel.add(NewCardPanel(CardType.TENCLUB));
        panel.add(NewCardPanel(CardType.TENHEART));
        panel.add(NewCardPanel(CardType.TENSPADE));
        panel.add(NewCardPanel(CardType.TENDIAMOND));

        panel.add(NewCardPanel(CardType.JACKCLUB));
        panel.add(NewCardPanel(CardType.JACKHEART));
        panel.add(NewCardPanel(CardType.JACKSPADE));
        panel.add(NewCardPanel(CardType.JACKDIAMOND));

        panel.add(NewCardPanel(CardType.QUEENCLUB));
        panel.add(NewCardPanel(CardType.QUEENHEART));
        panel.add(NewCardPanel(CardType.QUEENSPADE));
        panel.add(NewCardPanel(CardType.QUEENDIAMOND));

        panel.add(NewCardPanel(CardType.KINGCLUB));
        panel.add(NewCardPanel(CardType.KINGHEART));
        panel.add(NewCardPanel(CardType.KINGSPADE));
        panel.add(NewCardPanel(CardType.KINGDIAMOND));

        scrollPane.add(panel);

        frame.add(scrollPane);
        frame.pack();
        frame.setVisible(true);

    }

    public CardPanel() {
        super();

    }

    public static CardPanel NewCardPanel(CardType type) {
        System.out.println(type.path);
        CardPanel newPanel = new CardPanel();
        newPanel.setBackground(Color.white);
        BufferedImage img;
        String imgPath = "src/cards/" + type.path;
        System.out.println(imgPath);
        // resizes the image
        try {
            img = ImageIO.read(new File(imgPath));
        } catch (IOException ex) {

            System.out.println("bad image read");
            return null;
        }
        ImageIcon icon = resizeImage(100, img);

        // adds the image to a new JLabel and adds to the Purse Panel
        JLabel label = new JLabel(icon);
        newPanel.add(label);
        return newPanel;

    }

    /**
     * Helper function to resize an image to a specified height while keeping the
     * correct aspect ratio
     *
     * @param height the desired height to make the image
     * @param img    the image to resize as a BufferedImage object
     *
     * @return the resized image as an ImageIcon object
     */
    private static ImageIcon resizeImage(int height, BufferedImage img) {

        double ratio = (height * 1.0) / img.getHeight();
        ImageIcon newImg = new ImageIcon(
                img.getScaledInstance((int) (img.getWidth() * ratio), (int) (img.getHeight() * ratio),
                        Image.SCALE_SMOOTH));
        return newImg;
    }

}
