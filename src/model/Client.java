package model;

import java.io.IOException;
import java.net.Socket;

public class Client {
    private Socket socket;
    private Network network;

    public Client(String address, int port) throws IOException {
        socket = new Socket(address, port);
        System.out.println("Connected to server");
        network = new Network(socket);
    }

    public void close() throws IOException {
        network.close();
        socket.close();
    }

    public Network getNetwork() {
        return network;
    }
}
