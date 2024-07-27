package view;

import javax.swing.*;

import model.Client;
import model.Host;
import model.Network;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class CustomDialog extends JDialog {
    private JTextField nameField, addressField, portField;
    private JLabel statusLabel;
    private JButton connectButton, hostButton, cancelButton;
    private boolean isHost;
    private Network network;

    public CustomDialog(JFrame parent) {
        super(parent, true);
        setTitle("Network Connection");
        setLayout(new GridLayout(5, 2));

        add(new JLabel("Name:"));
        nameField = new JTextField();
        add(nameField);

        add(new JLabel("Address:"));
        addressField = new JTextField();
        add(addressField);

        add(new JLabel("Port:"));
        portField = new JTextField();
        add(portField);

        statusLabel = new JLabel("Status:");
        add(statusLabel);

        connectButton = new JButton("Connect");
        connectButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String address = addressField.getText();
                    int port = Integer.parseInt(portField.getText());
                    Client client = new Client(address, port);
                    network = client.getNetwork();
                    isHost = false;
                    setVisible(false);
                } catch (IOException ex) {
                    statusLabel.setText("Status: Connection failed");
                }
            }
        });
        add(connectButton);

        hostButton = new JButton("Host");
        hostButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    int port = Integer.parseInt(portField.getText());
                    Host host = new Host(port);
                    network = host.getNetwork();
                    isHost = true;
                    setVisible(false);
                } catch (IOException ex) {
                    statusLabel.setText("Status: Hosting failed");
                }
            }
        });
        add(hostButton);

        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                network = null;
                setVisible(false);
            }
        });
        add(cancelButton);

        pack();
        setLocationRelativeTo(parent);
    }

    public Network getNetwork() {
        return network;
    }

    public boolean isHost() {
        return isHost;
    }

    public String getName() {
        return nameField.getText();
    }
}
