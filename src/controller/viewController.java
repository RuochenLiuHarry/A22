package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import view.GameUi;

public class viewController{
	private GameUi gameUi;
	
	public viewController(GameUi gameUi) {
		this.gameUi = gameUi;
		initializeController();
	}
	
	private void initializeController() {
        // PVE
//        gameUi.getPveItem().addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                gameUi.showPveDialog();
//            }
//        });

        // PVP
        gameUi.getPvpItem().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle PVP logic here
            }
        });

        // Restart
        gameUi.getRestartItem().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle restart logic here
            }
        });

        // Exit
        gameUi.getExitItem().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }
	
}
