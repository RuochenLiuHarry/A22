package model;

import java.io.*;
import java.net.*;

public class Client {
    private Socket clientSocket;

    public void connect(String address, int port) throws IOException {
        clientSocket = new Socket(address, port);
    }

    public Socket getSocket() {
        return clientSocket;
    }

    public void disconnect() throws IOException {
        clientSocket.close();
    }
}
