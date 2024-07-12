package view;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

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
	        
	        
	     
	     this.add(panel);
         pack();
	}
    
    
	public void showMenu() {
        setVisible(true);
    }

}
