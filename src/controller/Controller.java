package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;
import javax.swing.Timer;
import javax.swing.JTextField;
import model.Game;
import model.Network;
import view.GameUi;

public class Controller {
    private GameUi gameUi;
    private Game game;
    private Network network;

    public Controller(GameUi gameUi) {
        this.gameUi = gameUi;
        this.game = new Game(gameUi);
        initializeController();
    }

    private void initializeController() {
        gameUi.getPveItem().addActionListener(e -> {
            gameUi.showPveDialog();
            game.enableShipPlacement();
            gameUi.showMessage("Game mode: PVE");
        });

        gameUi.getHostItem().addActionListener(e -> {
            network = new Network(this, gameUi);
            network.startHost();
        });

        gameUi.getConnectItem().addActionListener(e -> {
            network = new Network(this, gameUi);
            network.startClient();
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
                if (network != null) {
                    network.sendMessage("READY::true");
                }
                game.placeComputerShips();
                gameUi.showComputerBoard();
                gameUi.getStartButton().setEnabled(false);
                game.enableGamePlay();
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
                    network.sendMessage("CHAT::" + message);
                }
                chatInput.setText("");
            }
        });
    }

    public void showChatMessage(String chatMessage) {
        gameUi.showChatMessage(chatMessage);
    }

    public void processReadyMessage(boolean ready) {
        // Handle ready message
    }
}
