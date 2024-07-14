package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
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
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

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
    private JLabel yourShips2;
    private JLabel yourShips3;
    private JLabel yourShips4;
    private JLabel yourShips5;

    // Images for ships
    private ImageIcon bowEast;
    private ImageIcon bowNorth;
    private ImageIcon bowSouth;
    private ImageIcon bowWest;
    private ImageIcon midHullHoriz;
    private ImageIcon midHullVert;
    private ImageIcon hitIcon;
    private ImageIcon missIcon;

    private final String[] colLabel = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
    private ResourceBundle bundle;

    public GameUi() {
        initializeComponents();
        loadImages();
        changeLocale(Locale.ENGLISH);
        initializeUI();
    }

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

        // Add action listeners for language menu items

    }

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

    public void showMenu() {
        setVisible(true);
        JOptionPane.showMessageDialog(this, bundle.getString("showMenuMessage"));
    }

    public void showPveDialog() {
        JOptionPane.showMessageDialog(this, bundle.getString("showPveDialogMessage"));
    }

    public void showYourTurn() {
        JOptionPane.showMessageDialog(this, bundle.getString("showYourTurnMessage"));
    }

    public void placeShipPart(int x, int y, ImageIcon icon) {
        gridButtons[x][y].setIcon(icon);
    }

    public void markPlayerBoard(int x, int y, ImageIcon icon) {
        gridButtons[x][y].setIcon(icon);
    }

    public void markComputerBoard(int x, int y, ImageIcon icon) {
        computerGridButtons[x][y].setIcon(icon);
    }

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

    public void showPlayerBoard() {
        panel.remove(computerGridPanel);
        panel.add(gridPanel, BorderLayout.CENTER);
        panel.revalidate();
        panel.repaint();
    }

    public JMenuItem getPveItem() {
        return pveItem;
    }

    public JMenuItem getPvpItem() {
        return pvpItem;
    }

    public JMenuItem getRestartItem() {
        return restartItem;
    }

    public JMenuItem getExitItem() {
        return exitItem;
    }

    public JMenuItem getEnglishItem() {
        return englishItem;
    }

    public JMenuItem getChineseItem() {
        return chineseItem;
    }

    public JButton getRotateButton() {
        return rotateButton;
    }

    public JButton getStartButton() {
        return startButton;
    }

    public JButton getEndTurnButton() {
        return endTurnButton;
    }

    public JButton getQuitButton() {
        return quitButton;
    }

    public ImageIcon getBowEast() {
        return bowEast;
    }

    public ImageIcon getBowNorth() {
        return bowNorth;
    }

    public ImageIcon getBowSouth() {
        return bowSouth;
    }

    public ImageIcon getBowWest() {
        return bowWest;
    }

    public ImageIcon getMidHullHoriz() {
        return midHullHoriz;
    }

    public ImageIcon getMidHullVert() {
        return midHullVert;
    }

    public ImageIcon getHitIcon() {
        return hitIcon;
    }

    public ImageIcon getMissIcon() {
        return missIcon;
    }

    public JButton[][] getGridButtons() {
        return gridButtons;
    }

    public JButton[][] getComputerGridButtons() {
        return computerGridButtons;
    }

    public void showRotationMessage(boolean isVertical) {
        JOptionPane.showMessageDialog(this, bundle.getString("showRotationMessage") + (isVertical ? bundle.getString("vertical") : bundle.getString("horizontal")));
    }

    public void showPlaceAllShipsMessage() {
        JOptionPane.showMessageDialog(this, bundle.getString("showPlaceAllShipsMessage"));
    }

    public void showAllShipsPlacedMessage() {
        JOptionPane.showMessageDialog(this, bundle.getString("showAllShipsPlacedMessage"));
    }

    public void showCannotPlaceShipMessage() {
        JOptionPane.showMessageDialog(this, bundle.getString("showCannotPlaceShipMessage"));
    }

    public void showVictoryMessage() {
        JOptionPane.showMessageDialog(this, bundle.getString("showVictoryMessage"));
    }

    public void showLossMessage() {
        JOptionPane.showMessageDialog(this, bundle.getString("showLossMessage"));
    }

    public void showCannotGoTwiceMessage() {
        JOptionPane.showMessageDialog(this, bundle.getString("showCannotGoTwiceMessage"));
    }

    public void exitGame() {
        System.exit(0);
    }
}
