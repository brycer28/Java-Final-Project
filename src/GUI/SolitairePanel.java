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

/**
 * Solitaire Game Panel that displays and allows the play of a game of
 * Solitaire.
 * To start the start() method must be called.
 * When the game is exited, the super class must be a JPanel and it must have a
 * method endGame(JPanel). This method is called when the game is ended and
 * should
 * clear the SolitairePanel from the display.
 * 
 * @author Travis Clark
 */
public class SolitairePanel extends JPanel implements MouseListener, MouseMotionListener {
    int cardWidth = 150;
    int cardVertSpace = 30;
    final int CARDHORIZSPACE = 20;
    final int COLUMNSTARTX = 20;
    int columnStartY = 300;
    Solitaire logic;
    ArrayList<CardPanel> movableCards;
    ArrayList<CardPanel> unflippedCards;
    ArrayList<CardPanel> deck;
    ArrayList<ArrayList<CardPanel>> columns;
    ArrayList<ArrayList<CardPanel>> foundations;
    ArrayList<JPanel> placeHolders;
    CardPanel clickedCard;
    ArrayList<CardPanel> movingCards;
    int clickOffsetX = 0, clickOffsetY = 0;
    int returnX = 0, returnY = 0;
    int returnColumn = -1;
    int returnFoundation = -1;
    CardPanel deckPanel;
    final int drawX = 200, drawY = 10, deckX = 10, deckY = 10;
    ArrayList<Point> foundationCoord;
    ArrayList<Point> columnCoord;
    JPanel background;
    JButton restartButton;
    JButton quitButton;

    public SolitairePanel() {
        super();
    }

    /**
     * When called, creates the game and displays it.
     * Must be called or the screen is blank
     */
    public void start() {
        initComponents();
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
    }

    /**
     * Initializes the elements that will be displayed when the game is first
     * started
     */
    private void initComponents() {
        logic = new Solitaire();
        movableCards = new ArrayList<>();
        unflippedCards = new ArrayList<>();
        movingCards = new ArrayList<>();
        deckPanel = new CardPanel(CardType.BACK, cardWidth);
        this.setLayout(null);
        this.setOpaque(false);

        initBackground();
        initDeck();
        initFoundations();
        initColumns();
        initButtons();
        updateDisplayedZOrder();
        repaint();
    }

    /**
     * Helper method that adds the panel to the game panel and sets it Z order
     */
    private void addPanel(JPanel panel) {
        this.add(panel);
        this.setComponentZOrder(panel, 0);

    }

    /**
     * Initializes the Background that is displayed behind the cars
     */
    private void initBackground() {
        background = new JPanel();
        placeHolders = new ArrayList<>();
        background.setLayout(null);
        background.setBackground(new Color(0, 100, 0));
        background.setLocation(0, 0);
        background.setSize(this.getWidth(), this.getHeight());
        addPanel(background);
    }

    /**
     * Initializes the Buttons for controlling the game
     */
    private void initButtons() {
        restartButton = new JButton("Restart");
        restartButton.addActionListener(e -> restartGame());
        restartButton.setLocation((int) foundationCoord.getLast().getX() + 250, 100);
        restartButton.setSize(100, 30);
        this.add(restartButton);
        this.setComponentZOrder(restartButton, 0);

        // quit
        quitButton = new JButton("Quit");
        quitButton.addActionListener(e -> endGame());
        quitButton.setLocation((int) foundationCoord.getLast().getX() + 250, 200);
        quitButton.setSize(100, 30);
        this.add(quitButton);
        this.setComponentZOrder(quitButton, 0);
    }

    /**
     * Destroys the solitaire panel
     */
    private void endGame() {
        if (this.getParent() instanceof MenuPanel menu) {
            menu.endGame(this);
        }
    }

    /**
     * Resets the game board and starts a new game
     */
    private void restartGame() {
        // clear screen
        this.removeAll();
        initComponents();
    }

    /**
     * Initializes the Foundation stacks
     */
    private void initFoundations() {
        int x = 500, y = 10;
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
            placeHolders.add(panel);
            x += deckPanel.getWidth() + CARDHORIZSPACE;
        }

    }

    /**
     * Initializes the Column stacks
     */
    private void initColumns() {
        int x = COLUMNSTARTX, y = columnStartY;
        columnCoord = new ArrayList<>();
        columns = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            columns.add(new ArrayList<>());
            columnCoord.add(new Point(x, y));

            // adds placeholders
            var panel = new JPanel();
            panel.setSize(new Dimension(deckPanel.getWidth(), deckPanel.getHeight()));
            panel.setBackground(Color.green);
            panel.setLocation(x, y);
            background.add(panel);
            placeHolders.add(panel);
            x += deckPanel.getWidth() + CARDHORIZSPACE;

            initColumn(i);
        }
    }

    /**
     * Initializes the specified column
     */
    private void initColumn(int columnNum) {
        int index = 0;
        for (int j = columnNum; j >= 0; j--) {
            Card card = logic.getColumn(columnNum).get(index);
            index++;
            var cardPanel = new CardPanel(card, cardWidth);
            cardPanel.flipCard();

            columns.get(columnNum).add(cardPanel);
            cardPanel.setLocation(columnCoord.get(columnNum));
            addNonMovableCard(cardPanel);
            incrementColumnCoord(columnNum);

            cardPanel.flipCard();

            if (j == 0) {
                makeMovableCard(cardPanel);
            }
        }
    }

    /**
     * Initializes the deck and displays
     */
    private void initDeck() {

        deckPanel.setLocation(new Point(deckX, deckY));
        deck = new ArrayList<>();

        addPanel(deckPanel);
    }

    /**
     * Checks if the deck is clicked
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

        // if the card is not already face up, makes face up
        if (next.isFaceUp()) {
            next.toggleFaceUp();
        }

        // display the next card
        var nextCard = new CardPanel(next, cardWidth);
        nextCard.flipCard();
        nextCard.setLocation(new Point(drawX, drawY));
        addMovableCard(nextCard);
        deck.add(nextCard);

        repaint();

    }

    /**
     * Makes a un-flipped card a flipped card
     */
    private void makeMovableCard(CardPanel cardPanel) {
        cardPanel.flipCard();
        unflippedCards.remove(cardPanel);
        movableCards.add(cardPanel);
        updateDisplayedZOrder();
    }

    /**
     * Adds a card to the movable card list
     */
    private void addMovableCard(CardPanel cardPanel) {
        this.add(cardPanel);
        movableCards.add(cardPanel);
        updateDisplayedZOrder();
    }

    /**
     * Adds a card to the non-movable card list
     */
    private void addNonMovableCard(CardPanel cardPanel) {
        this.add(cardPanel);
        unflippedCards.add(cardPanel);
        updateDisplayedZOrder();
    }

    /**
     * updates the z order for all of the cards
     */
    private void updateDisplayedZOrder() {
        int i = unflippedCards.size() + movableCards.size();
        this.setComponentZOrder(deckPanel, 0);
        this.setComponentZOrder(background, this.getComponentCount() - 1);
        for (CardPanel panel : unflippedCards) {
            this.setComponentZOrder(panel, i);
            i--;
        }
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
                return panel;
            }
        }

        return null;
    }

    private void makeCardTopCard(CardPanel panel, int i) {
        // removes the card from the display and re-adds to make it
        // the top card
        movableCards.remove(i);
        addMovableCard(panel);
    }

    @Override
    public void mouseDragged(MouseEvent event) {
        int offset = 0;

        for (CardPanel panel : movingCards) {
            int x = event.getX() + clickOffsetX;
            int y = event.getY() + clickOffsetY;

            panel.setLocation(new Point(x, y + offset));
            offset += cardVertSpace;
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
        boolean check = false;
        // if the card is from the deck, remove from deck
        if (deck.size() != 0 && clickedCard == deck.getLast()) {
            if (foundationNum != -1) {
                check = logic.addToFoundation(foundationNum, returnColumn, clickedCard.getCard());
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
                check = logic.addToFoundation(foundationNum, returnColumn, clickedCard.getCard());
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
                newColumn = idColumn(x, y);
                newFoundation = idFoundation(x, y);
            } else {
            }
        }

        if (!check && returnX == drawX && returnY == drawY) {
            clickedCard.setLocation(new Point(returnX, returnY));
        } else {
            // move the cards
            // removes from old location
            if (returnColumn != -1) {
                removeClickedFromColumn();
            } else if (returnFoundation != -1) {
                removeClickedFromFoundation();
            }

            // moves the card to the new location
            if (newColumn != -1) {
                addClickedToColumn(newColumn);
            }
            // if the card is added to a foundation
            else if (newFoundation != -1) {
                addClickedToFoundation(newFoundation);
            } else {
                if (returnColumn != -1) {
                    addClickedToColumn(returnColumn);
                } else if (returnFoundation != -1) {
                    removeClickedFromFoundation();
                    addClickedToFoundation(returnFoundation);
                }

            }
        }

        // flips the card that was exposed if the card was moved from a col
        if (check && returnColumn != -1) {
            flipTopCardInColumn(returnColumn);
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
        if (index != -1 && returnColumn != index) {
            snapBack = false;
        }
        // checks if in foundation
        index = idFoundation(x, y);
        if (index != -1 && returnFoundation != index) {
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
            int height = columns.get(index).size() * cardVertSpace;
            Point newPoint = new Point((int) point.getX(), (int) point.getY() - height);
            height = height + deckPanel.getHeight();

            if (inDimensionAtCoord(newPoint, x, y, cardWidth, height)) {
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

    /**
     * Adds clicked card panels to a Column
     */
    private void addClickedToColumn(int columnNum) {
        for (CardPanel panel : movingCards) {
            movableCards.remove(panel);
            movableCards.add(panel);
            columns.get(columnNum).add(panel);
            panel.setLocation(columnCoord.get(columnNum));
            incrementColumnCoord(columnNum);
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
     * Makes the next card in a column displayed
     */
    private void flipTopCardInColumn(int columnNum) {
        if (columns.get(returnColumn).size() > 0) {
            CardPanel panel = columns.get(returnColumn).getLast();
            if (!panel.getCard().isFaceUp()) {
                makeMovableCard(panel);
                updateDisplayedZOrder();
            }
        }
    }

    /**
     * Adds clicked to foundation
     */
    private void addClickedToFoundation(int foundationNum) {
        movableCards.remove(clickedCard);
        movableCards.add(clickedCard);
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
                        (int) columnCoord.get(columnNum).getY() + cardVertSpace));
    }

    private void decrementColumnCoord(int columnNum) {
        columnCoord.set(columnNum,
                new Point((int) columnCoord.get(columnNum).getX(),
                        (int) columnCoord.get(columnNum).getY() - cardVertSpace));
    }

    /**
     * Overrides the set size so that the cards size and spacing can be changed
     * to fit the screen
     */
    @Override
    public void setSize(Dimension dimension) {
        super.setSize(dimension);
        this.cardWidth = (int) (dimension.getHeight() / 6.66);
        this.cardVertSpace = (int) (dimension.getHeight() / 33.3);
        this.columnStartY = (int) (dimension.getHeight() / 3.33);
    }
}
