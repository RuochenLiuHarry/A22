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
        System.out.println("Server started on port: " + port);
        clientSocket = serverSocket.accept(); // This will wait until a client connects
        System.out.println("Client connected from " + clientSocket.getInetAddress().getHostAddress());
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

    public static void main(String[] args) {
        try {
            Host host = new Host(8080); // Replace with your desired port
            host.startServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
