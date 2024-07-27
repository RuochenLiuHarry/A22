package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;
import javax.swing.Timer;
import javax.swing.JTextField;
import model.Game;
import view.GameUi;

/**
 * Controller class responsible for handling user interactions
 * and managing the game state in the Battleship game.
 */
public class Controller {
    private GameUi gameUi;
    private Game game;

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
    }
}
