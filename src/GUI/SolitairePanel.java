package GUI;

import Logic.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.List;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.*;

public class SolitairePanel extends JPanel implements MouseListener, MouseMotionListener {
    final int WIDTH = 150;
    Solitaire logic;
    ArrayList<CardPanel> movableCards;
    CardPanel clickedCard;
    int clickOffsetX = 0, clickOffsetY = 0;
    int returnX = 0, returnY = 0;
    CardPanel deckPanel = new CardPanel(CardType.BACK, 100);
    final int drawX = 250, drawY = 10, deckX = 10, deckY = 10;
    ArrayList<Point> foundationCoord;
    ArrayList<Point> columnCoord;
    boolean snapBack = false;
    JPanel background;
    int zValue = 0;

    public SolitairePanel() {
        super();
        logic = new Solitaire();
        movableCards = new ArrayList<>();
        this.setLayout(null);
        this.setOpaque(false);
        this.setBackground(new Color(0, 100, 0));

        initBackground();
        initDeck();
        initFoundations();
        initColumns();
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        repaint();

    }

    private void addPanel(JPanel panel) {
        this.add(panel);
        this.setComponentZOrder(panel, 0);
        zValue++;

    }

    private void initBackground() {
        background = new JPanel();
        background.setLayout(null);
        background.setBackground(new Color(0, 100, 0));
        background.setLocation(0, 0);
        this.setComponentZOrder(background, 0);
        addPanel(background);
    }

    /**
     * Initializes the Foundation stacks
     */
    private void initFoundations() {
        int x = 600, y = 10;
        foundationCoord = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            foundationCoord.add(new Point(x, y));

            // adds placeholders
            var panel = new JPanel();
            panel.setSize(new Dimension(deckPanel.getWidth(), deckPanel.getHeight()));
            panel.setBackground(Color.green);
            panel.setLocation(x, y);
            background.add(panel);
            x += deckPanel.getWidth() + 10;
        }

    }

    /**
     * Initializes the Column stacks
     */
    private void initColumns() {
        int x = 10, y = 400;
        columnCoord = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            columnCoord.add(new Point(x, y - 40));

            // adds placeholders
            var panel = new JPanel();
            panel.setSize(new Dimension(deckPanel.getWidth(), deckPanel.getHeight()));
            panel.setBackground(Color.green);
            panel.setLocation(x, y);
            background.add(panel);
            x += deckPanel.getWidth() + 10;
        }

    }

    /**
     * Ititializes the deck and displays
     */
    private void initDeck() {

        deckPanel = new CardPanel(CardType.BACK, WIDTH);
        deckPanel.setLocation(new Point(deckX, deckY));

        addPanel(deckPanel);
    }

    /**
     * Checks if the deck is clicked
     * If yes, does deck action
     */
    private void isDeckCoord(int x, int y) {
        if (x > deckPanel.getX()
                && y > deckPanel.getY()
                && x < deckPanel.getX() + deckPanel.getWidth()
                && y < deckPanel.getY() + deckPanel.getHeight()) {
            Card next = logic.flipTopCard();
            System.out.println(next);
            if (next == null) {
                return;
            }

            var nextCard = new CardPanel(next, WIDTH);
            nextCard.setLocation(new Point(drawX, drawY));
            addMovableCard(nextCard);
            repaint();
        }
    }

    private void addMovableCard(CardPanel cardPanel) {
        this.add(cardPanel);
        movableCards.add(cardPanel);
        int i = movableCards.size();
        this.setComponentZOrder(background, i + 1);
        for (CardPanel panel : movableCards) {
            this.setComponentZOrder(panel, i);
            i--;

        }

    }

    /**
     * Checks if the a position over a card panel and returns the card panel
     */
    private CardPanel isMovableCardCoord(int x, int y) {
        CardPanel panel;
        for (int i = movableCards.size() - 1; i >= 0; i--) {
            panel = movableCards.get(i);
            if (panel.coordInPanel(x, y)) {
                // removes the card from the display and re-adds to make it
                // the top card
                movableCards.remove(i);
                addMovableCard(panel);
                return panel;
            }
        }

        return null;
    }

    @Override
    public void mouseDragged(MouseEvent event) {
        if (clickedCard != null) {
            int x = event.getX() + clickOffsetX;
            int y = event.getY() + clickOffsetY;

            clickedCard.setLocation(new Point(x, y));
            repaint();
        }
    }

    @Override
    public void mouseMoved(MouseEvent event) {

    }

    @Override
    public void mouseClicked(MouseEvent event) {
        isDeckCoord(event.getX(), event.getY());
    }

    @Override
    public void mousePressed(MouseEvent event) {
        clickedCard = isMovableCardCoord(event.getX(), event.getY());
        if (clickedCard != null) {
            returnX = clickedCard.getX();
            returnY = clickedCard.getY();
            clickOffsetX = clickedCard.getX() - event.getX();
            clickOffsetY = clickedCard.getY() - event.getY();
            repaint();

        }
    }

    @Override
    public void mouseReleased(MouseEvent event) {
        int index = 0;
        if (clickedCard == null) {
            return;
        }
        Point point = inPosition(event.getX(), event.getY());
        if (!snapBack) {
            int num = idColumn(returnX + 1, returnY + 1);
            if (num != -1) {
                columnCoord.set(num,
                        new Point((int) columnCoord.get(num).getX(),
                                (int) columnCoord.get(num).getY() - 40));
            }
        }
        clickedCard.setLocation(point);
        clickedCard = null;
        repaint();
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    /**
     * Checks if the given coordinates is in a location that a card can go
     */
    private Point inPosition(int x, int y) {
        snapBack = false;
        // checks if in columns
        int index = idColumn(x, y);
        if (index != -1) {
            if (logic.addToColumn(index, clickedCard.getCard())) {
                Point point = columnCoord.get(index);
                columnCoord.set(index, new Point((int) point.getX(), (int) point.getY() + 40));
                return columnCoord.get(index);
            }
        }
        // checks if in foundation
        index = idFoundation(x, y);
        if (index != -1) {
            if (logic.addToFoundation(index, clickedCard.getCard())) {
                Point point = foundationCoord.get(index);
                foundationCoord.set(index, new Point((int) point.getX(), (int) point.getY()));
                return foundationCoord.get(index);
            }
        }
        snapBack = true;
        return new Point(returnX, returnY);
    }

    /**
     * Checks if a point is in a card dimension from a given point
     */
    private boolean inCardDimensionAtCoord(Point corner, int mouseX, int mouseY) {
        if (mouseX > corner.getX()
                && mouseY > corner.getY()
                && mouseX < corner.getX() + deckPanel.getWidth()
                && mouseY < corner.getY() + deckPanel.getHeight()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Gets the column that the coords are in if any
     *
     * @return index or -1 for false
     */
    private int idColumn(int x, int y) {
        for (int index = 0; index < columnCoord.size(); index++) {
            Point point = columnCoord.get(index);
            if (inCardDimensionAtCoord(point, x, y)) {
                return index;
            }
        }
        return -1;
    }

    /**
     * Gets the foundation that the coords are in if any
     *
     * @return index or -1 for false
     */
    private int idFoundation(int x, int y) {
        for (int index = 0; index < foundationCoord.size(); index++) {
            Point point = foundationCoord.get(index);
            if (inCardDimensionAtCoord(point, x, y)) {
                return index;
            }
        }
        return -1;
    }

    @Override
    public void setPreferredSize(Dimension dimension) {
        super.setPreferredSize(dimension);
        background.setSize(dimension);
        System.out.println("test");
    }
}
