package model;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private boolean[][] playerBoard;
    private List<String> shipNames;
    private List<Integer> shipLengths;
    private int currentShipIndex;
    private boolean isVertical;

    public Game() {
        playerBoard = new boolean[10][10];
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
        if (canPlaceShip(x, y, length)) {
            setShipPosition(x, y, length);
            currentShipIndex++;
            return true;
        }
        return false;
    }

    private boolean canPlaceShip(int x, int y, int length) {
        if (isVertical) {
            if (x + length > 10) return false;
            for (int i = 0; i < length; i++) {
                if (playerBoard[x + i][y]) return false;
            }
        } else {
            if (y + length > 10) return false;
            for (int i = 0; i < length; i++) {
                if (playerBoard[x][y + i]) return false;
            }
        }
        return true;
    }

    private void setShipPosition(int x, int y, int length) {
        if (isVertical) {
            for (int i = 0; i < length; i++) {
                playerBoard[x + i][y] = true;
            }
        } else {
            for (int i = 0; i < length; i++) {
                playerBoard[x][y + i] = true;
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
}
