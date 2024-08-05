package model;

import java.io.IOException;
import java.net.Socket;

/**
 * The Client class represents the client side of a network connection in the Battleship game.
 * It handles the setup and management of the client socket.
 */
public class Client {
    private Socket clientSocket;

    /**
     * Connects the client socket to the specified server address and port.
     * 
     * @param address the server address to connect to
     * @param port the server port to connect to
     * @throws IOException if an I/O error occurs when attempting to connect to the server
     */
    public void connect(String address, int port) throws IOException {
        clientSocket = new Socket(address, port);
    }

    /**
     * Gets the client socket.
     * 
     * @return the client socket
     */
    public Socket getSocket() {
        return clientSocket;
    }

    /**
     * Disconnects the client socket, closing it and releasing any resources associated with it.
     * 
     * @throws IOException if an I/O error occurs when closing the socket
     */
    public void disconnect() throws IOException {
        clientSocket.close();
    }
}
