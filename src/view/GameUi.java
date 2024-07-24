package view;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;
import model.*;

public class GameUi extends JFrame {
    private JMenu gameMenu;
    private JMenu languageMenu;
    private JPanel panel;
    private JMenuItem pveItem;
    private JMenu pvpItem;
    private JMenuItem restartItem;
    private JMenuItem exitItem;
    private JMenuItem englishItem;
    private JMenuItem chineseItem;
    private JMenuItem hostItem;
    private JMenuItem connectItem;
    private JMenuItem disconnectItem;
    private JPanel gridPanel;
    private JButton[][] gridButtons;
    private JPanel computerGridPanel;
    private JButton[][] computerGridButtons;
    private JButton rotateButton;
    private JButton startButton;
    private JButton endTurnButton;
    private JButton quitButton;
    private JPanel leftPanel;
    private JPanel rightPanel;
    private JTextArea gameLog;
    private JScrollPane gameLogScrollPane;
    private JLabel chatPart;
    private ImageIcon bowEast;
    private ImageIcon bowNorth;
    private ImageIcon bowSouth;
    private ImageIcon bowWest;
    private ImageIcon midHullHoriz;
    private ImageIcon midHullVert;
    private ImageIcon hitIcon;
    private ImageIcon missIcon;
    private final String[] colLabel = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
    private ResourceBundle bundle;

    public GameUi() {
        showSplashScreen("logo.png", 5000); // 5000 milliseconds (5 seconds)
        initializeComponents();
        loadImages();
        changeLocale(Locale.ENGLISH);
        initializeUI();
    }

    private void showSplashScreen(String imagePath, int duration) {
        JWindow splashScreen = new JWindow();
        ImageIcon imageIcon = new ImageIcon(imagePath);
        JLabel imageLabel = new JLabel(imageIcon);

        splashScreen.getContentPane().add(imageLabel, BorderLayout.CENTER);
        splashScreen.pack();

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = splashScreen.getSize().width;
        int height = splashScreen.getSize().height;
        int x = (screenSize.width - width) / 2;
        int y = (screenSize.height - height) / 2;
        splashScreen.setLocation(x, y);

        splashScreen.setVisible(true);

        try {
            Thread.sleep(duration);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        splashScreen.setVisible(false);
        splashScreen.dispose();
    }

    private void initializeComponents() {
        gameMenu = new JMenu();
        pveItem = new JMenuItem();
        pvpItem = new JMenu();
        restartItem = new JMenuItem();
        exitItem = new JMenuItem();
        languageMenu = new JMenu();
        englishItem = new JMenuItem();
        chineseItem = new JMenuItem();
        panel = new JPanel();
        gridPanel = new JPanel();
        gridButtons = new JButton[10][10];
        computerGridButtons = new JButton[10][10];
        rotateButton = new JButton();
        startButton = new JButton();
        endTurnButton = new JButton();
        quitButton = new JButton();
        leftPanel = new JPanel();
        rightPanel = new JPanel();
        gameLog = new JTextArea();
        gameLogScrollPane = new JScrollPane(gameLog);
        chatPart = new JLabel();
        hostItem = new JMenuItem();
        connectItem = new JMenuItem();
        disconnectItem = new JMenuItem();
    }

    private void initializeUI() {
        setTitle("Battleship");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        changeLocale(Locale.ENGLISH);

        JMenuBar menuBar = new JMenuBar();
        this.setJMenuBar(menuBar);
        gameMenu = new JMenu();
        menuBar.add(gameMenu);

        pveItem = new JMenuItem();
        pvpItem = new JMenu();
        restartItem = new JMenuItem();
        exitItem = new JMenuItem();

        gameMenu.add(pveItem);
        gameMenu.add(pvpItem);
        gameMenu.add(restartItem);
        gameMenu.add(exitItem);

        languageMenu = new JMenu();
        menuBar.add(languageMenu);

        englishItem = new JMenuItem();
        chineseItem = new JMenuItem();

        languageMenu.add(englishItem);
        languageMenu.add(chineseItem);

        hostItem.setText(bundle.getString("hostItem"));
        connectItem.setText(bundle.getString("connectItem"));
        disconnectItem.setText(bundle.getString("disconnectItem"));

        pvpItem.add(hostItem);
        pvpItem.add(connectItem);
        pvpItem.add(disconnectItem);

        panel = new JPanel();
        panel.setBackground(new Color(51, 204, 255));
        panel.setLayout(new BorderLayout());
        ImageIcon logoIcon = new ImageIcon("logo.png");
        JLabel logoLabel = new JLabel(logoIcon);
        panel.add(logoLabel, BorderLayout.NORTH);

        gridPanel = new JPanel(new GridLayout(11, 11, 2, 2));
        gridPanel.setBackground(new Color(51, 204, 255));

        gridButtons = new JButton[10][10];
        computerGridButtons = new JButton[10][10];

        gridPanel.add(new JLabel("")); // Empty top-left corner
        for (String label : colLabel) {
            gridPanel.add(new JLabel(label, SwingConstants.CENTER));
        }

        for (int i = 0; i < 10; i++) {
            gridPanel.add(new JLabel(String.valueOf(i + 1), SwingConstants.CENTER));
            for (int j = 0; j < 10; j++) {
                gridButtons[i][j] = new JButton();
                gridButtons[i][j].setPreferredSize(new Dimension(40, 40));
                gridButtons[i][j].setBackground(new Color(51, 204, 255));
                gridButtons[i][j].setBorder(new LineBorder(Color.WHITE));
                gridPanel.add(gridButtons[i][j]);

                computerGridButtons[i][j] = new JButton();
                computerGridButtons[i][j].setPreferredSize(new Dimension(40, 40));
                computerGridButtons[i][j].setBackground(new Color(51, 204, 255));
                computerGridButtons[i][j].setBorder(new LineBorder(Color.WHITE));
            }
        }

        panel.add(gridPanel, BorderLayout.CENTER);

        // Left Panel
        leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(new Color(0, 51, 90));
        leftPanel.setPreferredSize(new Dimension(150, leftPanel.getPreferredSize().height));
        gameLog.setEditable(false);
        gameLog.setLineWrap(true);
        gameLog.setWrapStyleWord(true);
        gameLog.setForeground(Color.WHITE);
        gameLog.setBackground(new Color(0, 51, 90));
        gameLogScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        leftPanel.add(gameLogScrollPane, BorderLayout.CENTER);
        panel.add(leftPanel, BorderLayout.WEST);

        // Right Panel
        rightPanel = new JPanel();
        rightPanel.setBackground(new Color(0, 51, 90));
        rightPanel.setPreferredSize(new Dimension(150, rightPanel.getPreferredSize().height));
        chatPart = new JLabel();
        chatPart.setForeground(Color.WHITE);
        rightPanel.add(chatPart);
        panel.add(rightPanel, BorderLayout.EAST);

        JPanel bottomPanel = new JPanel(new GridLayout(1, 4, 10, 0));
        bottomPanel.setBackground(new Color(0, 51, 102));

        rotateButton = new JButton();
        endTurnButton = new JButton();
        startButton = new JButton();
        quitButton = new JButton();

        bottomPanel.add(rotateButton);
        bottomPanel.add(startButton);
        bottomPanel.add(endTurnButton);
        bottomPanel.add(quitButton);

        panel.add(bottomPanel, BorderLayout.SOUTH);

        setResizable(false);
        this.add(panel);

        pack();

        updateText();
    }

    public void resetUI() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                gridButtons[i][j].setIcon(null);
                computerGridButtons[i][j].setIcon(null);
            }
        }

        showPlayerBoard();
        getStartButton().setEnabled(true);
    }

    private void loadImages() {
        bowEast = new ImageIcon(new ImageIcon("bow_east.png").getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH));
        bowNorth = new ImageIcon(new ImageIcon("bow_north.png").getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH));
        bowSouth = new ImageIcon(new ImageIcon("bow_south.png").getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH));
        bowWest = new ImageIcon(new ImageIcon("bow_west.png").getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH));
        midHullHoriz = new ImageIcon(new ImageIcon("midhull_horiz.png").getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH));
        midHullVert = new ImageIcon(new ImageIcon("midhull_vert.png").getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH));
        hitIcon = new ImageIcon(new ImageIcon("hit.png").getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH));
        missIcon = new ImageIcon(new ImageIcon("miss.png").getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH));
    }

    public void changeLocale(Locale locale) {
        try {
            Locale.setDefault(locale);
            bundle = ResourceBundle.getBundle("MessagesBundle", locale);
            updateText();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateText() {
        try {
            gameMenu.setText(bundle.getString("gameMenu"));
            pveItem.setText(bundle.getString("pveItem"));
            pvpItem.setText(bundle.getString("pvpItem"));
            restartItem.setText(bundle.getString("restartItem"));
            exitItem.setText(bundle.getString("exitItem"));
            languageMenu.setText(bundle.getString("languageMenu"));
            englishItem.setText(bundle.getString("englishItem"));
            chineseItem.setText(bundle.getString("chineseItem"));
            rotateButton.setText(bundle.getString("rotateButton"));
            startButton.setText(bundle.getString("startButton"));
            endTurnButton.setText(bundle.getString("endTurnButton"));
            quitButton.setText(bundle.getString("quitButton"));
            chatPart.setText(bundle.getString("chatPart"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showMenu() {
        setVisible(true);
        JOptionPane.showMessageDialog(this, bundle.getString("showMenuMessage"));
    }

    public void showPveDialog() {
        JOptionPane.showMessageDialog(this, bundle.getString("showPveDialogMessage"));
    }

    public void showYourTurn() {
        JOptionPane.showMessageDialog(this, bundle.getString("showYourTurnMessage"));
    }

    public void placeShipPart(int x, int y, ImageIcon icon) {
        gridButtons[x][y].setIcon(icon);
    }

    public void markPlayerBoard(int x, int y, ImageIcon icon) {
        gridButtons[x][y].setIcon(icon);
    }

    public void markComputerBoard(int x, int y, ImageIcon icon) {
        computerGridButtons[x][y].setIcon(icon);
    }

    public void showComputerBoard() {
        panel.remove(gridPanel);
        computerGridPanel = new JPanel(new GridLayout(11, 11, 2, 2));
        computerGridPanel.setBackground(new Color(51, 204, 255));

        computerGridPanel.add(new JLabel("")); // Empty top-left corner
        for (String label : colLabel) {
            computerGridPanel.add(new JLabel(label, SwingConstants.CENTER));
        }

        for (int i = 0; i < 10; i++) {
            computerGridPanel.add(new JLabel(String.valueOf(i + 1), SwingConstants.CENTER));
            for (int j = 0; j < 10; j++) {
                computerGridPanel.add(computerGridButtons[i][j]);
            }
        }

        panel.add(computerGridPanel, BorderLayout.CENTER);
        panel.revalidate();
        panel.repaint();
    }

    public void showPlayerBoard() {
        panel.remove(computerGridPanel);
        panel.add(gridPanel, BorderLayout.CENTER);
        panel.revalidate();
        panel.repaint();
    }

    public JMenuItem getPveItem() {
        return pveItem;
    }

    public JMenuItem getPvpItem() {
        return pvpItem;
    }

    public JMenuItem getRestartItem() {
        return restartItem;
    }

    public JMenuItem getExitItem() {
        return exitItem;
    }

    public JMenuItem getEnglishItem() {
        return englishItem;
    }

    public JMenuItem getChineseItem() {
        return chineseItem;
    }

    public JMenuItem getHostItem() {
        return hostItem;
    }

    public JMenuItem getConnectItem() {
        return connectItem;
    }

    public JMenuItem getDisconnectItem() {
        return disconnectItem;
    }

    public JButton getRotateButton() {
        return rotateButton;
    }

    public JButton getStartButton() {
        return startButton;
    }

    public JButton getEndTurnButton() {
        return endTurnButton;
    }

    public JButton getQuitButton() {
        return quitButton;
    }

    public ImageIcon getBowEast() {
        return bowEast;
    }

    public ImageIcon getBowNorth() {
        return bowNorth;
    }

    public ImageIcon getBowSouth() {
        return bowSouth;
    }

    public ImageIcon getBowWest() {
        return bowWest;
    }

    public ImageIcon getMidHullHoriz() {
        return midHullHoriz;
    }

    public ImageIcon getMidHullVert() {
        return midHullVert;
    }

    public ImageIcon getHitIcon() {
        return hitIcon;
    }

    public ImageIcon getMissIcon() {
        return missIcon;
    }

    public JButton[][] getGridButtons() {
        return gridButtons;
    }

    public JButton[][] getComputerGridButtons() {
        return computerGridButtons;
    }

    public void showRotationMessage(boolean isVertical) {
        JOptionPane.showMessageDialog(this, bundle.getString("showRotationMessage") + (isVertical ? bundle.getString("vertical") : bundle.getString("horizontal")));
    }

    public void showPlaceAllShipsMessage() {
        JOptionPane.showMessageDialog(this, bundle.getString("showPlaceAllShipsMessage"));
    }

    public void showAllShipsPlacedMessage() {
        JOptionPane.showMessageDialog(this, bundle.getString("showAllShipsPlacedMessage"));
    }

    public void showCannotPlaceShipMessage() {
        JOptionPane.showMessageDialog(this, bundle.getString("showCannotPlaceShipMessage"));
    }

    public void showVictoryMessage() {
        JOptionPane.showMessageDialog(this, bundle.getString("showVictoryMessage"));
    }

    public void showLossMessage() {
        JOptionPane.showMessageDialog(this, bundle.getString("showLossMessage"));
    }

    public void showCannotGoTwiceMessage() {
        JOptionPane.showMessageDialog(this, bundle.getString("showCannotGoTwiceMessage"));
    }

    public void exitGame() {
        System.exit(0);
    }

    public void showMessage(String message) {
        gameLog.append(message + "\n");
        gameLog.setCaretPosition(gameLog.getDocument().getLength()); // Scroll to the bottom
    }

    public void showHostDialog(JDialog hostDialog) {
        hostDialog.setLayout(new GridLayout(4, 2));

        hostDialog.add(new JLabel("Name:"));
        JTextField nameField = new JTextField();
        hostDialog.add(nameField);

        hostDialog.add(new JLabel("Port:"));
        Integer[] ports = {8080, 8081, 8082, 8083};
        JComboBox<Integer> portBox = new JComboBox<>(ports);
        hostDialog.add(portBox);

        hostDialog.add(new JLabel("Status:"));
        JLabel statusLabel = new JLabel("Waiting...");
        hostDialog.add(statusLabel);

        JButton hostButton = new JButton("Host");
        hostDialog.add(hostButton);
        JButton cancelButton = new JButton("Cancel");
        hostDialog.add(cancelButton);

        hostDialog.pack();
        hostDialog.setLocationRelativeTo(this);

        hostDialog.setVisible(true);

        addHostDialogListeners(hostDialog, nameField, portBox, statusLabel, hostButton, cancelButton);
    }

    public void showConnectDialog(JDialog connectDialog) {
        connectDialog.setLayout(new GridLayout(5, 2));

        connectDialog.add(new JLabel("Name:"));
        JTextField nameField = new JTextField();
        connectDialog.add(nameField);

        connectDialog.add(new JLabel("Address:"));
        JTextField addressField = new JTextField();
        connectDialog.add(addressField);

        connectDialog.add(new JLabel("Port:"));
        Integer[] ports = {8080, 8081, 8082, 8083};
        JComboBox<Integer> portBox = new JComboBox<>(ports);
        connectDialog.add(portBox);

        connectDialog.add(new JLabel("Status:"));
        JLabel statusLabel = new JLabel("Not connected");
        connectDialog.add(statusLabel);

        JButton connectButton = new JButton("Connect");
        connectDialog.add(connectButton);
        JButton cancelButton = new JButton("Cancel");
        connectDialog.add(cancelButton);

        connectDialog.pack();
        connectDialog.setLocationRelativeTo(this);

        connectDialog.setVisible(true);

        addConnectDialogListeners(connectDialog, nameField, addressField, portBox, statusLabel, connectButton, cancelButton);
    }

    public void addHostDialogListeners(JDialog hostDialog, JTextField nameField, JComboBox<Integer> portBox, JLabel statusLabel, JButton hostButton, JButton cancelButton) {
        hostButton.addActionListener(e -> {
            String playerName = nameField.getText();
            int port = (int) portBox.getSelectedItem();
            try {
                Host host = new Host(port);
                host.startServer();
                Network network = new Network(host.getClientSocket());
                showMessage("Player 1 (Host): " + playerName);
                statusLabel.setText("Hosting on port " + port);
                hostDialog.dispose();
            } catch (IOException ex) {
                statusLabel.setText("Failed to host on port " + port);
                ex.printStackTrace();
            }
        });

        cancelButton.addActionListener(e -> hostDialog.dispose());
    }

    public void addConnectDialogListeners(JDialog connectDialog, JTextField nameField, JTextField addressField, JComboBox<Integer> portBox, JLabel statusLabel, JButton connectButton, JButton cancelButton) {
        connectButton.addActionListener(e -> {
            String playerName = nameField.getText();
            String address = addressField.getText();
            int port = (int) portBox.getSelectedItem();
            try {
                Client client = new Client(address, port);
                client.connectToServer();
                Network network = new Network(client.getSocket());
                showMessage("Player 2: " + playerName);
                statusLabel.setText("Connected to " + address + ":" + port);
                connectDialog.dispose();
            } catch (IOException ex) {
                statusLabel.setText("Failed to connect to " + address + ":" + port);
                ex.printStackTrace();
            }
        });

        cancelButton.addActionListener(e -> connectDialog.dispose());
    }
}
