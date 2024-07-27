package model;

import java.io.IOException;
import java.net.Socket;
import controller.Controller;
import view.GameUi;

public class Client {
    private Socket socket;
    private Network network;

    public void connect(String hostname, int port, Controller controller, GameUi gameUi) throws IOException {
        socket = new Socket(hostname, port);
        System.out.println("Connected to server: " + hostname + ":" + port);
        network = new Network(socket, controller, gameUi);
        network.start();
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
