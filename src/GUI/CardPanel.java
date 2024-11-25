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

/**
 * This class displays a card image within a JPanel. The image can be rotated
 * and the background is transparent so the panel will not hide anything that
 * is underneath it.
 *
 * @author Travis Clark
 */
public class CardPanel extends JPanel {
    final public String imageFilePath = "src/cards/";
    final public String imageExtension = ".png";
    BufferedImage image;
    private double angleInRadians = 0.0;
    int width, height;
    CardType cardType;
    Card card;
    int imageWidth = 200;

    /**
     * Default constructor that displays a card back that is a Ace of Heart
     */
    public CardPanel() {
        super();
        // this.setBackground(Color.yellow);
        this.setOpaque(false);
        this.setLayout(null);

        // default values
        cardType = CardType.ACEHEART;
        setCard();
        card.toggleFaceUp();

        getImage();
        resizeImage();
        setImageRotation();
    }

    /**
     * Constructor that displays a card of the given CardType
     *
     * @param type the card type that will be displayed
     */
    public CardPanel(CardType type) {
        super();
        this.setOpaque(false);
        this.setLayout(null);
        this.cardType = type;
        setCard();

        getImage();
        resizeImage();
        setImageRotation();
    }

    /**
     * Constructor that displays a card of the given Card
     *
     * @param type the card that will be displayed
     */
    public CardPanel(Card card) {
        super();
        this.setOpaque(false);
        this.setLayout(null);
        this.card = card;
        card.toggleFaceUp();
        setCardType(card);

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

    /**
     * Sets the width of the card. Will keep the aspect ratio of the card
     *
     * @param width the width that the card will be displayed
     */
    public void setImageWidth(int width) {
        this.imageWidth = width;
        refreshImage();
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        graphics.drawImage(image, 0, 0, this);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    /**
     * Helper method to get required panel dimension
     */
    private void setPanelDimension(int imageWidth, int imageHeight) {
        double height = imageWidth * Math.sin(angleInRadians) + imageHeight * Math.cos(angleInRadians) + 10;
        double width = imageWidth * Math.cos(angleInRadians) + imageHeight * Math.sin(angleInRadians) + 10;
        // this.width = imageWidth;
        // this.height = imageHeight;
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
        scaler.scale(ratio, ratio);
        AffineTransformOp scaleOP = new AffineTransformOp(scaler, AffineTransformOp.TYPE_BILINEAR);
        newImg = scaleOP.filter(image, null);

        this.image = newImg;
    }

    private void getImagePath() {
    }

    private void setCard() {
        char rankChar = cardType.path.charAt(0);
        char suiteChar = cardType.path.charAt(1);

        Card.Suit suit = switch (suiteChar) {
            case 'h' -> Card.Suit.HEARTS;
            case 'd' -> Card.Suit.DIAMONDS;
            case 'c' -> Card.Suit.CLUBS;
            case 's' -> Card.Suit.SPADES;
            default -> Card.Suit.HEARTS;
        };

        Card.Rank rank = switch (rankChar) {
            case '2' -> Card.Rank.TWO;
            case '3' -> Card.Rank.THREE;
            case '4' -> Card.Rank.FOUR;
            case '5' -> Card.Rank.FIVE;
            case '6' -> Card.Rank.SIX;
            case '7' -> Card.Rank.SEVEN;
            case '8' -> Card.Rank.EIGHT;
            case '9' -> Card.Rank.NINE;
            case '1' -> Card.Rank.TEN;
            case 'j' -> Card.Rank.JACK;
            case 'q' -> Card.Rank.QUEEN;
            case 'k' -> Card.Rank.KING;
            case 'a' -> Card.Rank.ACE;
            default -> Card.Rank.ACE;
        };

        this.card = new Card(suit, rank);
        card.toggleFaceUp();

    }

    private void getImage() {
        String imgPath = "";
        if (card.isFaceUp()) {
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

    private void setCardType(Card card) {
        String typeAcronym = card.getImgPath();
        for (CardType type : CardType.values()) {
            if (type.path.equals(typeAcronym)) {
                this.cardType = type;
            }
        }

    }

    /**
     * Used for resetting the image from the file
     * Use anytime the image is resized to prevent compression artifacts
     */
    private void refreshImage() {
        getImage();
        resizeImage();
        setImageRotation();
    }

    private void setImage(BufferedImage image) {
        this.image = image;
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
        graphic.translate((neww - w) / 2, (newh - h) / 2);
        graphic.rotate(angleInRadians, w / 2, h / 2);
        graphic.drawRenderedImage(image, null);
        graphic.dispose();
        image = rotated;

    }

    public static void main(String[] args) {

        JFrame frame = new JFrame("Data Visualiztion Tool");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(1500, 1000));

        JPanel mainPanel = new JPanel();

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setBackground(Color.white);
        JPanel panel = new JPanel();
        // panel.setLayout(new GridLayout(4, 0));
        panel.setBackground(Color.gray);

        var c2 = new CardPanel();
        panel.add(c2);
        panel.add(new CardPanel(new Card(Card.Suit.CLUBS, Card.Rank.SIX)));

        var c3 = new CardPanel(CardType.TENCLUB);
        c3.setRotation(45);
        c3.flipCard();
        panel.add(c3);
        c3.setCard(new Card(Card.Suit.HEARTS, Card.Rank.TWO));

        for (CardType type : CardType.values()) {
            panel.add(NewCardPanel(type));
        }

        scrollPane.add(panel);
        scrollPane.setPreferredSize(new Dimension(1500, 500));

        JPanel xypanel = new JPanel();
        xypanel.setLayout(null);
        xypanel.setPreferredSize(new Dimension(1500, 500));

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

    /**
     * Flips the card opposite of its current face orientation
     * If face up makes it face down and vice versa
     */
    public void flipCard() {
        card.toggleFaceUp();
        refreshImage();
    }

    /**
     * Gets the card that the CardPanel is contains
     *
     * @return Card object that is contained in the panel
     */
    public Card getCard() {
        return card;
    }

    /**
     * Sets the Card displayed in the panel. Updates the panel
     *
     * @param card Card object representing the card that is to be displayed
     */
    public void setCard(Card card) {
        setCardType(card);
        refreshImage();
    }

}
