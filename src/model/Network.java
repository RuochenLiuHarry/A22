package model;

import java.io.*;
import java.net.Socket;

public class Network {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public Network(Socket socket) throws IOException {
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
    }

    public void sendMessage(String message) {
        out.println(message);
    }

    public String receiveMessage() throws IOException {
        return in.readLine();
    }

    public void closeConnection() throws IOException {
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
        if (in != null) {
            in.close();
        }
        if (out != null) {
            out.close();
        }
    }
}
