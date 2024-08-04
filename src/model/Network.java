package model;

import java.io.*;
import java.net.*;
import controller.Controller;
import view.GameUi;

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

    public Network(Socket socket, GameUi gameUi, Game game, Controller controller, boolean isHost) throws IOException {
        this.socket = socket;
        this.gameUi = gameUi;
        this.game = game;
        this.controller = controller;
        this.isHost = isHost;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        startNetworkListener();
    }

    public void sendMessage(String message) {
        out.println(message);
    }

    public String receiveMessage() throws IOException {
        return in.readLine();
    }

    public void close() throws IOException {
        in.close();
        out.close();
        socket.close();
    }

    public void setHostReady(boolean isHostReady) {
        this.isHostReady = isHostReady;
    }

    public void setClientReady(boolean isClientReady) {
        this.isClientReady = isClientReady;
    }

    public void checkBothReady() {	/* Gameplay PVP*/
        if (isHostReady && isClientReady) {
            gameUi.showMessage("Both players are ready! Host goes first.");
            if (isHost) {
                game.setPlayerTurn(true);
                gameUi.showMessage("Your turn!");
                gameUi.showComputerBoard();
                game.enableGamePlay();
            } else {
                game.setPlayerTurn(false);
                gameUi.showMessage("Waiting for host's move...");
                gameUi.showPlayerBoard();
            }
           
        }
    }

    private void processNetworkMessage(String message) {
        if (message.startsWith("CHAT:")) {
            gameUi.receiveChatMessage("Opponent: " + message.substring(5));
        } else if (message.startsWith("SHOOT:")) {
            String[] parts = message.substring(6).split(",");

            int x = Integer.parseInt(parts[0]);
            int y = Integer.parseInt(parts[1]);
            if (isHost) {
            	 boolean isHit = game.checkHit(x, y, game.getPlayerBoard());
            	 game.markHitOrMiss(x, y, game.getPlayerBoardHits(), isHit);
                gameUi.markPlayerBoard(x, y, isHit ? gameUi.getHitIcon() : gameUi.getMissIcon());
                gameUi.getNetwork().sendMessage("HIT:" + x + "," + y + "," + isHit);
                
                
            } else {
            	 boolean isHit = game.checkHit(x, y, game.getPlayerBoard());
            	 game.markHitOrMiss(x, y, game.getPlayerBoardHits(), isHit);
                gameUi.markPlayerBoard(x, y, isHit ? gameUi.getHitIcon() : gameUi.getMissIcon());
                gameUi.getNetwork().sendMessage("HIT:" + x + "," + y + "," + isHit);
                
            }
        } else if (message.startsWith("HIT:")) {
           String[] parts = message.substring(4).split(",");
            boolean isHit = Boolean.parseBoolean(parts[2]);
            int x = Integer.parseInt(parts[0]);
            int y = Integer.parseInt(parts[1]);
          if (isHost) {
              game.markHitOrMiss(x, y, game.getComputerBoardHits(), isHit);
              gameUi.markComputerBoard(x, y, isHit ? gameUi.getHitIcon() : gameUi.getMissIcon());
         } else {
            game.markHitOrMiss(x, y, game.getComputerBoardHits(), isHit);
            gameUi.markComputerBoard(x, y, isHit ? gameUi.getHitIcon() : gameUi.getMissIcon());
          }
        }
         else if (message.startsWith("GRESULT:")) {
            boolean hasWon = Boolean.parseBoolean(message.substring(8));
            if (hasWon) {
                gameUi.showLossMessage();
            } else {
                gameUi.showVictoryMessage();
            }
            game.disableGamePlay();
        } else if (message.startsWith("READY::")) {
            String name = message.split("::")[1];
            gameUi.showMessage(name + " is ready!");
            if (isHost) {
                isClientReady = true;
            } else {
                isHostReady = true;
            }
            checkBothReady();
        } else if (message.equals("PLACE_SHIPS")) {
            game.enableShipPlacement();
            gameUi.showMessage("Please place your 5 ships on the board.");
        } else if (message.equals("END_TURN")) {
            game.setPlayerTurn(true);
            game.setHasPlayerMadeMove(false);
            game.enableGamePlay();
            gameUi.showComputerBoard();
            gameUi.showYourTurn();
        }
    }

    private void startNetworkListener() {
        new Thread(() -> {
            while (true) {
                try {
                    String message = receiveMessage();
                    if (message != null) {
                        processNetworkMessage(message);
                    }
                } catch (IOException ex) {
                    gameUi.showMessage("Network error: " + ex.getMessage());
                    break;
                }
            }
        }).start();
    }
}
