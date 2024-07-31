package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;
import java.util.Locale;
import javax.swing.Timer;
import javax.swing.JTextField;

import model.Client;
import model.Game;
import model.Host;
import model.Network;
import view.CustomDialog;
import view.GameUi;

public class Controller {
    private GameUi gameUi;
    private Game game;
    private Host host;
    private Client client;
    private Network network;
    private CustomDialog serverDialog;
    private CustomDialog clientDialog;
    private boolean isHost;
    private boolean isPvpMode = false; // Add a flag to indicate PVP mode

    public Controller(GameUi gameUi) {
        this.gameUi = gameUi;
        this.game = new Game(gameUi);
        initializeController();
    }

    private void initializeController() {
        gameUi.getPveItem().addActionListener(e -> {
            isPvpMode = false; // Set PVE mode
            gameUi.showPveDialog();
            game.enableShipPlacement();
            gameUi.showMessage("Game mode: PVE");
        });

        gameUi.getHostItem().addActionListener(e -> {
            isPvpMode = true; // Set PVP mode
            serverDialog = new CustomDialog(gameUi, true);
            serverDialog.setVisible(true);

            if (serverDialog.getAddress() != null) {
                try {
                    host = new Host();
                    host.start(serverDialog.getPort());
                    gameUi.showMessage("Waiting for a connection...");
                    new Thread(() -> {
                        try {
                            Socket socket = host.accept();
                            network = new Network(socket, gameUi, game, this, true);
                            gameUi.setNetwork(network);
                            isHost = true;
                            gameUi.setPlayerName(serverDialog.getPlayerName());
                            gameUi.showMessage("Connected as host.");
                            network.sendMessage("PLACE_SHIPS");
                        } catch (IOException ex) {
                            gameUi.showMessage("Failed to accept connection: " + ex.getMessage());
                        }
                    }).start();
                } catch (IOException ex) {
                    gameUi.showMessage("Failed to start host: " + ex.getMessage());
                }
            }
        });

        gameUi.getConnectItem().addActionListener(e -> {
            isPvpMode = true; // Set PVP mode
            clientDialog = new CustomDialog(gameUi, false);
            clientDialog.setVisible(true);

            if (clientDialog.getAddress() != null) {
                try {
                    client = new Client();
                    client.connect(clientDialog.getAddress(), clientDialog.getPort());
                    network = new Network(client.getSocket(), gameUi, game, this, false);
                    gameUi.setNetwork(network);
                    isHost = false;
                    gameUi.setPlayerName(clientDialog.getPlayerName());
                    gameUi.showMessage("Connected to host.");
                    network.sendMessage("PLACE_SHIPS");
                } catch (IOException ex) {
                    gameUi.showMessage("Failed to connect to host: " + ex.getMessage());
                }
            }
        });

        gameUi.getRestartItem().addActionListener(e -> {
            game.resetGame();
            gameUi.resetUI();
            gameUi.showPveDialog();
        });

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

        gameUi.getRotateButton().addActionListener(e -> {
            game.toggleRotation();
            gameUi.showRotationMessage(game.isVertical());
        });

        gameUi.getStartButton().addActionListener(e -> {
            if (game.getCurrentShipName() != null) {
                gameUi.showPlaceAllShipsMessage();
            } else {
                if (isPvpMode) { // For PVP mode
                    if (network != null) {
                        network.sendMessage("READY::" + gameUi.getPlayerName());
                    }
                    if (isHost) {
                        network.setHostReady(true);
                        gameUi.showMessage("Host is ready!");
                    } else {
                        network.setClientReady(true);
                        gameUi.showMessage("Client is ready!");
                    }
                    network.checkBothReady();
                } else { // For PVE mode
                    game.placeComputerShips();
                    gameUi.showComputerBoard();
                    gameUi.getStartButton().setEnabled(false); // Disable the Start Game button
                    game.enableGamePlay();
                }
            }
        });

        gameUi.getEndTurnButton().addActionListener(e -> {
            if (!game.isPlayerTurn() || !game.hasPlayerMadeMove()) return;
            game.setPlayerTurn(false);
            game.setHasPlayerMadeMove(false);
            gameUi.showPlayerBoard();

            if (game.checkVictory(game.getPlayerHits())) {
                gameUi.showVictoryMessage();
                game.disableGamePlay();
            } else {
                Timer timer = new Timer(1500, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        game.computerTurn();
                    }
                });
                timer.setRepeats(false);
                timer.start();
            }
        });

        gameUi.getChatInput().addActionListener(e -> {
            JTextField chatInput = gameUi.getChatInput();
            String message = chatInput.getText();
            if (!message.trim().isEmpty()) {
                gameUi.showChatMessage("Player: " + message);
                if (network != null) {
                    network.sendMessage("CHAT:" + message);
                }
                chatInput.setText("");
            }
        });

        gameUi.getDisconnectItem().addActionListener(e -> {
            try {
                if (network != null) {
                    network.close();
                }
                if (host != null) {
                    host.stop();
                }
                if (client != null) {
                    client.disconnect();
                }
                gameUi.showMessage("Disconnected.");
            } catch (IOException ex) {
                gameUi.showMessage("Failed to disconnect: " + ex.getMessage());
            }
        });
    }
}
