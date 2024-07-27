package model;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import controller.Controller;
import view.GameUi;

public class Host {
    private ServerSocket serverSocket;
    private Controller controller;
    private GameUi gameUi;

    public Host(Controller controller, GameUi gameUi) {
        this.controller = controller;
        this.gameUi = gameUi;
    }

    public void start(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        System.out.println("Server started on port " + port);

        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected: " + clientSocket.getInetAddress());
            Network network = new Network(clientSocket, controller, gameUi);
            network.start();
        }
    }

    public void stop() throws IOException {
        if (serverSocket != null && !serverSocket.isClosed()) {
            serverSocket.close();
        }
    }
}
