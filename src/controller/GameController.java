package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import model.Game;
import view.GameUi;

public class GameController {
    private GameUi gameUi;
    private Game game;
    private boolean isPlayerTurn;
    private boolean[][] computerBoardHits;
    private boolean hasPlayerMadeMove;
    private boolean isHunting;
    private int lastHitX;
    private int lastHitY;
    private List<int[]> availableMoves;
    private List<int[]> huntMoves;
    private boolean isFirstMove;

    public GameController(GameUi gameUi) {
        this.gameUi = gameUi;
        this.game = new Game();
        this.isPlayerTurn = true;
        this.computerBoardHits = new boolean[10][10];
        this.hasPlayerMadeMove = false;
        this.isHunting = false;
        this.isFirstMove = true;
        this.availableMoves = generateAvailableMoves();
        this.huntMoves = new ArrayList<>();
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
            gameUi.showPlayerBoard();

            // Use Timer for delay instead of Thread.sleep
            Timer timer = new Timer(1500, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    computerTurn();
                }
            });
            timer.setRepeats(false);
            timer.start();
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
                        if (game.checkVictory(game.getComputerBoard())) {
                            JOptionPane.showMessageDialog(gameUi, "Victory! You have won the game of Battleship!");
                        }
                    } else if (isPlayerTurn) {
                        JOptionPane.showMessageDialog(gameUi, "Cannot go twice! Please click End Turn button to end your turn.");
                    }
                });
            }
        }
    }

    private void computerTurn() {
        SwingUtilities.invokeLater(() -> {
            boolean turnOver = false;
            int x = 0, y = 0;
            Random random = new Random();

            while (!turnOver) {
                if (isFirstMove) {
                    // Find the first ship position and target it
                    for (int i = 0; i < 10; i++) {
                        for (int j = 0; j < 10; j++) {
                            if (game.getPlayerBoard()[i][j]) {
                                x = i;
                                y = j;
                                isFirstMove = false;
                                break;
                            }
                        }
                        if (!isFirstMove) break;
                    }
                } else if (isHunting && !huntMoves.isEmpty()) {
                    int[] nextMove = huntMoves.remove(0);
                    x = nextMove[0];
                    y = nextMove[1];
                } else {
                    int index = random.nextInt(availableMoves.size());
                    int[] move = availableMoves.remove(index);
                    x = move[0];
                    y = move[1];
                }

                if (!game.checkHit(x, y, game.getPlayerBoard())) {
                    boolean isHit = game.checkHit(x, y, game.getPlayerBoard());
                    game.markHitOrMiss(x, y, game.getPlayerBoard(), isHit);
                    gameUi.markPlayerBoard(x, y, isHit ? gameUi.getHitIcon() : gameUi.getMissIcon());
                    turnOver = true;

                    if (isHit) {
                        isHunting = true;
                        lastHitX = x;
                        lastHitY = y;
                        addHuntMoves(lastHitX, lastHitY);
                    } else {
                        isHunting = false;
                    }
                }
            }

            gameUi.showPlayerBoard();

            // Use Timer for delay
            Timer timer = new Timer(1500, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (game.checkVictory(game.getPlayerBoard())) {
                        JOptionPane.showMessageDialog(gameUi, "Computer has sunk all of your ships. You lost!");
                    } else {
                        isPlayerTurn = true;
                        gameUi.showComputerBoard();
                    }
                }
            });
            timer.setRepeats(false);
            timer.start();
        });
    }

    private void addHuntMoves(int x, int y) {
        int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        for (int[] direction : directions) {
            int newX = x + direction[0];
            int newY = y + direction[1];
            if (newX >= 0 && newX < 10 && newY >= 0 && newY < 10 && !game.checkHit(newX, newY, game.getPlayerBoard())) {
                huntMoves.add(new int[]{newX, newY});
            }
        }
    }

    private List<int[]> generateAvailableMoves() {
        List<int[]> moves = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                moves.add(new int[]{i, j});
            }
        }
        return moves;
    }
}
