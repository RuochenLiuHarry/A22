package model;

import java.io.IOException;
import java.net.Socket;
import javax.swing.JOptionPane;
import controller.Controller;
import view.CustomDialog;
import view.GameUi;

public class Network extends Thread {
    private Socket socket;
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
    }

    @Override
    public void run() {
        // Implementation remains the same
    }

    public void sendMessage(String message) {
        // Implementation remains the same
    }

    private void processMessage(String message) {
        // Implementation remains the same
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
