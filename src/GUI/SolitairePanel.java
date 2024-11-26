package GUI;

import Logic.*;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.*;

public class SolitairePanel extends JPanel implements MouseListener, MouseMotionListener {
    Solitaire logic;
    ArrayList<CardPanel> movableCards;
    CardPanel clickedCard;
    int clickOffsetX = 0, clickOffsetY = 0;
    CardPanel deckPanel = new CardPanel(CardType.BACK);

    public SolitairePanel() {
        super();
        logic = new Solitaire();
        movableCards = new ArrayList<>();
        this.setLayout(null);

        initDeck();
        this.addMouseListener(this);
        this.addMouseMotionListener(this);

    }

    /**
     * Ititializes the deck and displays
     */
    private void initDeck() {

        logic.flipTopCard();

        deckPanel = new CardPanel(logic.peekTopCard());
        deckPanel.flipCard();
        deckPanel.setLocation(new Point(10, 10));

        var showCard = new CardPanel(logic.peekDisplayedCard());
        showCard.setLocation(new Point(250, 10));
        this.add(showCard);
        movableCards.add(showCard);
        this.add(deckPanel);
    }

    /**
     * Checks if the a position over a card panel and returns the card panel
     */
    private CardPanel isCardPanelLocation(int x, int y) {
        for (CardPanel panel : movableCards) {
            if (x > panel.getX()
                    && y > panel.getY()
                    && x < panel.getX() + panel.getWidth()
                    && y < panel.getY() + panel.getHeight()) {
                System.out.println("pass");
                return panel;
            }
        }

        System.out.println("fail");
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
    }

    @Override
    public void mousePressed(MouseEvent event) {
        clickedCard = isCardPanelLocation(event.getX(), event.getY());
        if (clickedCard != null) {
            // clickedCard.flipCard();

            clickOffsetX = clickedCard.getX() - event.getX();
            clickOffsetY = clickedCard.getY() - event.getY();
            repaint();

        }
    }

    @Override
    public void mouseReleased(MouseEvent event) {
        clickedCard = null;
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
