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
    final int CARDVERTSPACE = 40;
    final int CARDHORIZSPACE = 40;
    final int COLUMNSTARTX = 20;
    final int COLUMNSTARTY = 400;
    Solitaire logic;
    ArrayList<CardPanel> movableCards;
    ArrayList<CardPanel> deck;
    ArrayList<ArrayList<CardPanel>> columns;
    ArrayList<ArrayList<CardPanel>> foundations;
    CardPanel clickedCard;
    ArrayList<CardPanel> movingCards;
    int clickOffsetX = 0, clickOffsetY = 0;
    int returnX = 0, returnY = 0;
    int returnColumn = -1;
    int returnFoundation = -1;
    CardPanel deckPanel = new CardPanel(CardType.BACK, WIDTH);
    final int drawX = 250, drawY = 10, deckX = 10, deckY = 10;
    ArrayList<Point> foundationCoord;
    ArrayList<Point> columnCoord;
    JPanel background;
    int zValue = 0;

    public SolitairePanel() {
        super();
        logic = new Solitaire();
        movableCards = new ArrayList<>();
        movingCards = new ArrayList<>();
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
        foundations = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            foundationCoord.add(new Point(x, y));
            foundations.add(new ArrayList<>());
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
        columns = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            columns.add(new ArrayList<>());
            columnCoord.add(new Point(x, y - CARDHORIZSPACE));

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
     * Initializes the deck and displays
     */
    private void initDeck() {

        deckPanel = new CardPanel(CardType.BACK, WIDTH);
        deckPanel.setLocation(new Point(deckX, deckY));
        deck = new ArrayList<>();

        addPanel(deckPanel);
    }

    /**
     * Checks if the deck is clicked
     * If yes, does deck action
     */
    private boolean isDeckCoord(int x, int y) {
        if (x > deckPanel.getX()
                && y > deckPanel.getY()
                && x < deckPanel.getX() + deckPanel.getWidth()
                && y < deckPanel.getY() + deckPanel.getHeight()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * removes the displayed cards beside the deck
     */
    private void clearDeck() {
        for (CardPanel panel : deck) {
            movableCards.remove(panel);
            this.remove(panel);
        }
        deck.clear();
        repaint();
    }

    /**
     * Displays the next card in the deck
     */
    private void showNextCard() {

        System.out.println("deck index = " + logic.getDeckIndex());
        logic.printDeck();

        // hides the deck card if the deck is empty or all cards are displayed
        if (logic.getDeckIndex() == logic.getDeckSize() - 1) {
            deckPanel.setVisible(false);
        } else if (!deckPanel.isVisible() && logic.getDeckSize() != 0) {
            // sets the deck card to visible and clears the displayed cards
            // early return to not iterate the card once
            deckPanel.setVisible(true);
            clearDeck();
            return;
        }

        Card next = logic.flipTopCard();
        if (next == null) {
            return;
        }

        if (next.isFaceUp()) {
            next.toggleFaceUp();
        }
        var nextCard = new CardPanel(next, WIDTH);
        nextCard.setLocation(new Point(drawX, drawY));
        addMovableCard(nextCard);
        deck.add(nextCard);

        repaint();

    }

    /**
     * Adds a card to the movable card list
     */
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
     * Checks if the position is over a card panel and returns the card panel
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
        int offset = 0;
        for (CardPanel panel : movingCards) {
            int x = event.getX() + clickOffsetX;
            int y = event.getY() + clickOffsetY;

            panel.setLocation(new Point(x, y + offset));
            offset += CARDVERTSPACE;
            this.setComponentZOrder(panel, 0);
        }
        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent event) {

    }

    @Override
    public void mouseClicked(MouseEvent event) {
        if (isDeckCoord(event.getX(), event.getY())) {
            showNextCard();
        }
    }

    @Override
    public void mousePressed(MouseEvent event) {
        clickedCard = isMovableCardCoord(event.getX(), event.getY());
        if (clickedCard != null) {
            movingCards = getMovingCards(clickedCard, idColumnWhole(event.getX(), event.getY()));
            returnX = clickedCard.getX();
            returnY = clickedCard.getY();
            clickOffsetX = clickedCard.getX() - event.getX();
            clickOffsetY = clickedCard.getY() - event.getY();
            returnColumn = idColumnWhole(event.getX(), event.getY());
            returnFoundation = idFoundation(event.getX() + 1, event.getY() + 1);
            System.out.println(returnColumn + ":" + returnFoundation);
            repaint();

        }
    }

    @Override
    public void mouseReleased(MouseEvent event) {
        moveCard(event.getX(), event.getY());
        clickedCard = null;
        movingCards.clear();
    }

    /**
     * Gets the cards that will be moving with the selected card.
     * Returned ArrayList includes the clicked card.
     */
    private ArrayList<CardPanel> getMovingCards(CardPanel card, int columnNum) {
        var list = new ArrayList<CardPanel>();
        list.add(card);

        // only check required is if the card is from a column
        if (columnNum != -1) {
            boolean foundCard = false;
            for (CardPanel currentCard : columns.get(columnNum)) {
                if (currentCard == card) {
                    foundCard = true;
                } else if (foundCard) {
                    list.add(currentCard);
                }
            }
        }

        return list;
    }

    /**
     * Checks if the move is valid and moves the card in the logic
     */
    private boolean moveCardInLogic(int x, int y) {
        int columnNum = idColumn(x, y);
        int foundationNum = idFoundation(x, y);
        System.out.println(columnNum);
        boolean check = false;
        // if the card is from the deck, remove from deck
        if (deck.size() != 0 && clickedCard == deck.getLast()) {
            if (foundationNum != -1) {
                check = logic.addToFoundation(foundationNum, columnNum, clickedCard.getCard());
            } else {
                check = logic.addToColumn(columnNum, clickedCard.getCard());
            }

            if (check) {
                deck.remove(clickedCard);
                logic.removeDisplayedCard();
                return true;
            }
        }
        // if the card is from another column move the card stack to new column
        else if (returnColumn != -1) {
            // if the card being added to a foundation, try to add and
            if (foundationNum != -1) {
                check = logic.addToFoundation(foundationNum, columnNum, clickedCard.getCard());
            } else if (columnNum != -1) {
                check = logic.moveCardToColumn(returnColumn, columnNum, clickedCard.getCard());
            } else {
                check = false;
            }
        }

        return check;
    }

    /**
     * Moves the card that was held to the new position if it is valid
     * If it is not valid, moves it back to it's old position
     */
    private void moveCard(int x, int y) {
        int index = 0;
        if (clickedCard == null) {
            return;
        }
        boolean snapBack = inPosition(x, y);
        boolean check = false;
        int newFoundation = returnColumn;
        int newColumn = returnColumn;
        // check for a move
        if (!snapBack) {
            check = moveCardInLogic(x, y);
            if (check) {

                System.out.println("logic pass");
                newColumn = idColumn(x, y);
                newFoundation = idFoundation(x, y);
            } else {

                System.out.println("logic fail");
            }
        }

        if (!check && returnX == drawX && returnY == drawY) {

            System.out.println("return");
            clickedCard.setLocation(new Point(returnX, returnY));
        } else {

            System.out.println("move");
            // move the cards
            if (returnColumn != -1) {
                System.out.println("removing from col");
                removeClickedFromColumn();
            } else if (returnFoundation != -1) {
                System.out.println("removing from found");
                removeClickedFromFoundation();
            }

            // refreshes the columns if moving between columns
            if (newColumn != -1) {
                System.out.println("add to col");
                addClickedToColumn(newColumn);
            }
            // if the card is added to a foundaiton
            else if (newFoundation != -1) {
                System.out.println("add to foud");
                addClickedToFoundation(newFoundation);
            } else {
                System.out.println(returnColumn + ":" + returnFoundation);
                if (returnColumn != -1) {
                    System.out.println("reseting to col");
                    addClickedToColumn(returnColumn);
                } else if (returnFoundation != -1) {
                    System.out.println("reseting to found");
                    removeClickedFromFoundation();
                    addClickedToFoundation(returnFoundation);
                }

            }
        }
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
    private boolean inPosition(int x, int y) {
        boolean snapBack = true;
        // checks if in columns
        int index = idColumn(x, y);
        if (index != -1) {
            snapBack = false;
        }
        // checks if in foundation
        index = idFoundation(x, y);
        if (index != -1) {
            snapBack = false;
        }

        return snapBack;
    }

    /**
     * Checks if a point is in a card dimension from a given point
     */
    private boolean inCardDimensionAtCoord(Point corner, int mouseX, int mouseY) {
        if (mouseX >= corner.getX()
                && mouseY >= corner.getY()
                && mouseX <= corner.getX() + deckPanel.getWidth()
                && mouseY <= corner.getY() + deckPanel.getHeight()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Checks if a point is in a given dimension from a given point
     */
    private boolean inDimensionAtCoord(Point corner, int mouseX, int mouseY, int width, int height) {
        if (mouseX >= corner.getX()
                && mouseY >= corner.getY()
                && mouseX <= corner.getX() + width
                && mouseY <= corner.getY() + height) {
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

    private int idColumnWhole(int x, int y) {
        for (int index = 0; index < columnCoord.size(); index++) {
            Point point = columnCoord.get(index);
            int height = columns.get(index).size() * CARDVERTSPACE;
            Point newPoint = new Point((int) point.getX(), (int) point.getY() - height);
            height = height + deckPanel.getHeight();

            if (inDimensionAtCoord(newPoint, x, y, WIDTH, height)) {
                System.out.println("PASS");
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

    /**
     * Adds clicked card panels to a Column
     */
    private void addClickedToColumn(int columnNum) {
        for (CardPanel panel : movingCards) {
            movableCards.remove(panel);
            movableCards.add(panel);
            columns.get(columnNum).add(panel);
            incrementColumnCoord(columnNum);
            panel.setLocation(columnCoord.get(columnNum));
        }
    }

    /**
     * Removes the clicked card panels from their original column
     */
    private void removeClickedFromColumn() {
        for (CardPanel panel : movingCards) {
            columns.get(returnColumn).remove(panel);
            decrementColumnCoord(returnColumn);
        }
    }

    /**
     * Adds clicked to foundation
     */
    private void addClickedToFoundation(int foundationNum) {
        foundations.get(foundationNum).add(clickedCard);
        clickedCard.setLocation(foundationCoord.get(foundationNum));
    }

    /**
     * Removes clicked from the foundation
     */
    private void removeClickedFromFoundation() {
        foundations.get(returnFoundation).remove(clickedCard);
    }

    private void incrementColumnCoord(int columnNum) {
        columnCoord.set(columnNum,
                new Point((int) columnCoord.get(columnNum).getX(),
                        (int) columnCoord.get(columnNum).getY() + CARDVERTSPACE));
    }

    private void decrementColumnCoord(int columnNum) {
        columnCoord.set(columnNum,
                new Point((int) columnCoord.get(columnNum).getX(),
                        (int) columnCoord.get(columnNum).getY() - CARDVERTSPACE));
    }
}
