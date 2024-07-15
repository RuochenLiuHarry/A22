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

/**
 * Game class representing the Battleship game logic and state.
 */
public class Game {
	 /**
     * The user interface for the game.
     */
    private GameUi gameUi;

    /**
     * The player's game board, represented as a 10x10 grid of booleans.
     */
    private boolean[][] playerBoard;

    /**
     * The computer's game board, represented as a 10x10 grid of booleans.
     */
    private boolean[][] computerBoard;

    /**
     * The player's hits on the computer's board, represented as a 10x10 grid of booleans.
     */
    private boolean[][] playerBoardHits;

    /**
     * The computer's hits on the player's board, represented as a 10x10 grid of booleans.
     */
    private boolean[][] computerBoardHits;

    /**
     * The names of the ships.
     */
    private List<String> shipNames;

    /**
     * The lengths of the ships.
     */
    private List<Integer> shipLengths;

    /**
     * The index of the current ship being placed.
     */
    private int currentShipIndex;

    /**
     * Whether the current ship placement is vertical.
     */
    private boolean isVertical;

    /**
     * Whether it is the player's turn.
     */
    private boolean isPlayerTurn;

    /**
     * Whether the player has made a move in the current turn.
     */
    private boolean hasPlayerMadeMove;

    /**
     * Whether the computer is in hunting mode.
     */
    private boolean isHunting;

    /**
     * The x-coordinate of the last hit made by the computer.
     */
    private int lastHitX;

    /**
     * The y-coordinate of the last hit made by the computer.
     */
    private int lastHitY;

    /**
     * The list of available moves for the computer.
     */
    private List<int[]> availableMoves;

    /**
     * The list of moves for the computer when in hunting mode.
     */
    private List<int[]> huntMoves;

    /**
     * Whether it is the computer's first move.
     */
    private boolean isFirstMove;

    /**
     * The number of hits made by the player.
     */
    private int playerHits;

    /**
     * The number of hits made by the computer.
     */
    private int computerHits;

    /**
     * The total number of ship parts for victory check.
     */
    private static final int TOTAL_SHIP_PARTS = 17;

    /**
     * Constructs a Game with the specified GameUi.
     * 
     * @param gameUi the GameUi instance to be used by the Game
     */
    public Game(GameUi gameUi) {
        this.gameUi = gameUi;
        this.playerBoard = new boolean[10][10];
        this.computerBoard = new boolean[10][10];
        this.playerBoardHits = new boolean[10][10];
        this.computerBoardHits = new boolean[10][10];
        this.shipNames = new ArrayList<>();
        this.shipLengths = new ArrayList<>();
        this.currentShipIndex = 0;
        this.isVertical = true; // Default rotation is vertical
        this.isPlayerTurn = true;
        this.hasPlayerMadeMove = false;
        this.isHunting = false;
        this.isFirstMove = true;
        this.availableMoves = generateAvailableMoves();
        this.huntMoves = new ArrayList<>();
        this.playerHits = 0;
        this.computerHits = 0;
        initializeShips();
    }

    /**
     * Initializes the ships with their names and lengths.
     */
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

    /**
     * Resets the game state and re-enables ship placement.
     */
    public void resetGame() {
        // Reset game boards
        playerBoard = new boolean[10][10];
        computerBoard = new boolean[10][10];
        playerBoardHits = new boolean[10][10];
        computerBoardHits = new boolean[10][10];

        // Reset ship placement and game state
        currentShipIndex = 0;
        isVertical = true; // Default rotation is vertical
        isPlayerTurn = true;
        hasPlayerMadeMove = false;
        isHunting = false;
        isFirstMove = true;
        availableMoves = generateAvailableMoves();
        huntMoves = new ArrayList<>();
        playerHits = 0;
        computerHits = 0;

        // Re-enable ship placement
        enableShipPlacement();
    }
    
    /**
     * Enables ship placement by adding action listeners to the grid buttons.
     */
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

    /**
     * Disables ship placement by removing action listeners from the grid buttons.
     */
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

    /**
     * Places a ship at the specified coordinates.
     * 
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @return true if the ship is placed successfully, false otherwise
     */
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

    /**
     * Checks if a ship can be placed at the specified coordinates.
     * 
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @param length the length of the ship
     * @param board the game board
     * @return true if the ship can be placed, false otherwise
     */
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

    /**
     * Sets the ship position on the game board.
     * 
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @param length the length of the ship
     * @param board the game board
     */
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

    /**
     * Gets the length of the current ship.
     * 
     * @return the length of the current ship
     */
    public int getCurrentShipLength() {
        if (currentShipIndex >= shipLengths.size()) return 0;
        return shipLengths.get(currentShipIndex);
    }

    /**
     * Gets the name of the current ship.
     * 
     * @return the name of the current ship
     */
    public String getCurrentShipName() {
        if (currentShipIndex >= shipNames.size()) return null;
        return shipNames.get(currentShipIndex);
    }

    /**
     * Checks if the current rotation is vertical.
     * 
     * @return true if the rotation is vertical, false otherwise
     */
    public boolean isVertical() {
        return isVertical;
    }

    /**
     * Toggles the rotation between vertical and horizontal.
     */
    public void toggleRotation() {
        isVertical = !isVertical;
    }

    /**
     * Places the computer's ships randomly on the board.
     */
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

    /**
     * Checks if the specified coordinates are a hit.
     * 
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @param board the game board
     * @return true if it is a hit, false otherwise
     */
    public boolean checkHit(int x, int y, boolean[][] board) {
        return board[x][y];
    }

    /**
     * Marks the specified coordinates as a hit or miss on the hits board.
     * 
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @param hitsBoard the hits board
     * @param isHit true if it is a hit, false otherwise
     */
    public void markHitOrMiss(int x, int y, boolean[][] hitsBoard, boolean isHit) {
        hitsBoard[x][y] = true;
        if (isHit) {
            if (hitsBoard == computerBoardHits) {
                playerHits++;
            } else if (hitsBoard == playerBoardHits) {
                computerHits++;
            }
        }
    }

    /**
     * Checks if the specified number of hits indicates a victory.
     * 
     * @param hits the number of hits
     * @return true if it is a victory, false otherwise
     */
    public boolean checkVictory(int hits) {
        return hits >= TOTAL_SHIP_PARTS;
    }

    /**
     * Gets the number of player hits.
     * 
     * @return the number of player hits
     */
    public int getPlayerHits() {
        return playerHits;
    }

    /**
     * Gets the number of computer hits.
     * 
     * @return the number of computer hits
     */
    public int getComputerHits() {
        return computerHits;
    }

    /**
     * Gets the player's game board.
     * 
     * @return the player's game board
     */
    public boolean[][] getPlayerBoard() {
        return playerBoard;
    }

    /**
     * Gets the computer's game board.
     * 
     * @return the computer's game board
     */
    public boolean[][] getComputerBoard() {
        return computerBoard;
    }
    
    /**
     * Enables gameplay by adding action listeners to the computer grid buttons.
     */
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
                    if (isPlayerTurn) {
                        if (hasPlayerMadeMove) {
                            //gameUi.showCannotGoTwiceMessage();
                        } else {
                            boolean isHit = checkHit(x, y, getComputerBoard());
                            markHitOrMiss(x, y, computerBoardHits, isHit);
                            gameUi.markComputerBoard(x, y, isHit ? gameUi.getHitIcon() : gameUi.getMissIcon());
                            hasPlayerMadeMove = true;
                            if (checkVictory(playerHits)) {
                                gameUi.showVictoryMessage();
                                disableGamePlay();
                            }
                        }
                    }
                });
            }
        }
    }

    /**
     * Disables gameplay by removing action listeners from the computer grid buttons.
     */
    public void disableGamePlay() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                JButton button = gameUi.getComputerGridButtons()[i][j];
                for (ActionListener al : button.getActionListeners()) {
                    button.removeActionListener(al);
                }
            }
        }
    }

    /**
     * Executes the computer's turn.
     */
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

                if (!playerBoardHits[x][y]) {
                    boolean isHit = checkHit(x, y, getPlayerBoard());
                    markHitOrMiss(x, y, playerBoardHits, isHit);
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
            Timer timer = new Timer(1000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (checkVictory(computerHits)) {
                        gameUi.showLossMessage();
                        disableGamePlay();
                    } else {
                        isPlayerTurn = true;
                        gameUi.showComputerBoard();
                        gameUi.showYourTurn();
                    }
                }
            });
            timer.setRepeats(false);
            timer.start();
        });
    }

    /**
     * Adds potential moves around a hit location to the hunt moves list.
     * 
     * @param x the x-coordinate of the hit
     * @param y the y-coordinate of the hit
     */
    private void addHuntMoves(int x, int y) {
        int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        for (int[] direction : directions) {
            int newX = x + direction[0];
            int newY = y + direction[1];
            if (newX >= 0 && newX < 10 && newY >= 0 && newY < 10 && !playerBoardHits[newX][newY]) {
                huntMoves.add(new int[]{newX, newY});
            }
        }
    }

    /**
     * Generates a list of all possible moves.
     * 
     * @return the list of all possible moves
     */
    private List<int[]> generateAvailableMoves() {
        List<int[]> moves = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                moves.add(new int[]{i, j});
            }
        }
        return moves;
    }

    /**
     * Sets whether it is the player's turn.
     * 
     * @param isPlayerTurn true if it is the player's turn, false otherwise
     */
    public void setPlayerTurn(boolean isPlayerTurn) {
        this.isPlayerTurn = isPlayerTurn;
    }

    /**
     * Sets whether the player has made a move.
     * 
     * @param hasPlayerMadeMove true if the player has made a move, false otherwise
     */
    public void setHasPlayerMadeMove(boolean hasPlayerMadeMove) {
        this.hasPlayerMadeMove = hasPlayerMadeMove;
    }

    /**
     * Checks if it is the player's turn.
     * 
     * @return true if it is the player's turn, false otherwise
     */
    public boolean isPlayerTurn() {
        return isPlayerTurn;
    }

    /**
     * Checks if the player has made a move.
     * 
     * @return true if the player has made a move, false otherwise
     */
    public boolean hasPlayerMadeMove() {
        return hasPlayerMadeMove;
    }
}
