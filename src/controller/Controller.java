package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;
import model.Game;
import view.GameUi;

public class Controller {
    private GameUi gameUi;
    private Game game;

    public Controller(GameUi gameUi) {
        this.gameUi = gameUi;
        this.game = new Game(gameUi);
        initializeController();
    }

    private void initializeController() {
        // PVE
        gameUi.getPveItem().addActionListener(e -> {
            gameUi.showPveDialog();
            game.enableShipPlacement();
        });

        // PVP
        gameUi.getPvpItem().addActionListener(e -> {
            // Handle PVP logic here
        });

        // Restart
        gameUi.getRestartItem().addActionListener(e -> {
            // Handle restart logic here
        });

        // Exit
        gameUi.getExitItem().addActionListener(e -> {
            System.exit(0);
        });

        // Rotate Ship
        gameUi.getRotateButton().addActionListener(e -> {
            game.toggleRotation();
            gameUi.showRotationMessage(game.isVertical());
        });

        // Start Game
        gameUi.getStartButton().addActionListener(e -> {
            if (game.getCurrentShipName() != null) {
                gameUi.showPlaceAllShipsMessage();
            } else {
                game.placeComputerShips();
                gameUi.showComputerBoard();
                gameUi.getStartButton().setEnabled(false); // Disable the Start Game button
                game.enableGamePlay();
            }
        });

        // End Turn
        gameUi.getEndTurnButton().addActionListener(e -> {
            if (!game.isPlayerTurn() || !game.hasPlayerMadeMove()) return;
            game.setPlayerTurn(false);
            game.setHasPlayerMadeMove(false);
            gameUi.showPlayerBoard();

            // Use Timer for delay instead of Thread.sleep
            Timer timer = new Timer(1500, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    game.computerTurn();
                }
            });
            timer.setRepeats(false);
            timer.start();
        });
    }
}
