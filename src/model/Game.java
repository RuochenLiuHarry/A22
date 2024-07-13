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
    private boolean[][] playerBoard;
    private boolean[][] computerBoard;
    private List<String> shipNames;
    private List<Integer> shipLengths;
    private int currentShipIndex;
    private boolean isVertical;
    private boolean isPlayerTurn;
    private boolean[][] computerBoardHits;
    private boolean hasPlayerMadeMove;
    private boolean isHunting;
    private int lastHitX;
    private int lastHitY;
    private List<int[]> availableMoves;
    private List<int[]> huntMoves;
    private boolean isFirstMove;

    public Game(GameUi gameUi) {
        this.gameUi = gameUi;
        this.playerBoard = new boolean[10][10];
        this.computerBoard = new boolean[10][10];
        this.shipNames = new ArrayList<>();
        this.shipLengths = new ArrayList<>();
        this.currentShipIndex = 0;
        this.isVertical = true; // Default rotation is vertical
        this.isPlayerTurn = true;
        this.computerBoardHits = new boolean[10][10];
        this.hasPlayerMadeMove = false;
        this.isHunting = false;
        this.isFirstMove = true;
        this.availableMoves = generateAvailableMoves();
        this.huntMoves = new ArrayList<>();
        initializeShips();
    }

    private void initializeShips() {
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

    public void enableShipPlacement() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                int x = i;
                int y = j;
                JButton button = gameUi.getGridButtons()[i][j];
                button.addActionListener(e -> {
                    if (placeShip(x, y)) {
                        if (getCurrentShipName() == null) {
                            gameUi.showAllShipsPlacedMessage();
                            disableShipPlacement(); // Disable ship placement buttons
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
        if (canPlaceShip(x, y, length, playerBoard)) {
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
            setShipPosition(x, y, length, playerBoard);
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

    // Computer board methods
    public void placeComputerShips() {
        Random random = new Random();
        for (int i = 0; i < shipNames.size(); i++) {
            int length = shipLengths.get(i);
            boolean placed = false;
            while (!placed) {
                int x = random.nextInt(10);
                int y = random.nextInt(10);
                isVertical = random.nextBoolean();
                if (canPlaceShip(x, y, length, computerBoard)) {
                    setShipPosition(x, y, length, computerBoard);
                    placed = true;
                }
            }
        }
    }

    public boolean checkHit(int x, int y, boolean[][] board) {
        return board[x][y];
    }

    public boolean markHitOrMiss(int x, int y, boolean[][] board, boolean isHit) {
        board[x][y] = isHit;
        return isHit;
    }

    public boolean checkVictory(boolean[][] board) {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (board[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean[][] getPlayerBoard() {
        return playerBoard;
    }

    public boolean[][] getComputerBoard() {
        return computerBoard;
    }

    public void enableGamePlay() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                int x = i;
                int y = j;
                JButton button = gameUi.getComputerGridButtons()[i][j];
                button.addActionListener(e -> {
                    if (isPlayerTurn && !hasPlayerMadeMove) {
                        boolean isHit = checkHit(x, y, getComputerBoard());
                        markHitOrMiss(x, y, computerBoardHits, isHit);
                        gameUi.markComputerBoard(x, y, isHit ? gameUi.getHitIcon() : gameUi.getMissIcon());
                        hasPlayerMadeMove = true;
                        if (checkVictory(getComputerBoard())) {
                            gameUi.showVictoryMessage();
                        }
                    } else if (isPlayerTurn) {
                        gameUi.showCannotGoTwiceMessage();
                    }
                });
            }
        }
    }

    public void computerTurn() {
        SwingUtilities.invokeLater(() -> {
            boolean turnOver = false;
            int x = 0, y = 0;
            Random random = new Random();

            while (!turnOver) {
                if (isFirstMove) {
                    // Find the first ship position and target it
                    for (int i = 0; i < 10; i++) {
                        for (int j = 0; j < 10; j++) {
                            if (getPlayerBoard()[i][j]) {
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

                if (!checkHit(x, y, getPlayerBoard())) {
                    boolean isHit = checkHit(x, y, getPlayerBoard());
                    markHitOrMiss(x, y, getPlayerBoard(), isHit);
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
                    if (checkVictory(getPlayerBoard())) {
                        gameUi.showLossMessage();
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
            if (newX >= 0 && newX < 10 && newY >= 0 && newY < 10 && !checkHit(newX, newY, getPlayerBoard())) {
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

    public void setPlayerTurn(boolean isPlayerTurn) {
        this.isPlayerTurn = isPlayerTurn;
    }

    public void setHasPlayerMadeMove(boolean hasPlayerMadeMove) {
        this.hasPlayerMadeMove = hasPlayerMadeMove;
    }

    public boolean isPlayerTurn() {
        return isPlayerTurn;
    }

    public boolean hasPlayerMadeMove() {
        return hasPlayerMadeMove;
    }
}
