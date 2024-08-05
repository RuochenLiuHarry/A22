package model;

import java.io.*;
import java.net.*;

/**
 * The Host class represents the server side of a network connection in the Battleship game.
 * It handles the setup and management of the server socket.
 */
public class Host {
    private ServerSocket serverSocket;

    /**
     * Starts the server socket on the specified port.
     * 
     * @param port the port number to bind the server socket to
     * @throws IOException if an I/O error occurs when opening the socket
     */
    public void start(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }

    /**
     * Accepts a connection from a client socket.
     * 
     * @return the client socket that connects to this server
     * @throws IOException if an I/O error occurs when waiting for a connection
     */
    public Socket accept() throws IOException {
        return serverSocket.accept();
    }

    /**
     * Stops the server socket, closing it and releasing any resources associated with it.
     * 
     * @throws IOException if an I/O error occurs when closing the socket
     */
    public void stop() throws IOException {
        serverSocket.close();
    }
}
