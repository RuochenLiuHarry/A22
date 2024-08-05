package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;
import java.util.Locale;
import java.util.ResourceBundle;
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
    private boolean isPvpMode = false;
    private ResourceBundle bundle;

    public Controller(GameUi gameUi) {
        this.gameUi = gameUi;
        this.game = new Game(gameUi);
        this.bundle = ResourceBundle.getBundle("MessagesBundle", Locale.getDefault());
        initializeController();
    }

    private void initializeController() {
        gameUi.getPveItem().addActionListener(e -> {
            isPvpMode = false;
            gameUi.showPveDialog();
            game.enableShipPlacement();
            gameUi.showMessage(bundle.getString("gameModePVE"));
        });

        gameUi.getHostItem().addActionListener(e -> {
            isPvpMode = true;
            serverDialog = new CustomDialog(gameUi, true);
            serverDialog.setVisible(true);

            if (serverDialog.getAddress() != null) {
                try {
                    host = new Host();
                    host.start(serverDialog.getPort());
                    gameUi.showMessage(bundle.getString("waitingForConnection"));
                    new Thread(() -> {
                        try {
                            Socket socket = host.accept();
                            network = new Network(socket, gameUi, game, this, true);
                            gameUi.setNetwork(network);
                            game.setNetwork(network);
                            isHost = true;
                            gameUi.setPlayerName(serverDialog.getPlayerName());
                            gameUi.showMessage(bundle.getString("connectedAsHost"));
                            network.sendMessage("PLACE_SHIPS");
                        } catch (IOException ex) {
                            gameUi.showMessage(bundle.getString("failedToAcceptConnection") + ex.getMessage());
                        }
                    }).start();
                } catch (IOException ex) {
                    gameUi.showMessage(bundle.getString("failedToStartHost") + ex.getMessage());
                }
            }
        });

        gameUi.getConnectItem().addActionListener(e -> {
            isPvpMode = true;
            clientDialog = new CustomDialog(gameUi, false);
            clientDialog.setVisible(true);

            if (clientDialog.getAddress() != null) {
                try {
                    client = new Client();
                    client.connect(clientDialog.getAddress(), clientDialog.getPort());
                    network = new Network(client.getSocket(), gameUi, game, this, false);
                    gameUi.setNetwork(network);
                    game.setNetwork(network);
                    isHost = false;
                    gameUi.setPlayerName(clientDialog.getPlayerName());
                    gameUi.showMessage(bundle.getString("connectedToHost"));
                    network.sendMessage("PLACE_SHIPS");
                } catch (IOException ex) {
                    gameUi.showMessage(bundle.getString("failedToConnectToHost") + ex.getMessage());
                }
            }
        });

        gameUi.getRestartItem().addActionListener(e -> {
            if(isPvpMode) {
                gameUi.getNetwork().sendMessage("RESTART");
                game.resetGame();
                gameUi.resetUI();
                gameUi.showPveDialog();
                gameUi.getNetwork().setHostReady(false);
                gameUi.getNetwork().setClientReady(false);
            } else {
                game.resetGame();
                gameUi.resetUI();
                gameUi.showPveDialog();
            }
        });

        gameUi.getExitItem().addActionListener(e -> {
            gameUi.exitGame();
        });

        gameUi.getQuitButton().addActionListener(e -> {
            gameUi.exitGame();
        });

        gameUi.getEnglishItem().addActionListener(e -> {
            gameUi.changeLocale(Locale.ENGLISH);
            this.bundle = ResourceBundle.getBundle("MessagesBundle", Locale.ENGLISH);
        });

        gameUi.getChineseItem().addActionListener(e -> {
            gameUi.changeLocale(Locale.SIMPLIFIED_CHINESE);
            this.bundle = ResourceBundle.getBundle("MessagesBundle", Locale.SIMPLIFIED_CHINESE);
        });

        gameUi.getRotateButton().addActionListener(e -> {
            game.toggleRotation();
            gameUi.showRotationMessage(game.isVertical());
        });

        gameUi.getStartButton().addActionListener(e -> {
            if (game.getCurrentShipName() != null) {
                gameUi.showPlaceAllShipsMessage();
            } else {
                if (isPvpMode) {
                    if (network != null) {
                        network.sendMessage("READY::" + gameUi.getPlayerName());
                    }
                    if (isHost) {
                        network.setHostReady(true);
                        gameUi.showMessage(bundle.getString("hostIsReady"));
                        gameUi.enableStartButton(false);
                    } else {
                        network.setClientReady(true);
                        gameUi.showMessage(bundle.getString("clientIsReady"));
                        gameUi.enableStartButton(false);
                    }
                    network.checkBothReady();
                } else {
                    game.placeComputerShips();
                    gameUi.showComputerBoard();
                    gameUi.getStartButton().setEnabled(false);
                    game.enableGamePlay();
                }
            }
        });

        gameUi.getEndTurnButton().addActionListener(e -> {
            if (!game.isPlayerTurn() || !game.hasPlayerMadeMove()) return;
            game.setPlayerTurn(false);
            game.setHasPlayerMadeMove(false);
            if (isPvpMode && network != null) {
                gameUi.showPlayerBoard();
                network.sendMessage("END_TURN");
                gameUi.showMessage(bundle.getString("waitingForOpponent"));
            } else {
                if (game.checkVictory(game.getPlayerHits())) {
                    gameUi.showVictoryMessage();
                    game.disableGamePlay();
                } else {
                    gameUi.showPlayerBoard();
                    Timer timer = new Timer(1000, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            game.computerTurn();
                        }
                    });
                    timer.setRepeats(false);
                    timer.start();
                }
            }
        });

        gameUi.getChatInput().addActionListener(e -> {
            JTextField chatInput = gameUi.getChatInput();
            String message = chatInput.getText();
            if (!message.trim().isEmpty()) {
                gameUi.showChatMessage(bundle.getString("playerPrefix") + message);
                if (network != null) {
                    network.sendMessage("CHAT:" + message);
                }
                chatInput.setText("");
            }
        });

        gameUi.getDisconnectItem().addActionListener(e -> {
            try {
                if (network != null) {
                    network.disconnect();
                }
                if (host != null) {
                    host.stop();
                }
                if (client != null) {
                    client.disconnect();
                }
                gameUi.showMessage(bundle.getString("disconnected"));
                gameUi.showMenu(); // Show the menu again after disconnection
                game.disableShipPlacement(); // Disable ship placement
                game.disableGamePlay(); // Disable gameplay
            } catch (IOException ex) {
                gameUi.showMessage(bundle.getString("failedToDisconnect") + ex.getMessage());
            }
        });
    }
}
