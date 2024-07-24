package model;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import view.GameUi;

public class Game {
    private GameUi gameUi;

    private boolean[][] player1Board;
    private boolean[][] player2Board;
    private boolean[][] player1BoardHits;
    private boolean[][] player2BoardHits;

    private List<String> shipNames;
    private List<Integer> shipLengths;

    private int currentShipIndex;

    private boolean isVertical;
    private boolean isPlayer1Turn;
    private boolean hasPlayer1MadeMove;
    private boolean hasPlayer2MadeMove;

    private int player1Hits;
    private int player2Hits;

    private static final int TOTAL_SHIP_PARTS = 17;

    public Game(GameUi gameUi) {
        this.gameUi = gameUi;
        this.player1Board = new boolean[10][10];
        this.player2Board = new boolean[10][10];
        this.player1BoardHits = new boolean[10][10];
        this.player2BoardHits = new boolean[10][10];
        this.shipNames = new ArrayList<>();
        this.shipLengths = new ArrayList<>();
        this.currentShipIndex = 0;
        this.isVertical = true;
        this.isPlayer1Turn = true;
        this.hasPlayer1MadeMove = false;
        this.hasPlayer2MadeMove = false;
        this.player1Hits = 0;
        this.player2Hits = 0;
        initializeShips();
    }

    private void initializeShips() {
        shipNames.clear();
        shipLengths.clear();

        shipNames.add("Carrier");
        shipLengths.add(5);

        shipNames.add("Cruiser");
        shipLengths.add(4);

        shipNames.add("Destroyer");
        shipLengths.add(3);

        shipNames.add("Missile Frigate");
        shipLengths.add(3);

        shipNames.add("Submarine");
        shipLengths.add(2);
    }

    public void resetGame() {
        player1Board = new boolean[10][10];
        player2Board = new boolean[10][10];
        player1BoardHits = new boolean[10][10];
        player2BoardHits = new boolean[10][10];

        currentShipIndex = 0;
        isVertical = true;
        isPlayer1Turn = true;
        hasPlayer1MadeMove = false;
        hasPlayer2MadeMove = false;
        player1Hits = 0;
        player2Hits = 0;

        enableShipPlacement();
    }

    public void enableShipPlacement() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                int x = i;
                int y = j;
                JButton button = gameUi.getGridButtons()[i][j];
                button.addActionListener(e -> {
                    if (placeShip(x, y)) {
                        if (getCurrentShipName() == null) {
                            if (isPlayer1Turn) {
                                gameUi.showMessage("Player 1 is ready!");
                                gameUi.getStartButton().setEnabled(true);
                            } else {
                                gameUi.showMessage("Player 2 is ready! Player 1's turn.");
                                gameUi.getStartButton().setEnabled(false);
                                gameUi.getEndTurnButton().setEnabled(true);
                                gameUi.getRotateButton().setEnabled(false);
                                disableShipPlacement();
                                enableGamePlay();
                            }
                        }
                    } else {
                        gameUi.showCannotPlaceShipMessage();
                    }
                });
            }
        }
    }

    private void disableShipPlacement() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                JButton button = gameUi.getGridButtons()[i][j];
                for (ActionListener al : button.getActionListeners()) {
                    button.removeActionListener(al);
                }
            }
        }
    }

    public boolean placeShip(int x, int y) {
        int length = getCurrentShipLength();
        if (isPlayer1Turn && canPlaceShip(x, y, length, player1Board)) {
            placeShipOnBoard(x, y, length, player1Board, gameUi.getGridButtons());
            currentShipIndex++;
            return true;
        } else if (!isPlayer1Turn && canPlaceShip(x, y, length, player2Board)) {
            placeShipOnBoard(x, y, length, player2Board, gameUi.getGridButtons());
            currentShipIndex++;
            return true;
        }
        return false;
    }

    private boolean canPlaceShip(int x, int y, int length, boolean[][] board) {
        if (isVertical) {
            if (x + length > 10) return false;
            for (int i = 0; i < length; i++) {
                if (board[x + i][y]) return false;
            }
        } else {
            if (y + length > 10) return false;
            for (int i = 0; i < length; i++) {
                if (board[x][y + i]) return false;
            }
        }
        return true;
    }

    private void placeShipOnBoard(int x, int y, int length, boolean[][] board, JButton[][] buttons) {
        if (isVertical) {
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
        setShipPosition(x, y, length, board);
    }

    private void setShipPosition(int x, int y, int length, boolean[][] board) {
        if (isVertical) {
            for (int i = 0; i < length; i++) {
                board[x + i][y] = true;
            }
        } else {
            for (int i = 0; i < length; i++) {
                board[x][y + i] = true;
            }
        }
    }

    public int getCurrentShipLength() {
        if (currentShipIndex >= shipLengths.size()) return 0;
        return shipLengths.get(currentShipIndex);
    }

    public String getCurrentShipName() {
        if (currentShipIndex >= shipNames.size()) return null;
        return shipNames.get(currentShipIndex);
    }

    public boolean isVertical() {
        return isVertical;
    }

    public void toggleRotation() {
        isVertical = !isVertical;
    }

    public void enableGamePlay() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                int x = i;
                int y = j;
                JButton button = gameUi.getComputerGridButtons()[i][j];
                for (ActionListener al : button.getActionListeners()) {
                    button.removeActionListener(al);
                }
                button.addActionListener(e -> {
                    if (isPlayer1Turn) {
                        if (hasPlayer1MadeMove) {
                            gameUi.showCannotGoTwiceMessage();
                        } else {
                            handlePlayerMove(x, y, player2Board, player2BoardHits);
                            hasPlayer1MadeMove = true;
                            if (checkVictory(player1Hits)) {
                                gameUi.showVictoryMessage();
                                disableGamePlay();
                            }
                        }
                    } else {
                        if (hasPlayer2MadeMove) {
                            gameUi.showCannotGoTwiceMessage();
                        } else {
                            handlePlayerMove(x, y, player1Board, player1BoardHits);
                            hasPlayer2MadeMove = true;
                            if (checkVictory(player2Hits)) {
                                gameUi.showVictoryMessage();
                                disableGamePlay();
                            }
                        }
                    }
                });
            }
        }
    }

    private void handlePlayerMove(int x, int y, boolean[][] opponentBoard, boolean[][] opponentBoardHits) {
        boolean isHit = checkHit(x, y, opponentBoard);
        markHitOrMiss(x, y, opponentBoardHits, isHit);
        if (isPlayer1Turn) {
            gameUi.markComputerBoard(x, y, isHit ? gameUi.getHitIcon() : gameUi.getMissIcon());
        } else {
            gameUi.markPlayerBoard(x, y, isHit ? gameUi.getHitIcon() : gameUi.getMissIcon());
        }
    }

    private void disableGamePlay() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                JButton button = gameUi.getComputerGridButtons()[i][j];
                for (ActionListener al : button.getActionListeners()) {
                    button.removeActionListener(al);
                }
            }
        }
    }

    private boolean checkHit(int x, int y, boolean[][] board) {
        return board[x][y];
    }

    private void markHitOrMiss(int x, int y, boolean[][] hitsBoard, boolean isHit) {
        hitsBoard[x][y] = true;
        if (isHit) {
            if (hitsBoard == player2BoardHits) {
                player1Hits++;
            } else if (hitsBoard == player1BoardHits) {
                player2Hits++;
            }
        }
    }

    private boolean checkVictory(int hits) {
        return hits >= TOTAL_SHIP_PARTS;
    }

    public void setPlayerTurn(boolean isPlayer1Turn) {
        this.isPlayer1Turn = isPlayer1Turn;
    }

    public boolean isPlayerTurn() {
        return isPlayer1Turn;
    }

    public void setHasPlayerMadeMove(boolean hasPlayerMadeMove) {
        if (isPlayer1Turn) {
            this.hasPlayer1MadeMove = hasPlayerMadeMove;
        } else {
            this.hasPlayer2MadeMove = hasPlayerMadeMove;
        }
    }

    public boolean hasPlayerMadeMove() {
        return isPlayer1Turn ? hasPlayer1MadeMove : hasPlayer2MadeMove;
    }

    public int getPlayer1Hits() {
        return player1Hits;
    }

    public int getPlayer2Hits() {
        return player2Hits;
    }
}
