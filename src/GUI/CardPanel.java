package GUI;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.ScrollPane;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import Logic.*;

public class CardPanel extends JPanel {
    final public String imageFilePath = "src/cards/";
    final public String imageExtension = ".png";
    BufferedImage image;
    private double angleInRadians = 0.0;
    int width, height;
    CardType cardType;
    int imageWidth = 200;
    boolean faceUp = true;

    public CardPanel() {
        super();
        //this.setBackground(Color.yellow);
        this.setOpaque(false);
        this.setLayout(null);

        // default values
        cardType = CardType.BACK;

        getImage();
        resizeImage();
        setImageRotation();
    }

    public CardPanel(CardType type) {
        super();
        this.setOpaque(false);
        this.setLayout(null);
        this.cardType = type;

        getImage();
        resizeImage();
        setImageRotation();
    }

    /**
     * Factory method that returns a new Instance of the CardPanel class
     * Panel contains the image of the card specified
     *
     * @param type type of card that the card panel should hold the image of 
     * @return CardPanel instance
     */
    public static CardPanel NewCardPanel(CardType type) {
        CardPanel cardPanel = new CardPanel(type);

        return cardPanel;
    }

    private void getImage() {
        String imgPath = "";
        if (faceUp) {
            imgPath = imageFilePath + cardType.path + imageExtension;
        } else {
            imgPath = imageFilePath + CardType.BACK.path + imageExtension;
        }

        // resizes the image
        try {
            image = ImageIO.read(new File(imgPath));
        } catch (IOException ex) {
            System.out.println("bad image read");
            System.exit(1);
        }

    }

    /**
     * Used for regetting the image from the file
     * Use anytime the image is resized to prevent compression artifacts
     */
    private void refreshImage() {
        getImage();
        resizeImage();
        setImageRotation();
    }

    /**
     * Sets the angle the card image is displayed at
     *
     * @param angleInDegrees a value from 0-360
     * @throws IllegalArgumentException when the value is not 0-360
     */
    public void setRotation(int angleInDegrees) {
        if (angleInRadians < 0 || angleInDegrees > 360) {
            System.out.println("Error: angle of card must be 0-360");
            throw new IllegalArgumentException("Error: angle of card must be 0-360");
        }

        angleInRadians = Math.toRadians(angleInDegrees);
        refreshImage();
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    /**
     * Sets the width of the card. Will keep the aspect ratio of the card
     *
     * @param width the width that the card will be displayed
     */
    public void setImageWidth(int width) {
        this.imageWidth = width;
        refreshImage();
    }

    private void setImageRotation() {
        double sin = Math.abs(Math.sin(Math.toRadians(angleInRadians))),
               cos = Math.abs(Math.cos(Math.toRadians(angleInRadians)));
        int w = image.getWidth();
        int h = image.getHeight();

        double height = w * Math.sin(angleInRadians) + h * Math.cos(angleInRadians);
        double width = w * Math.cos(angleInRadians) + h * Math.sin(angleInRadians);
        int neww = (int) Math.round(width);
        int newh = (int) Math.round(height);
        this.width = neww;
        this.height = newh;
        this.setPreferredSize(new Dimension(this.width, this.height));

        BufferedImage rotated = new BufferedImage(neww, newh, image.getType());
        Graphics2D graphic = rotated.createGraphics();
        graphic.translate((neww-w)/2, (newh-h)/2);
        graphic.rotate(angleInRadians, w/2, h/2);
        graphic.drawRenderedImage(image, null);
        graphic.dispose();
        image = rotated;


    }
    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        graphics.drawImage(image, 0, 0, this);
    }

    /**
     * Helper method to get required panel dimension
     */
    private void setPanelDimension(int imageWidth, int imageHeight) {
        double height = imageWidth * Math.sin(angleInRadians) + imageHeight * Math.cos(angleInRadians) + 10;
        double width = imageWidth * Math.cos(angleInRadians) + imageHeight * Math.sin(angleInRadians) + 10;
        //this.width = imageWidth;
        //this.height = imageHeight;
        this.width = (int) Math.round(width);
        this.height = (int) Math.round(height);
        this.setPreferredSize(new Dimension(this.width, this.height));
        System.out.println("width = " + this.width + " height = " + this.height);


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
    private void resizeImage() {

        double ratio = (double) imageWidth / image.getWidth();
        BufferedImage newImg = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        AffineTransform scaler = new AffineTransform();
        scaler.scale(ratio,ratio);
        AffineTransformOp scaleOP = new AffineTransformOp(scaler,AffineTransformOp.TYPE_BILINEAR);
        newImg = scaleOP.filter(image, null);

        this.image = newImg;
    }


    public static void main(String[] args) {
        System.out.println("test");

        JFrame frame = new JFrame("Data Visualiztion Tool");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(1500, 1000));

        JPanel mainPanel = new JPanel();

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setBackground(Color.white);
        JPanel panel = new JPanel();
        //panel.setLayout(new GridLayout(4, 0));
        panel.setBackground(Color.gray);

        var temp = new JPanel();
        temp.setLayout(null);
        var card1 = NewCardPanel(CardType.BACK);
        temp.add(card1);
        card1.setLocation(200,10);

        var card2 = NewCardPanel(CardType.BACK);
        temp.add(card2);
        card2.setLocation(210,20);
        temp.setLayout(null);
        panel.add(temp);

        panel.add(NewCardPanel(CardType.BACK));

        var joker = new CardPanel(CardType.JOKER);
        joker.setRotation(45);
        panel.add(joker);

        for (CardType type : CardType.values()) {
            panel.add(NewCardPanel(type));
        }

        scrollPane.add(panel);
        scrollPane.setPreferredSize(new Dimension(1500,500));

        JPanel xypanel = new JPanel();
        xypanel.setLayout(null);
        xypanel.setPreferredSize(new Dimension(1500,500));

        int x = 0;
        int y = 100;
        for (CardType type : CardType.values()) {
            var c1 = new CardPanel(type);
            c1.setRotation(45);
            panel.add(c1);
            c1.setLocation(x, y);
            xypanel.add(c1);

            x += 20;
        }

        mainPanel.add(scrollPane);
        mainPanel.add(xypanel);
        frame.add(mainPanel);
        frame.pack();
        frame.setVisible(true);

    }

    public double getAngleInRadians() {
        return angleInRadians;
    }

    public void setAngleInRadians(double angleInRadians) {
        this.angleInRadians = angleInRadians;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Dimension getDimension() {
        return new Dimension(width, height);
    }


    private void getImagePath() {
    }

    public void flipCard() {
        faceUp = !faceUp;
        refreshImage();
    }

}
