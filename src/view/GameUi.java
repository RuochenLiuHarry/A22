package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
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
    private JButton[][] computerGridButtons;
    private JButton rotateButton;
    private JButton startButton;
    private JButton endTurnButton;
    private JButton quitButton;
    private JPanel leftPanel;
    private JPanel rightPanel;
    private JLabel gameRule;
    private JLabel chatPart;
    public GameUi() {
    	initiallizeUI();
    }

	private void initiallizeUI() {
		
		setTitle("Battleship");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
		JMenuBar menuBar = new JMenuBar();
		this.setJMenuBar(menuBar);
		gameMenu = new JMenu("Game");
		menuBar.add(gameMenu);
		
		
		 pveItem = new JMenuItem("PVE");
	     pvpItem = new JMenuItem("PVP");
	     restartItem = new JMenuItem("restart");
	     exitItem = new JMenuItem("exit");
	        
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
	     panel.setBackground(new Color(51,204,255));
	     panel.setLayout(new BorderLayout());
	     ImageIcon logoIcon = new ImageIcon("logo.png");
	     JLabel logoLabel = new JLabel(logoIcon);
	     panel.add(logoLabel, BorderLayout.NORTH);
	        
	     gridPanel = new JPanel(new GridLayout(11, 11, 2, 2));
	     gridPanel.setBackground(new Color(51, 204, 255));   
	     
	     gridButtons = new JButton[10][10];
	     computerGridButtons = new JButton[10][10];
	        String[] colLabel = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};

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
	        
	        leftPanel = new JPanel();
	        leftPanel.setBackground(new Color(0, 51, 90));
	        gameRule = new JLabel("Getting you up to speed!");
	        gameRule.setForeground(Color.WHITE);
	        leftPanel.add(gameRule);
	        panel.add(leftPanel, BorderLayout.WEST);
	        
	        
	        rightPanel = new JPanel();
	        rightPanel.setBackground(new Color(0, 51, 90));
	        chatPart = new JLabel("talk to you competitor! ");
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
    
    
	public void showMenu() {
        setVisible(true);
    }

}
