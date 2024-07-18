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
            addHostDialogListeners(hostDialog);
        });

        // Connect Game
        gameUi.getConnectItem().addActionListener(e -> {
            JDialog connectDialog = new JDialog(gameUi, "Connect to Game", true);
            gameUi.showConnectDialog(connectDialog);
            addConnectDialogListeners(connectDialog);
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

    private void addHostDialogListeners(JDialog hostDialog) {
        JButton hostButton = (JButton) hostDialog.getContentPane().getComponent(6);
        JButton cancelButton = (JButton) hostDialog.getContentPane().getComponent(7);
        JTextField nameField = (JTextField) hostDialog.getContentPane().getComponent(1);
        JComboBox<Integer> portBox = (JComboBox<Integer>) hostDialog.getContentPane().getComponent(3);
        JLabel statusLabel = (JLabel) hostDialog.getContentPane().getComponent(5);

        hostButton.addActionListener(e -> {
            player1Name = nameField.getText();
            int port = (int) portBox.getSelectedItem();
            try {
                Host host = new Host(port);
                host.startServer();
                network = new Network(host.getClientSocket());
                gameUi.showMessage("Player 1 (Host): " + player1Name);
                statusLabel.setText("Hosting on port " + port);
                hostDialog.dispose();
            } catch (IOException ex) {
                statusLabel.setText("Failed to host on port " + port);
                ex.printStackTrace();
            }
        });

        cancelButton.addActionListener(e -> hostDialog.dispose());
    }

    private void addConnectDialogListeners(JDialog connectDialog) {
        JButton connectButton = (JButton) connectDialog.getContentPane().getComponent(8);
        JButton cancelButton = (JButton) connectDialog.getContentPane().getComponent(9);
        JTextField nameField = (JTextField) connectDialog.getContentPane().getComponent(1);
        JTextField addressField = (JTextField) connectDialog.getContentPane().getComponent(3);
        JComboBox<Integer> portBox = (JComboBox<Integer>) connectDialog.getContentPane().getComponent(5);
        JLabel statusLabel = (JLabel) connectDialog.getContentPane().getComponent(7);

        connectButton.addActionListener(e -> {
            player2Name = nameField.getText();
            String address = addressField.getText();
            int port = (int) portBox.getSelectedItem();
            try {
                Client client = new Client(address, port);
                client.connectToServer();
                network = new Network(client.getSocket());
                gameUi.showMessage("Player 2: " + player2Name);
                statusLabel.setText("Connected to " + address + ":" + port);
                connectDialog.dispose();
            } catch (IOException ex) {
                statusLabel.setText("Failed to connect to " + address + ":" + port);
                ex.printStackTrace();
            }
        });

        cancelButton.addActionListener(e -> connectDialog.dispose());
    }
}
