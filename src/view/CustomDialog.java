package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CustomDialog extends JDialog {
    private JTextField addressField;
    private JTextField portField;
    private JTextField nameField;
    private JButton okButton;
    private JButton cancelButton;
    private String address;
    private int port;
    private String playerName;
    private boolean isHost;

    public CustomDialog(JFrame parent, boolean isHost) {
        super(parent, "Network Setup", true);
        this.isHost = isHost;
        setupUI();
        setupListeners();
    }

    private void setupUI() {
        addressField = new JTextField(20);
        portField = new JTextField(5);
        nameField = new JTextField(20);
        okButton = new JButton("OK");
        cancelButton = new JButton("Cancel");

        setLayout(new GridLayout(5, 2));

        add(new JLabel("Address:"));
        add(addressField);
        add(new JLabel("Port:"));
        add(portField);
        add(new JLabel("Name:"));
        add(nameField);
        add(okButton);
        add(cancelButton);

        pack();
    }

    private void setupListeners() {
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                address = addressField.getText();
                port = Integer.parseInt(portField.getText());
                playerName = nameField.getText();
                setVisible(false);
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                address = null;
                port = -1;
                playerName = null;
                setVisible(false);
            }
        });
    }

    public String getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }

    public String getPlayerName() {
        return playerName;
    }
}
