package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;
import model.Game;
import view.GameUi;

public class Controller {
    private GameUi gameUi;
    private Game game;
    private JDialog hostDialog;
    private JDialog connectDialog;

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
                gameUi.getStartButton().setEnabled(false);
                game.enableGamePlay();
            }
        });

        // End Turn
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
    }

    private void addHostDialogListeners() {
        JButton hostButton = (JButton) ((JPanel) hostDialog.getContentPane().getComponent(6)).getComponent(0);
        JButton cancelButton = (JButton) ((JPanel) hostDialog.getContentPane().getComponent(7)).getComponent(0);
        JLabel statusLabel = (JLabel) hostDialog.getContentPane().getComponent(5);
        JComboBox<Integer> portBox = (JComboBox<Integer>) hostDialog.getContentPane().getComponent(3);

        hostButton.addActionListener(e -> {
            statusLabel.setText("Hosting on port " + portBox.getSelectedItem());
            // Implement hosting logic here
        });

        cancelButton.addActionListener(e -> hostDialog.dispose());
    }

    private void addConnectDialogListeners() {
        JButton connectButton = (JButton) ((JPanel) connectDialog.getContentPane().getComponent(8)).getComponent(0);
        JButton cancelButton = (JButton) ((JPanel) connectDialog.getContentPane().getComponent(9)).getComponent(0);
        JLabel statusLabel = (JLabel) connectDialog.getContentPane().getComponent(7);
        JTextField addressField = (JTextField) connectDialog.getContentPane().getComponent(3);
        JComboBox<Integer> portBox = (JComboBox<Integer>) connectDialog.getContentPane().getComponent(5);

        connectButton.addActionListener(e -> {
            statusLabel.setText("Connected to " + addressField.getText() + ":" + portBox.getSelectedItem());
            // Implement connection logic here
        });

        cancelButton.addActionListener(e -> connectDialog.dispose());
    }
}
