package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;
import java.util.ResourceBundle;

public class CustomDialog extends JDialog {
    private JTextField addressField;
    private JTextField portField;
    private JTextField playerNameField;
    private String address;
    private int port;
    private String playerName;
    private boolean isHost;
    private ResourceBundle bundle;

    public CustomDialog(Frame owner, boolean isHost) {
        super(owner, "", true);
        this.isHost = isHost;
        this.bundle = ResourceBundle.getBundle("MessagesBundle", Locale.getDefault());
        setTitle(bundle.getString("networkSetupTitle"));
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridLayout(3, 2));
        panel.add(new JLabel(bundle.getString("addressLabel")));
        addressField = new JTextField();
        panel.add(addressField);

        panel.add(new JLabel(bundle.getString("portLabel")));
        portField = new JTextField();
        panel.add(portField);

        panel.add(new JLabel(bundle.getString("nameLabel")));
        playerNameField = new JTextField();
        panel.add(playerNameField);

        if (isHost) {
            addressField.setEditable(false);
        }

        add(panel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton okButton = new JButton(bundle.getString("okButton"));
        JButton cancelButton = new JButton(bundle.getString("cancelButton"));

        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

        add(buttonPanel, BorderLayout.SOUTH);

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                address = addressField.getText().trim();
                port = Integer.parseInt(portField.getText().trim());
                playerName = playerNameField.getText().trim();
                setVisible(false);
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                address = null;
                port = 0;
                playerName = null;
                setVisible(false);
            }
        });

        pack();
        setLocationRelativeTo(getOwner());
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
