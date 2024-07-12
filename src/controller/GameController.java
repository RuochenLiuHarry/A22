package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import model.Game;
import view.GameUi;

public class GameController {
    private GameUi gameUi;
    private Game game;

    public GameController(GameUi gameUi) {
        this.gameUi = gameUi;
        this.game = new Game();
        initializeController();
    }

    private void initializeController() {
        // PVE
        gameUi.getPveItem().addActionListener(e -> {
            gameUi.showPveDialog();
            enableShipPlacement();
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
            JOptionPane.showMessageDialog(gameUi, "Ship rotation toggled to " + (game.isVertical() ? "vertical" : "horizontal"));
        });
    }

    private void enableShipPlacement() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                int x = i;
                int y = j;
                JButton button = gameUi.getGridButtons()[i][j];
                button.addActionListener(e -> {
                    if (placeShip(x, y)) {
                        if (game.getCurrentShipName() == null) {
                            JOptionPane.showMessageDialog(gameUi, "All ships placed!");
                        }
                    } else {
                        JOptionPane.showMessageDialog(gameUi, "Cannot place ship here!");
                    }
                });
            }
        }
    }

    private boolean placeShip(int x, int y) {
        int length = game.getCurrentShipLength();
        if (game.placeShip(x, y)) {
            if (game.isVertical()) {
                gameUi.placeShipPart(x, y, gameUi.getBowNorth());
                for (int i = 1; i < length - 1; i++) {
                    gameUi.placeShipPart(x + i, y, gameUi.getMidHullVert());
                }
                gameUi.placeShipPart(x + length - 1, y, gameUi.getBowSouth());
            } else {
                gameUi.placeShipPart(x, y, gameUi.getBowWest());
                for (int i = 1; i < length - 1; i++) {
                    gameUi.placeShipPart(x, y + i, gameUi.getMidHullHoriz());
                }
                gameUi.placeShipPart(x, y + length - 1, gameUi.getBowEast());
            }
            return true;
        }
        return false;
    }
}
