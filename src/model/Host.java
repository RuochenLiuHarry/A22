package model;


import java.io.*;
import java.net.*;

public class Host {
    private ServerSocket serverSocket;

    public void start(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }

    public Socket accept() throws IOException {
        return serverSocket.accept();
    }

    public void stop() throws IOException {
        serverSocket.close();
    }
}