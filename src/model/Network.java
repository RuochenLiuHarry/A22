package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.JOptionPane;
import controller.Controller;
import view.CustomDialog;
import view.GameUi;

public class Network extends Thread {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private Controller controller;
    private GameUi gameUi;

    public Network(Controller controller, GameUi gameUi) {
        this.controller = controller;
        this.gameUi = gameUi;
    }

    public Network(Socket socket, Controller controller, GameUi gameUi) {
        this.socket = socket;
        this.controller = controller;
        this.gameUi = gameUi;
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                processMessage(inputLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        out.println(message);
    }

    private void processMessage(String message) {
        String[] parts = message.split("::");
        String protocol = parts[0];

        switch (protocol) {
            case "READY":
                boolean ready = Boolean.parseBoolean(parts[1]);
                // Handle ready message
                break;
            case "USERNAME":
                String username = parts[1];
                // Process username message
                break;
            case "HIT":
                boolean hit = Boolean.parseBoolean(parts[1]);
                // Process hit result message
                break;
            case "SHOOT":
                int x = Integer.parseInt(parts[1]);
                int y = Integer.parseInt(parts[2]);
                // Process shoot message
                break;
            case "GRESULT":
                boolean win = Boolean.parseBoolean(parts[1]);
                // Process game result message
                break;
            case "RESET":
                // Process reset message
                break;
            case "CHAT":
                String chatMessage = parts[1];
                controller.showChatMessage(chatMessage);
                break;
        }
    }

    public void startHost() {
        CustomDialog dialog = new CustomDialog(gameUi, true);
        dialog.setVisible(true);
        String portStr = dialog.getPort();
        int port = Integer.parseInt(portStr);

        new Thread(() -> {
            try {
                Host host = new Host(controller, gameUi);
                host.start(port);
                Network network = new Network(new Socket("localhost", port), controller, gameUi);
                network.start();
                gameUi.showMessage("Hosting game. Waiting for client to connect...");
            } catch (IOException e) {
                e.printStackTrace();
                gameUi.showMessage("Failed to start host.");
            }
        }).start();
    }

    public void startClient() {
        CustomDialog dialog = new CustomDialog(gameUi, false);
        dialog.setVisible(true);
        String address = dialog.getAddress();
        String portStr = dialog.getPort();
        int port = Integer.parseInt(portStr);

        try {
            Client client = new Client();
            client.connect(address, port, controller, gameUi);
            gameUi.showMessage("Connected to host.");
            client.getNetwork().sendMessage("USERNAME::" + dialog.getName());
        } catch (IOException e) {
            e.printStackTrace();
            gameUi.showMessage("Failed to connect to host.");
        }
    }
}
