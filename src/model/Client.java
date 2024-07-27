package model;

import java.io.IOException;
import java.net.Socket;
import controller.Controller;
import view.GameUi;

public class Client {
    private Socket socket;
    private Network network;

    public void connect(String hostname, int port, Controller controller, GameUi gameUi) throws IOException {
        try {
            socket = new Socket(hostname, port);
            System.out.println("Connected to server: " + hostname + ":" + port);
            network = new Network(socket, controller, gameUi);
            network.start();
        } catch (IOException e) {
            System.err.println("Failed to connect to server: " + hostname + ":" + port);
            e.printStackTrace();
            throw e;
        }
    }

    public void disconnect() throws IOException {    
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
    }

    public Network getNetwork() {
        return network;
    }
}
