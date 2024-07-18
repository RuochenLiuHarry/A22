package model;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Host {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private int port;

    public Host(int port) {
        this.port = port;
    }

    public void startServer() throws IOException {
        serverSocket = new ServerSocket(port);
        clientSocket = serverSocket.accept(); // This will wait until a client connects
    }

    public Socket getClientSocket() {
        return clientSocket;
    }

    public void stopServer() throws IOException {
        if (clientSocket != null && !clientSocket.isClosed()) {
            clientSocket.close();
        }
        if (serverSocket != null && !serverSocket.isClosed()) {
            serverSocket.close();
        }
    }
}
