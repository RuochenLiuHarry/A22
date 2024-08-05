package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * CustomDialog is a dialog window for setting up network connections in the Battleship game.
 * It allows the user to enter the address, port, and player name.
 */
public class CustomDialog extends JDialog {
    private JTextField addressField;
    private JTextField portField;
    private JTextField playerNameField;
    private String address;
    private int port;
    private String playerName;
    private boolean isHost;
    private ResourceBundle bundle;

    /**
     * Constructor for the CustomDialog class.
     * Initializes the dialog with the specified owner and host mode.
     * 
     * @param owner the parent frame
     * @param isHost true if the dialog is for the host, false if for the client
     */
    public CustomDialog(Frame owner, boolean isHost) {
        super(owner, "", true);
        this.isHost = isHost;
        this.bundle = ResourceBundle.getBundle("MessagesBundle", Locale.getDefault());
        setTitle(bundle.getString("networkSetupTitle"));
        initializeUI();
    }

    /**
     * Initializes the UI components of the dialog.
     * Sets up the text fields for address, port, and player name, and adds OK and Cancel buttons.
     */
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

    /**
     * Gets the address entered by the user.
     * 
     * @return the address as a String
     */
    public String getAddress() {
        return address;
    }

    /**
     * Gets the port entered by the user.
     * 
     * @return the port as an int
     */
    public int getPort() {
        return port;
    }

    /**
     * Gets the player name entered by the user.
     * 
     * @return the player name as a String
     */
    public String getPlayerName() {
        return playerName;
    }
}
