package view;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class CustomDialog extends JDialog implements ActionListener {
    private JTextField addressInput;
    private JTextField portInput;
    private JTextField nameInput;
    private JButton button;
    private JButton cancelButton;
    private boolean isHost;

    public CustomDialog(JFrame mainView, boolean isHost) {
        super(mainView, isHost ? "Enter Host Details" : "Enter Client Details", true);
        this.isHost = isHost;
        Container networkPanel = getContentPane();
        JPanel innerPanel = new JPanel();
        innerPanel.setLayout(new FlowLayout());

        innerPanel.add(new JLabel("Name:"));
        nameInput = new JTextField(20);
        innerPanel.add(nameInput);

        if (!isHost) {
            innerPanel.add(new JLabel("Address:"));
            addressInput = new JTextField(20);
            innerPanel.add(addressInput);
        }

        innerPanel.add(new JLabel("Port:"));
        portInput = new JTextField(5);
        innerPanel.add(portInput);

        networkPanel.add(innerPanel);

        button = new JButton(isHost ? "Host" : "Connect");
        button.addActionListener(this);
        innerPanel.add(button);

        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> setVisible(false));
        innerPanel.add(cancelButton);

        pack();
    }

    public String getAddress() {
        return addressInput != null ? addressInput.getText() : null;
    }

    public String getPort() {
        return portInput.getText();
    }

    public String getName() {
        return nameInput.getText();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        setVisible(false);
    }
}
