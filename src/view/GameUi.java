package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JWindow;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

/**
 * GameUi class representing the user interface for the Battleship game.
 */
public class GameUi extends JFrame {

    private JMenu gameMenu;

    private JMenu languageMenu;


    private JPanel panel;


    private JMenuItem pveItem;


    private JMenuItem pvpItem;

    private JMenuItem restartItem;


    private JMenuItem exitItem;

    private JMenuItem englishItem;

    private JMenuItem chineseItem;

    private JPanel gridPanel;

    private JButton[][] gridButtons;


    private JPanel computerGridPanel;

    private JButton[][] computerGridButtons;

    private JButton rotateButton;

    private JButton startButton;

    private JButton endTurnButton;

    private JButton quitButton;
    
    private JPanel leftPanel;

    private JPanel rightPanel;

    private JLabel gameRule;

    private JLabel chatPart;

    private JLabel yourShips1;

    /**
     * The label displaying the second ship's status.
     */
    private JLabel yourShips2;

    /**
     * The label displaying the third ship's status.
     */
    private JLabel yourShips3;

    /**
     * The label displaying the fourth ship's status.
     */
    private JLabel yourShips4;

    /**
     * The label displaying the fifth ship's status.
     */
    private JLabel yourShips5;

    // Images for ships
    /**
     * The image icon for the bow of a ship facing east.
     */
    private ImageIcon bowEast;

    /**
     * The image icon for the bow of a ship facing north.
     */
    private ImageIcon bowNorth;

    /**
     * The image icon for the bow of a ship facing south.
     */
    private ImageIcon bowSouth;

    /**
     * The image icon for the bow of a ship facing west.
     */
    private ImageIcon bowWest;

    /**
     * The image icon for the middle hull of a ship placed horizontally.
     */
    private ImageIcon midHullHoriz;

    /**
     * The image icon for the middle hull of a ship placed vertically.
     */
    private ImageIcon midHullVert;

    /**
     * The image icon representing a hit on the board.
     */
    private ImageIcon hitIcon;

    /**
     * The image icon representing a miss on the board.
     */
    private ImageIcon missIcon;

    /**
     * The column labels for the game board.
     */
    private final String[] colLabel = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};

    /**
     * The resource bundle for internationalization.
     */
    private ResourceBundle bundle;

    /**
     * Constructs a GameUi instance and initializes the UI components.
     */
    public GameUi() {
        showSplashScreen("logo.png", 5000); // 5000 millisecond (5 seconds)
        initializeComponents();
        loadImages();
        changeLocale(Locale.ENGLISH);
        initializeUI();
    }
    
    /**
     * Displays a splash screen with the specified image for a specified duration.
     *
     * @param imagePath the path to the image file to be displayed
     * @param duration the duration in milliseconds for which the splash screen is displayed
     */
    private void showSplashScreen(String imagePath, int duration) {
        JWindow splashScreen = new JWindow();
        ImageIcon imageIcon = new ImageIcon(imagePath);
        JLabel imageLabel = new JLabel(imageIcon);

        splashScreen.getContentPane().add(imageLabel, BorderLayout.CENTER);
        splashScreen.pack();

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = splashScreen.getSize().width;
        int height = splashScreen.getSize().height;
        int x = (screenSize.width - width) / 2;
        int y = (screenSize.height - height) / 2;
        splashScreen.setLocation(x, y);

        splashScreen.setVisible(true);

        try {
            Thread.sleep(duration);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        splashScreen.setVisible(false);
        splashScreen.dispose();
    }

    /**
     * Initializes the UI components.
     */
    private void initializeComponents() {
        gameMenu = new JMenu();
        pveItem = new JMenuItem();
        pvpItem = new JMenuItem();
        restartItem = new JMenuItem();
        exitItem = new JMenuItem();
        languageMenu = new JMenu();
        englishItem = new JMenuItem();
        chineseItem = new JMenuItem();
        panel = new JPanel();
        gridPanel = new JPanel();
        gridButtons = new JButton[10][10];
        computerGridButtons = new JButton[10][10];
        rotateButton = new JButton();
        startButton = new JButton();
        endTurnButton = new JButton();
        quitButton = new JButton();
        leftPanel = new JPanel();
        rightPanel = new JPanel();
        gameRule = new JLabel();
        chatPart = new JLabel();
        yourShips1 = new JLabel();
        yourShips2 = new JLabel();
        yourShips3 = new JLabel();
        yourShips4 = new JLabel();
        yourShips5 = new JLabel();
    }

    /**
     * Initializes the user interface.
     */
    private void initializeUI() {
        setTitle("Battleship");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set default locale and resource bundle
        changeLocale(Locale.ENGLISH);

        JMenuBar menuBar = new JMenuBar();
        this.setJMenuBar(menuBar);
        gameMenu = new JMenu();
        menuBar.add(gameMenu);

        pveItem = new JMenuItem();
        pvpItem = new JMenuItem();
        restartItem = new JMenuItem();
        exitItem = new JMenuItem();

        gameMenu.add(pveItem);
        gameMenu.add(pvpItem);
        gameMenu.add(restartItem);
        gameMenu.add(exitItem);

        languageMenu = new JMenu();
        menuBar.add(languageMenu);

        englishItem = new JMenuItem();
        chineseItem = new JMenuItem();

        languageMenu.add(englishItem);
        languageMenu.add(chineseItem);

        panel = new JPanel();
        panel.setBackground(new Color(51, 204, 255));
        panel.setLayout(new BorderLayout());
        ImageIcon logoIcon = new ImageIcon("logo.png");
        JLabel logoLabel = new JLabel(logoIcon);
        panel.add(logoLabel, BorderLayout.NORTH);

        gridPanel = new JPanel(new GridLayout(11, 11, 2, 2));
        gridPanel.setBackground(new Color(51, 204, 255));

        gridButtons = new JButton[10][10];
        computerGridButtons = new JButton[10][10];

        gridPanel.add(new JLabel("")); // Empty top-left corner
        for (String label : colLabel) {
            gridPanel.add(new JLabel(label, SwingConstants.CENTER));
        }

        for (int i = 0; i < 10; i++) {
            gridPanel.add(new JLabel(String.valueOf(i + 1), SwingConstants.CENTER));
            for (int j = 0; j < 10; j++) {
                gridButtons[i][j] = new JButton();
                gridButtons[i][j].setPreferredSize(new Dimension(40, 40));
                gridButtons[i][j].setBackground(new Color(51, 204, 255));
                gridButtons[i][j].setBorder(new LineBorder(Color.WHITE));
                gridPanel.add(gridButtons[i][j]);

                computerGridButtons[i][j] = new JButton();
                computerGridButtons[i][j].setPreferredSize(new Dimension(40, 40));
                computerGridButtons[i][j].setBackground(new Color(51, 204, 255));
                computerGridButtons[i][j].setBorder(new LineBorder(Color.WHITE));
            }
        }

        panel.add(gridPanel, BorderLayout.CENTER);

        // Left Panel
        leftPanel = new JPanel();
        leftPanel.setBackground(new Color(0, 51, 90));
        leftPanel.setPreferredSize(new Dimension(150, leftPanel.getPreferredSize().height));
        leftPanel.setLayout(new GridLayout(6, 1));
        gameRule = new JLabel();
        gameRule.setForeground(Color.WHITE);
        leftPanel.add(gameRule);

        yourShips1 = new JLabel();
        yourShips2 = new JLabel();
        yourShips3 = new JLabel();
        yourShips4 = new JLabel();
        yourShips5 = new JLabel();

        yourShips1.setForeground(Color.WHITE);
        yourShips2.setForeground(Color.WHITE);
        yourShips3.setForeground(Color.WHITE);
        yourShips4.setForeground(Color.WHITE);
        yourShips5.setForeground(Color.WHITE);

        leftPanel.add(yourShips1);
        leftPanel.add(yourShips2);
        leftPanel.add(yourShips3);
        leftPanel.add(yourShips4);
        leftPanel.add(yourShips5);

        panel.add(leftPanel, BorderLayout.WEST);

        // Right Panel
        rightPanel = new JPanel();
        rightPanel.setBackground(new Color(0, 51, 90));
        rightPanel.setPreferredSize(new Dimension(150, rightPanel.getPreferredSize().height));
        chatPart = new JLabel();
        chatPart.setForeground(Color.WHITE);
        rightPanel.add(chatPart);
        panel.add(rightPanel, BorderLayout.EAST);

        JPanel bottomPanel = new JPanel(new GridLayout(1, 4, 10, 0));
        bottomPanel.setBackground(new Color(0, 51, 102));

        rotateButton = new JButton();
        endTurnButton = new JButton();
        startButton = new JButton();
        quitButton = new JButton();

        bottomPanel.add(rotateButton);
        bottomPanel.add(startButton);
        bottomPanel.add(endTurnButton);
        bottomPanel.add(quitButton);

        panel.add(bottomPanel, BorderLayout.SOUTH);

        setResizable(false);
        this.add(panel);

        pack();

        // Set initial text from the resource bundle
        updateText();
    }
    
    /**
     * Resets the UI by clearing ship and hit/miss icons from the boards.
     */
    public void resetUI() {
        // Clear all ship and hit/miss icons from the player and computer boards
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                gridButtons[i][j].setIcon(null);
                computerGridButtons[i][j].setIcon(null);
            }
        }

        // Reset UI elements if necessary
        showPlayerBoard(); // Ensure the player's board is shown
        getStartButton().setEnabled(true); // Enable the Start Game button
    }

    /**
     * Loads images for the ships.
     */
    private void loadImages() {
        bowEast = new ImageIcon(new ImageIcon("bow_east.png").getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH));
        bowNorth = new ImageIcon(new ImageIcon("bow_north.png").getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH));
        bowSouth = new ImageIcon(new ImageIcon("bow_south.png").getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH));
        bowWest = new ImageIcon(new ImageIcon("bow_west.png").getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH));
        midHullHoriz = new ImageIcon(new ImageIcon("midhull_horiz.png").getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH));
        midHullVert = new ImageIcon(new ImageIcon("midhull_vert.png").getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH));
        hitIcon = new ImageIcon(new ImageIcon("hit.png").getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH));
        missIcon = new ImageIcon(new ImageIcon("miss.png").getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH));
    }

    /**
     * Changes the locale of the UI.
     * 
     * @param locale the new locale to be set
     */
    public void changeLocale(Locale locale) {
        try {
            Locale.setDefault(locale);
            bundle = ResourceBundle.getBundle("MessagesBundle", locale);
            System.out.println("Loaded resource bundle for locale: " + locale);
            updateText();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading resource bundle for locale: " + locale);
        }
    }

    /**
     * Updates the text of the UI components based on the current locale.
     */
    private void updateText() {
        try {
            gameMenu.setText(bundle.getString("gameMenu"));
            pveItem.setText(bundle.getString("pveItem"));
            pvpItem.setText(bundle.getString("pvpItem"));
            restartItem.setText(bundle.getString("restartItem"));
            exitItem.setText(bundle.getString("exitItem"));
            languageMenu.setText(bundle.getString("languageMenu"));
            englishItem.setText(bundle.getString("englishItem"));
            chineseItem.setText(bundle.getString("chineseItem"));
            rotateButton.setText(bundle.getString("rotateButton"));
            startButton.setText(bundle.getString("startButton"));
            endTurnButton.setText(bundle.getString("endTurnButton"));
            quitButton.setText(bundle.getString("quitButton"));
            gameRule.setText(bundle.getString("gameRule"));
            chatPart.setText(bundle.getString("chatPart"));
            yourShips1.setText(bundle.getString("yourShips1"));
            yourShips2.setText(bundle.getString("yourShips2"));
            yourShips3.setText(bundle.getString("yourShips3"));
            yourShips4.setText(bundle.getString("yourShips4"));
            yourShips5.setText(bundle.getString("yourShips5"));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error updating text for locale: " + Locale.getDefault());
        }
    }

    /**
     * Displays the main menu.
     */
    public void showMenu() {
        setVisible(true);
        JOptionPane.showMessageDialog(this, bundle.getString("showMenuMessage"));
    }

    /**
     * Displays the PVE dialog.
     */
    public void showPveDialog() {
        JOptionPane.showMessageDialog(this, bundle.getString("showPveDialogMessage"));
    }

    /**
     * Displays a message indicating it is the player's turn.
     */
    public void showYourTurn() {
        JOptionPane.showMessageDialog(this, bundle.getString("showYourTurnMessage"));
    }

    /**
     * Places a ship part at the specified coordinates.
     * 
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @param icon the icon representing the ship part
     */
    public void placeShipPart(int x, int y, ImageIcon icon) {
        gridButtons[x][y].setIcon(icon);
    }

    /**
     * Marks the player's board at the specified coordinates.
     * 
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @param icon the icon representing the hit or miss
     */
    public void markPlayerBoard(int x, int y, ImageIcon icon) {
        gridButtons[x][y].setIcon(icon);
    }

    /**
     * Marks the computer's board at the specified coordinates.
     * 
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @param icon the icon representing the hit or miss
     */
    public void markComputerBoard(int x, int y, ImageIcon icon) {
        computerGridButtons[x][y].setIcon(icon);
    }

    /**
     * Displays the computer's board.
     */
    public void showComputerBoard() {
        panel.remove(gridPanel);
        computerGridPanel = new JPanel(new GridLayout(11, 11, 2, 2));
        computerGridPanel.setBackground(new Color(51, 204, 255));

        computerGridPanel.add(new JLabel("")); // Empty top-left corner
        for (String label : colLabel) {
            computerGridPanel.add(new JLabel(label, SwingConstants.CENTER));
        }

        for (int i = 0; i < 10; i++) {
            computerGridPanel.add(new JLabel(String.valueOf(i + 1), SwingConstants.CENTER));
            for (int j = 0; j < 10; j++) {
                computerGridPanel.add(computerGridButtons[i][j]);
            }
        }

        panel.add(computerGridPanel, BorderLayout.CENTER);
        panel.revalidate();
        panel.repaint();
    }

    /**
     * Displays the player's board.
     */
    public void showPlayerBoard() {
        panel.remove(computerGridPanel);
        panel.add(gridPanel, BorderLayout.CENTER);
        panel.revalidate();
        panel.repaint();
    }

    /**
     * Gets the PVE menu item.
     * 
     * @return the PVE menu item
     */
    public JMenuItem getPveItem() {
        return pveItem;
    }

    /**
     * Gets the PVP menu item.
     * 
     * @return the PVP menu item
     */
    public JMenuItem getPvpItem() {
        return pvpItem;
    }

    /**
     * Gets the Restart menu item.
     * 
     * @return the Restart menu item
     */
    public JMenuItem getRestartItem() {
        return restartItem;
    }

    /**
     * Gets the Exit menu item.
     * 
     * @return the Exit menu item
     */
    public JMenuItem getExitItem() {
        return exitItem;
    }

    /**
     * Gets the English language menu item.
     * 
     * @return the English language menu item
     */
    public JMenuItem getEnglishItem() {
        return englishItem;
    }

    /**
     * Gets the Chinese language menu item.
     * 
     * @return the Chinese language menu item
     */
    public JMenuItem getChineseItem() {
        return chineseItem;
    }

    /**
     * Gets the Rotate button.
     * 
     * @return the Rotate button
     */
    public JButton getRotateButton() {
        return rotateButton;
    }

    /**
     * Gets the Start button.
     * 
     * @return the Start button
     */
    public JButton getStartButton() {
        return startButton;
    }

    /**
     * Gets the End Turn button.
     * 
     * @return the End Turn button
     */
    public JButton getEndTurnButton() {
        return endTurnButton;
    }

    /**
     * Gets the Quit button.
     * 
     * @return the Quit button
     */
    public JButton getQuitButton() {
        return quitButton;
    }

    /**
     * Gets the bow east icon.
     * 
     * @return the bow east icon
     */
    public ImageIcon getBowEast() {
        return bowEast;
    }

    /**
     * Gets the bow north icon.
     * 
     * @return the bow north icon
     */
    public ImageIcon getBowNorth() {
        return bowNorth;
    }

    /**
     * Gets the bow south icon.
     * 
     * @return the bow south icon
     */
    public ImageIcon getBowSouth() {
        return bowSouth;
    }

    /**
     * Gets the bow west icon.
     * 
     * @return the bow west icon
     */
    public ImageIcon getBowWest() {
        return bowWest;
    }

    /**
     * Gets the mid hull horizontal icon.
     * 
     * @return the mid hull horizontal icon
     */
    public ImageIcon getMidHullHoriz() {
        return midHullHoriz;
    }

    /**
     * Gets the mid hull vertical icon.
     * 
     * @return the mid hull vertical icon
     */
    public ImageIcon getMidHullVert() {
        return midHullVert;
    }

    /**
     * Gets the hit icon.
     * 
     * @return the hit icon
     */
    public ImageIcon getHitIcon() {
        return hitIcon;
    }

    /**
     * Gets the miss icon.
     * 
     * @return the miss icon
     */
    public ImageIcon getMissIcon() {
        return missIcon;
    }

    /**
     * Gets the grid buttons for the player's board.
     * 
     * @return the grid buttons for the player's board
     */
    public JButton[][] getGridButtons() {
        return gridButtons;
    }

    /**
     * Gets the grid buttons for the computer's board.
     * 
     * @return the grid buttons for the computer's board
     */
    public JButton[][] getComputerGridButtons() {
        return computerGridButtons;
    }

    /**
     * Displays a message indicating the current rotation of the ship.
     * 
     * @param isVertical true if the rotation is vertical, false otherwise
     */
    public void showRotationMessage(boolean isVertical) {
        JOptionPane.showMessageDialog(this, bundle.getString("showRotationMessage") + (isVertical ? bundle.getString("vertical") : bundle.getString("horizontal")));
    }

    /**
     * Displays a message indicating all ships need to be placed.
     */
    public void showPlaceAllShipsMessage() {
        JOptionPane.showMessageDialog(this, bundle.getString("showPlaceAllShipsMessage"));
    }

    /**
     * Displays a message indicating all ships have been placed.
     */
    public void showAllShipsPlacedMessage() {
        JOptionPane.showMessageDialog(this, bundle.getString("showAllShipsPlacedMessage"));
    }

    /**
     * Displays a message indicating a ship cannot be placed.
     */
    public void showCannotPlaceShipMessage() {
        JOptionPane.showMessageDialog(this, bundle.getString("showCannotPlaceShipMessage"));
    }

    /**
     * Displays a victory message.
     */
    public void showVictoryMessage() {
        JOptionPane.showMessageDialog(this, bundle.getString("showVictoryMessage"));
    }

    /**
     * Displays a loss message.
     */
    public void showLossMessage() {
        JOptionPane.showMessageDialog(this, bundle.getString("showLossMessage"));
    }

    /**
     * Displays a message indicating a move cannot be made twice.
     */
    public void showCannotGoTwiceMessage() {
        JOptionPane.showMessageDialog(this, bundle.getString("showCannotGoTwiceMessage"));
    }

    /**
     * Exits the game.
     */
    public void exitGame() {
        System.exit(0);
    }
}
