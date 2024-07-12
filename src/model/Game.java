package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game {
    private boolean[][] playerBoard;
    private boolean[][] computerBoard;
    private List<String> shipNames;
    private List<Integer> shipLengths;
    private int currentShipIndex;
    private boolean isVertical;

    public Game() {
        playerBoard = new boolean[10][10];
        computerBoard = new boolean[10][10];
        shipNames = new ArrayList<>();
        shipLengths = new ArrayList<>();
        initializeShips();
        currentShipIndex = 0;
        isVertical = true; // Default rotation is vertical
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

    public boolean placeShip(int x, int y) {
        if (currentShipIndex >= shipNames.size()) return false;
        int length = shipLengths.get(currentShipIndex);
        if (canPlaceShip(x, y, length, playerBoard)) {
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
}
