package model;

import java.io.IOException;
import java.net.Socket;

public class Client {
    private Socket socket;
    private String hostAddress;
    private int port;

    public Client(String hostAddress, int port) {
        this.hostAddress = hostAddress;
        this.port = port;
    }

    public void connectToServer() throws IOException {
        socket = new Socket(hostAddress, port);
    }

    public Socket getSocket() {
        return socket;
    }

    public void disconnect() throws IOException {
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
    }
}
