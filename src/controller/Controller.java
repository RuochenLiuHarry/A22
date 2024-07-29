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

/**
 * Controller class responsible for handling user interactions
 * and managing the game state in the Battleship game.
 */
public class Controller {
    private GameUi gameUi;
    private Game game;
    private Host host;
    private Client client;
    private Network network;
    private CustomDialog serverDialog;
    private CustomDialog clientDialog;
    private boolean isHost;
    /**
     * Constructs a Controller with the specified GameUi.
     * 
     * @param gameUi the GameUi instance to be used by the Controller
     */
    public Controller(GameUi gameUi) {
        this.gameUi = gameUi;
        this.game = new Game(gameUi);
        initializeController();
    }

    /**
     * Initializes the Controller by setting up action listeners
     * for various UI components.
     */
    private void initializeController() {
        // PVE
        gameUi.getPveItem().addActionListener(e -> {
            gameUi.showPveDialog();
            game.enableShipPlacement();
            gameUi.showMessage("Game mode: PVE");
        });

        // PVP
        gameUi.getPvpItem().addActionListener(e -> {
            // Handle PVP logic here
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

            // Check for victory before computer's turn
            if (game.checkVictory(game.getPlayerHits())) {
                gameUi.showVictoryMessage();
                game.disableGamePlay();
            } else {
                // Use Timer for delay instead of Thread.sleep
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

        // Chat input
        gameUi.getChatInput().addActionListener(e -> {
            JTextField chatInput = gameUi.getChatInput();
            String message = chatInput.getText();
            if (!message.trim().isEmpty()) {
                gameUi.showChatMessage("Player: " + message);
                chatInput.setText(""); // Clear the chat input field
            }
        });
        
        gameUi.getHostItem().addActionListener(e -> {
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
                            network = new Network(socket);
                            gameUi.setNetwork(network);
                            isHost = true;
                            gameUi.showMessage("Connected as host.");
                            startNetworkListener();
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
            clientDialog = new CustomDialog(gameUi, false);
            clientDialog.setVisible(true);

            if (clientDialog.getAddress() != null) {
                try {
                    client = new Client();
                    client.connect(clientDialog.getAddress(), clientDialog.getPort());
                    network = new Network(client.getSocket());
                    gameUi.setNetwork(network);
                    isHost = false;
                    gameUi.showMessage("Connected to host.");
                    startNetworkListener();
                } catch (IOException ex) {
                    gameUi.showMessage("Failed to connect to host: " + ex.getMessage());
                }
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

        // Other existing listeners...
    }

    private void processNetworkMessage(String message) {
        if (message.startsWith("CHAT:")) {
            gameUi.receiveChatMessage("Opponent: " + message.substring(5));
        } else if (message.startsWith("SHOOT:")) {
            String[] parts = message.substring(6).split(",");
            int x = Integer.parseInt(parts[0]);
            int y = Integer.parseInt(parts[1]);
            boolean isHit = game.checkHit(x, y, game.getPlayerBoard());
            game.markHitOrMiss(x, y, game.getPlayerBoardHits(), isHit);
            network.sendMessage("HIT:" + isHit);
            if (game.checkVictory(game.getPlayerHits())) {
                network.sendMessage("GRESULT:true");
                gameUi.showVictoryMessage();
                game.disableGamePlay();
            }
            gameUi.showPlayerBoard();
        } else if (message.startsWith("HIT:")) {
            boolean isHit = Boolean.parseBoolean(message.substring(4));
            game.markHitOrMiss(game.getLastMoveX(), game.getLastMoveY(), game.getComputerBoardHits(), isHit);
            if (game.checkVictory(game.getComputerHits())) {
                gameUi.showLossMessage();
                game.disableGamePlay();
            }
            gameUi.showComputerBoard();
        } else if (message.startsWith("GRESULT:")) {
            boolean hasWon = Boolean.parseBoolean(message.substring(8));
            if (hasWon) {
                gameUi.showLossMessage();
            } else {
                gameUi.showVictoryMessage();
            }
            game.disableGamePlay();
        }
    }

    private void startNetworkListener() {
        new Thread(() -> {
            while (network != null) {
                try {
                    String message = network.receiveMessage();
                    if (message != null) {
                        processNetworkMessage(message);
                    }
                } catch (IOException ex) {
                    gameUi.showMessage("Network error: " + ex.getMessage());
                    break;
                }
            }
        }).start();
        
        
    }
}
