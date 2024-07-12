package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import model.Game;
import view.GameUi;

public class GameController {
    private GameUi gameUi;
    private Game game;
    private boolean isPlayerTurn;
    private boolean[][] computerBoardHits;
    private boolean hasPlayerMadeMove;

    public GameController(GameUi gameUi) {
        this.gameUi = gameUi;
        this.game = new Game();
        this.isPlayerTurn = true;
        this.computerBoardHits = new boolean[10][10];
        this.hasPlayerMadeMove = false;
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

        // Start Game
        gameUi.getStartButton().addActionListener(e -> {
            if (game.getCurrentShipName() != null) {
                JOptionPane.showMessageDialog(gameUi, "Place all your ships before starting the game.");
            } else {
                game.placeComputerShips();
                gameUi.showComputerBoard();
                gameUi.getStartButton().setEnabled(false); // Disable the Start Game button
                enableGamePlay();
            }
        });

        // End Turn
        gameUi.getEndTurnButton().addActionListener(e -> {
            if (!isPlayerTurn || !hasPlayerMadeMove) return;
            isPlayerTurn = false;
            hasPlayerMadeMove = false;
            computerTurn();
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
                            JOptionPane.showMessageDialog(gameUi, "All ships placed! Click Start Game To Play!!");
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

    private void enableGamePlay() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                int x = i;
                int y = j;
                JButton button = gameUi.getComputerGridButtons()[i][j];
                button.addActionListener(e -> {
                    if (isPlayerTurn && !hasPlayerMadeMove) {
                        boolean isHit = game.checkHit(x, y, game.getComputerBoard());
                        game.markHitOrMiss(x, y, computerBoardHits, isHit);
                        gameUi.markComputerBoard(x, y, isHit ? gameUi.getHitIcon() : gameUi.getMissIcon());
                        hasPlayerMadeMove = true;
                    } else if (isPlayerTurn) {
                        JOptionPane.showMessageDialog(gameUi, "Cannot go twice! Please click End Turn button to end your turn.");
                    }
                });
            }
        }
    }

    private void computerTurn() {
        SwingUtilities.invokeLater(() -> {
            Random random = new Random();
            boolean turnOver = false;
            int x = 0, y = 0;
            while (!turnOver) {
                x = random.nextInt(10);
                y = random.nextInt(10);
                if (!game.checkHit(x, y, game.getPlayerBoard())) {
                    boolean isHit = game.checkHit(x, y, game.getPlayerBoard());
                    game.markHitOrMiss(x, y, game.getPlayerBoard(), isHit);
                    gameUi.markPlayerBoard(x, y, isHit ? gameUi.getHitIcon() : gameUi.getMissIcon());
                    turnOver = true;
                }
            }
            gameUi.showPlayerBoard();
            SwingUtilities.invokeLater(() -> {
                try {
                    Thread.sleep(3000); // Small delay to allow user to see computer's move
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                isPlayerTurn = true;
                gameUi.showComputerBoard();
            });
        });
    }
}
