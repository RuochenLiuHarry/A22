package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
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

    // Column labels for grid
    private final String[] colLabel = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};

    public GameUi() {
        initializeUI();
        loadImages();
    }

    private void initializeUI() {
        setTitle("Battleship");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JMenuBar menuBar = new JMenuBar();
        this.setJMenuBar(menuBar);
        gameMenu = new JMenu("Game");
        menuBar.add(gameMenu);

        pveItem = new JMenuItem("PVE");
        pvpItem = new JMenuItem("PVP");
        restartItem = new JMenuItem("Restart");
        exitItem = new JMenuItem("Exit");

        gameMenu.add(pveItem);
        gameMenu.add(pvpItem);
        gameMenu.add(restartItem);
        gameMenu.add(exitItem);

        languageMenu = new JMenu("Language");
        menuBar.add(languageMenu);

        englishItem = new JMenuItem("English");
        chineseItem = new JMenuItem("Chinese");

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
        gameRule = new JLabel("Your ships");
        gameRule.setForeground(Color.WHITE);
        leftPanel.add(gameRule);

        yourShips1 = new JLabel("Carrier: 5/5");
        yourShips2 = new JLabel("Cruiser: 4/4");
        yourShips3 = new JLabel("Destroyer: 3/3");
        yourShips4 = new JLabel("Missile Frigate: 3/3");
        yourShips5 = new JLabel("Submarine: 2/2");

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
        chatPart = new JLabel("Talk to your competitor!");
        chatPart.setForeground(Color.WHITE);
        rightPanel.add(chatPart);
        panel.add(rightPanel, BorderLayout.EAST);

        JPanel bottomPanel = new JPanel(new GridLayout(1, 4, 10, 0));
        bottomPanel.setBackground(new Color(0, 51, 102));

        rotateButton = new JButton("Rotate Ship");
        endTurnButton = new JButton("End Turn");
        startButton = new JButton("Start Game");
        quitButton = new JButton("Quit");

        bottomPanel.add(rotateButton);
        bottomPanel.add(startButton);
        bottomPanel.add(endTurnButton);
        bottomPanel.add(quitButton);

        panel.add(bottomPanel, BorderLayout.SOUTH);

        setResizable(false);
        this.add(panel);

        pack();
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

    public void showMenu() {
        setVisible(true);
        JOptionPane.showMessageDialog(this, "To start the game, click on the menu bar Game tab.");
    }

    public void showPveDialog() {
        JOptionPane.showMessageDialog(this, "Please place your 5 ships on the board.");
    }

    // Method to place a ship part on the board
    public void placeShipPart(int x, int y, ImageIcon icon) {
        gridButtons[x][y].setIcon(icon);
    }

    // Method to mark hit or miss on the player board
    public void markPlayerBoard(int x, int y, ImageIcon icon) {
        gridButtons[x][y].setIcon(icon);
    }

    // Method to mark hit or miss on the computer board
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
        gridPanel = new JPanel(new GridLayout(11, 11, 2, 2));
        gridPanel.setBackground(new Color(51, 204, 255));

        gridPanel.add(new JLabel("")); // Empty top-left corner
        for (String label : colLabel) {
            gridPanel.add(new JLabel(label, SwingConstants.CENTER));
        }

        for (int i = 0; i < 10; i++) {
            gridPanel.add(new JLabel(String.valueOf(i + 1), SwingConstants.CENTER));
            for (int j = 0; j < 10; j++) {
                gridPanel.add(gridButtons[i][j]);
            }
        }

        panel.add(gridPanel, BorderLayout.CENTER);
        panel.revalidate();
        panel.repaint();
    }

    // Getters for menu items and buttons
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

    // Getters for ship images
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

    // Getter for grid buttons
    public JButton[][] getGridButtons() {
        return gridButtons;
    }

    public JButton[][] getComputerGridButtons() {
        return computerGridButtons;
    }
}
