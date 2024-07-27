package model;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Host {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private Network network;

    public Host(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        System.out.println("Server started on port: " + port);
        clientSocket = serverSocket.accept();
        System.out.println("Client connected");
        network = new Network(clientSocket);
    }

    public void close() throws IOException {
        network.close();
        clientSocket.close();
        serverSocket.close();
    }

    public Network getNetwork() {
        return network;
    }
}
