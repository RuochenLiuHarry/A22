package model;

import java.io.*;
import java.net.*;
import java.util.Locale;
import java.util.ResourceBundle;
import controller.Controller;
import view.GameUi;

/**
 * The Network class handles the network communication for the Battleship game.
 * It manages sending and receiving messages between the host and client, and processes game-related messages.
 */
public class Network {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private GameUi gameUi;
    private Game game;
    private Controller controller;
    private boolean isHost;
    private boolean isClientReady = false;
    private boolean isHostReady = false;
    private ResourceBundle bundle;

    /**
     * Constructor for the Network class.
     * Initializes the network communication with the specified socket, game UI, game logic, and controller.
     * 
     * @param socket the network socket
     * @param gameUi the GameUi instance
     * @param game the Game instance
     * @param controller the Controller instance
     * @param isHost true if this instance represents the host, false if it represents the client
     * @throws IOException if an I/O error occurs when setting up the network streams
     */
    public Network(Socket socket, GameUi gameUi, Game game, Controller controller, boolean isHost) throws IOException {
        this.socket = socket;
        this.gameUi = gameUi;
        this.game = game;
        this.controller = controller;
        this.isHost = isHost;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        bundle = ResourceBundle.getBundle("MessagesBundle", Locale.getDefault());
        startNetworkListener();
    }

    /**
     * Sends a message to the connected peer.
     * 
     * @param message the message to send
     */
    public void sendMessage(String message) {
        out.println(message);
    }

    /**
     * Receives a message from the connected peer.
     * 
     * @return the received message
     * @throws IOException if an I/O error occurs when reading the message
     */
    public String receiveMessage() throws IOException {
        return in.readLine();
    }

    /**
     * Closes the network connection and releases resources.
     * 
     * @throws IOException if an I/O error occurs when closing the network streams or socket
     */
    public void close() throws IOException {
        if (in != null) in.close();
        if (out != null) out.close();
        if (socket != null) socket.close();
    }

    /**
     * Disconnects from the network and resets the game UI and game logic.
     */
    public void disconnect() {
        try {
            sendMessage("DISCONNECT");
            close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            gameUi.showMessage(bundle.getString("disconnectedMessage"));
            game.resetGame();
            gameUi.resetUI();
            gameUi.showMenu();
            game.disableShipPlacement();
            game.disableGamePlay();
        }
    }

    /**
     * Sets the host ready status.
     * 
     * @param isHostReady true if the host is ready, false otherwise
     */
    public void setHostReady(boolean isHostReady) {
        this.isHostReady = isHostReady;
    }

    /**
     * Sets the client ready status.
     * 
     * @param isClientReady true if the client is ready, false otherwise
     */
    public void setClientReady(boolean isClientReady) {
        this.isClientReady = isClientReady;
    }

    /**
     * Checks if both host and client are ready, and starts the game if they are.
     */
    public void checkBothReady() {
        if (isHostReady && isClientReady) {
            gameUi.showMessage(bundle.getString("bothReadyMessage"));
            if (isHost) {
                game.setPlayerTurn(true);
                gameUi.showMessage(bundle.getString("yourTurnMessage"));
                gameUi.showComputerBoard();
                game.enablePvpGamePlay();
            } else {
                game.setPlayerTurn(false);
                gameUi.showMessage(bundle.getString("waitingForHostMessage"));
                gameUi.showPlayerBoard();
            }
        }
    }

    /**
     * Processes the received network message and updates the game state accordingly.
     * 
     * @param message the received network message
     */
    private void processNetworkMessage(String message) {
        if (message.startsWith("CHAT:")) {
            gameUi.receiveChatMessage(bundle.getString("opponentMessage") + message.substring(5));
        } else if (message.startsWith("SHOOT:")) {
            String[] parts = message.substring(6).split(",");

            int x = Integer.parseInt(parts[0]);
            int y = Integer.parseInt(parts[1]);
            boolean isHit = game.checkHit(x, y, game.getPlayerBoard());
            game.markHitOrMiss(x, y, game.getPlayerBoardHits(), isHit);
            gameUi.markPlayerBoard(x, y, isHit ? gameUi.getHitIcon() : gameUi.getMissIcon());
            gameUi.getNetwork().sendMessage("HIT:" + x + "," + y + "," + isHit);
        } else if (message.startsWith("HIT:")) {
            String[] parts = message.substring(4).split(",");
            boolean isHit = Boolean.parseBoolean(parts[2]);
            int x = Integer.parseInt(parts[0]);
            int y = Integer.parseInt(parts[1]);
            game.markHitOrMiss(x, y, game.getComputerBoardHits(), isHit);
            gameUi.markComputerBoard(x, y, isHit ? gameUi.getHitIcon() : gameUi.getMissIcon());
            if (game.checkVictory(game.getPlayerHits())) {
                gameUi.showVictoryMessage();
                gameUi.getNetwork().sendMessage("LOST");
                game.disableGamePlay();
            }
        } else if (message.equals("RESTART")) {
            game.resetGame();
            gameUi.resetUI();
            gameUi.showPveDialog();
            isHostReady = false;
            isClientReady = false;
        } else if (message.equals("LOST")) {
            gameUi.showLossMessage();
            game.disableGamePlay();
        } else if (message.startsWith("READY::")) {
            String name = message.split("::")[1];
            gameUi.showMessage(name + " " + bundle.getString("isReadyMessage"));
            if (isHost) {
                isClientReady = true;
            } else {
                isHostReady = true;
            }
            checkBothReady();
        } else if (message.equals("PLACE_SHIPS")) {
            game.enableShipPlacement();
            gameUi.showMessage(bundle.getString("placeShipsMessage"));
        } else if (message.equals("END_TURN")) {
            game.setPlayerTurn(true);
            game.setHasPlayerMadeMove(false);
            game.enablePvpGamePlay();
            gameUi.showComputerBoard();
            gameUi.showYourTurn();
        } else if (message.equals("DISCONNECT")) {
            disconnect();
        }
    }

    /**
     * Starts a background thread to listen for network messages from the connected peer.
     */
    private void startNetworkListener() {
        new Thread(() -> {
            while (true) {
                try {
                    String message = receiveMessage();
                    if (message != null) {
                        processNetworkMessage(message);
                    }
                } catch (IOException ex) {
                    gameUi.showMessage(bundle.getString("networkErrorMessage") + ex.getMessage());
                    break;
                }
            }
        }).start();
    }
}
