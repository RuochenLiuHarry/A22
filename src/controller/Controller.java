package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Locale;
import javax.swing.*;
import model.*;
import view.GameUi;

public class Controller {
    private GameUi gameUi;
    private Game game;
    private String player1Name;
    private String player2Name;
    private Network network;

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
            // PVP logic here
        });

        // Host Game
        gameUi.getHostItem().addActionListener(e -> {
            JDialog hostDialog = new JDialog(gameUi, "Host Game", true);
            gameUi.showHostDialog(hostDialog);
        });

        // Connect Game
        gameUi.getConnectItem().addActionListener(e -> {
            JDialog connectDialog = new JDialog(gameUi, "Connect to Game", true);
            gameUi.showConnectDialog(connectDialog);
        });

        // Disconnect Game
        gameUi.getDisconnectItem().addActionListener(e -> {
            gameUi.showMessage("Disconnected");
            if (network != null) {
                try {
                    network.closeConnection();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        // Restart
        gameUi.getRestartItem().addActionListener(e -> {
            game.resetGame();
            gameUi.resetUI();
            gameUi.showPveDialog();
        });

        // Exit
        gameUi.getExitItem().addActionListener(e -> {
            gameUi.exitGame();
        });

        gameUi.getQuitButton().addActionListener(e -> {
            gameUi.exitGame();
        });

        gameUi.getEnglishItem().addActionListener(e -> {
            gameUi.changeLocale(Locale.ENGLISH);
        });

        gameUi.getChineseItem().addActionListener(e -> {
            gameUi.changeLocale(Locale.SIMPLIFIED_CHINESE);
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
                if (game.isPlayerTurn()) {
                    gameUi.showMessage("Player 1 is ready!");
                    game.setPlayerTurn(false);
                    gameUi.getStartButton().setEnabled(false);
                    gameUi.getEndTurnButton().setEnabled(true);
                    gameUi.getRotateButton().setEnabled(false);
                    gameUi.getEndTurnButton().setText("End Turn (Player 2)");
                    gameUi.showMessage("Player 2, place your ships.");
                    game.enableShipPlacement();
                } else {
                    gameUi.showMessage("Player 2 is ready! Player 1's turn.");
                    game.setPlayerTurn(true);
                    gameUi.getStartButton().setEnabled(false);
                    gameUi.getEndTurnButton().setEnabled(true);
                    gameUi.getRotateButton().setEnabled(false);
                    game.enableGamePlay();
                }
            }
        });

        // End Turn
        gameUi.getEndTurnButton().addActionListener(e -> {
            if (game.isPlayerTurn() && game.hasPlayerMadeMove()) {
                game.setPlayerTurn(false);
                game.setHasPlayerMadeMove(false);
                gameUi.getEndTurnButton().setText("End Turn (Player 2)");
                gameUi.showPlayerBoard();
                gameUi.showMessage("Player 2's turn.");
            } else if (!game.isPlayerTurn() && game.hasPlayerMadeMove()) {
                game.setPlayerTurn(true);
                game.setHasPlayerMadeMove(false);
                gameUi.getEndTurnButton().setText("End Turn (Player 1)");
                gameUi.showComputerBoard();
                gameUi.showMessage("Player 1's turn.");
            }
        });
    }
}
