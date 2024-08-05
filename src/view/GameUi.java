package view;

import java.awt.*;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.*;
import javax.swing.border.LineBorder;
import model.Network;

/**
 * GameUi is the main user interface class for the Battleship game. 
 * It manages the game board, menus, and user interactions.
 */
public class GameUi extends JFrame {

    // Components declaration
    private JMenu gameMenu;
    private JMenu languageMenu;
    private JPanel panel;
    private JMenuItem pveItem;
    private JMenu pvpMenu;
    private JMenuItem hostItem;
    private JMenuItem connectItem;
    private JMenuItem disconnectItem;
    private JMenuItem restartItem;
    private JMenuItem exitItem;
    private JMenuItem englishItem;
    private JMenuItem chineseItem;
    private JPanel gridPanel; // player & host
    private JButton[][] gridButtons; // player & host
    private JPanel computerGridPanel;  // computer
    private JButton[][] computerGridButtons; // computer
    
    private JPanel clientGridPanel; // client
    private JButton[][] clientGridButtons; // client
    
    private JButton rotateButton;
    private JButton startButton;
    private JButton endTurnButton;
    private JButton quitButton;
    private JPanel leftPanel;
    private JScrollPane leftPanelScrollPane;
    private JTextArea gameLog;
    private JPanel rightPanel;
    private JTextArea chatArea;
    private JTextField chatInput;
    private JScrollPane chatScrollPane;
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
    private JTextArea chatArea1;
    private JTextField chatField;
    private JButton sendButton;
    private Network network;
    private boolean isPlayerTurn;
    private String playerName;

    /**
     * Constructor for the GameUi class. Initializes the UI components, loads images,
     * sets the default locale to English, and displays the splash screen.
     */
    public GameUi() {
        showSplashScreen("logo.png", 2000);
        initializeComponents();
        loadImages();
        changeLocale(Locale.ENGLISH);
        initializeUI();
        initializeChatComponents();
    }

    /**
     * Initializes chat components, sets up the chat area, chat input field, and send button.
     * Adds an action listener to the send button to handle sending chat messages.
     */
    private void initializeChatComponents() {
        chatArea1 = new JTextArea();
        chatField = new JTextField(20);
        sendButton = new JButton("Send");

        rightPanel.add(new JScrollPane(chatArea1), BorderLayout.CENTER);
        JPanel chatInputPanel = new JPanel(new BorderLayout());
        chatInputPanel.add(chatField, BorderLayout.CENTER);
        chatInputPanel.add(sendButton, BorderLayout.EAST);
        rightPanel.add(chatInputPanel, BorderLayout.SOUTH);

        sendButton.addActionListener(e -> {
            String message = chatField.getText();
            if (!message.isEmpty() && network != null) {
                network.sendMessage("CHAT:" + message);
                chatArea1.append("You: " + message + "\n");
                chatField.setText("");
            }
        });
    }

    /**
     * Sets the network instance for the game UI.
     * @param network the Network instance
     */
    public void setNetwork(Network network) {
        this.network = network;
    }

    /**
     * Gets the network instance of the game UI.
     * @return the Network instance
     */
    public Network getNetwork() {
        return network;
    }

    /**
     * Sets whether it is the player's turn.
     * @param isPlayerTurn true if it is the player's turn, false otherwise
     */
    public void setPlayerTurn(boolean isPlayerTurn) {
        this.isPlayerTurn = isPlayerTurn;
    }

    /**
     * Sets the player's name.
     * @param playerName the player's name
     */
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    /**
     * Gets the player's name.
     * @return the player's name
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     * Receives a chat message and appends it to the chat area.
     * @param message the received chat message
     */
    public void receiveChatMessage(String message) {
        chatArea1.append(message + "\n");
    }

    /**
     * Displays the splash screen with the specified image and duration.
     * @param imagePath the path to the splash screen image
     * @param duration the duration to display the splash screen in milliseconds
     */
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

    /**
     * Initializes the UI components of the game.
     */
    private void initializeComponents() {
        gameMenu = new JMenu();
        pveItem = new JMenuItem();
        pvpMenu = new JMenu();
        hostItem = new JMenuItem();
        connectItem = new JMenuItem();
        disconnectItem = new JMenuItem();
        restartItem = new JMenuItem();
        exitItem = new JMenuItem();
        languageMenu = new JMenu();
        englishItem = new JMenuItem();
        chineseItem = new JMenuItem();
        panel = new JPanel();
        gridPanel = new JPanel();
        gridButtons = new JButton[10][10];
        computerGridButtons = new JButton[10][10];
        computerGridPanel = new JPanel(new GridLayout(11, 11, 2, 2));
        rotateButton = new JButton();
        startButton = new JButton();
        endTurnButton = new JButton();
        quitButton = new JButton();
        leftPanel = new JPanel();
        rightPanel = new JPanel();
        gameLog = new JTextArea();
        leftPanelScrollPane = new JScrollPane(gameLog);
        chatArea = new JTextArea();
        chatInput = new JTextField();
        chatScrollPane = new JScrollPane(chatArea);
    }

    /**
     * Initializes the main UI of the game, including menus, panels, and buttons.
     * Sets up the layout and adds components to the main panel.
     */
    private void initializeUI() {
        setTitle("Battleship");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        changeLocale(Locale.ENGLISH);

        JMenuBar menuBar = new JMenuBar();
        this.setJMenuBar(menuBar);
        gameMenu = new JMenu();
        menuBar.add(gameMenu);

        pveItem = new JMenuItem();
        pvpMenu = new JMenu();
        restartItem = new JMenuItem();
        exitItem = new JMenuItem();

        gameMenu.add(pveItem);
        gameMenu.add(pvpMenu);
        pvpMenu.add(hostItem);
        pvpMenu.add(connectItem);
        pvpMenu.add(disconnectItem);
        gameMenu.add(restartItem);
        gameMenu.add(exitItem);

        languageMenu = new JMenu();
        menuBar.add(languageMenu);

        englishItem = new JMenuItem();
        chineseItem = new JMenuItem();

        languageMenu.add(englishItem);
        languageMenu.add(chineseItem);

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

        leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(new Color(0, 51, 90));
        leftPanel.setPreferredSize(new Dimension(150, leftPanel.getPreferredSize().height));

        gameLog.setEditable(false);
        gameLog.setLineWrap(true);
        gameLog.setWrapStyleWord(true);
        gameLog.setForeground(Color.WHITE);
        gameLog.setBackground(new Color(0, 51, 90));
        leftPanelScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        leftPanel.add(leftPanelScrollPane, BorderLayout.CENTER);

        panel.add(leftPanel, BorderLayout.WEST);

        rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(new Color(0, 51, 90));
        rightPanel.setPreferredSize(new Dimension(150, rightPanel.getPreferredSize().height));

        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        chatArea.setForeground(Color.WHITE);
        chatArea.setBackground(new Color(0, 51, 90));
        chatScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        rightPanel.add(chatScrollPane, BorderLayout.CENTER);

        chatInput.setBackground(Color.WHITE);
        rightPanel.add(chatInput, BorderLayout.SOUTH);

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

    /**
     * Resets the UI by clearing the game board and re-enabling the start button.
     */
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

    /**
     * Loads the images used for the game pieces, such as ships and hit/miss markers.
     */
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

    /**
     * Changes the locale of the game UI and updates the text to match the selected locale.
     * @param locale the new Locale to set
     */
    public void changeLocale(Locale locale) {
        try {
            Locale.setDefault(locale);
            bundle = ResourceBundle.getBundle("MessagesBundle", locale);
            System.out.println("Loaded resource bundle for locale: " + locale);
            updateText();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading resource bundle for locale: " + locale);
        }
    }

    /**
     * Updates the text of all UI components to match the current locale.
     */
    public void updateText() {
        try {
            gameMenu.setText(bundle.getString("gameMenu"));
            pveItem.setText(bundle.getString("pveItem"));
            pvpMenu.setText(bundle.getString("pvpItem"));
            hostItem.setText(bundle.getString("hostItem"));
            connectItem.setText(bundle.getString("connectItem"));
            disconnectItem.setText(bundle.getString("disconnectItem"));
            restartItem.setText(bundle.getString("restartItem"));
            exitItem.setText(bundle.getString("exitItem"));
            languageMenu.setText(bundle.getString("languageMenu"));
            englishItem.setText(bundle.getString("englishItem"));
            chineseItem.setText(bundle.getString("chineseItem"));
            rotateButton.setText(bundle.getString("rotateButton"));
            startButton.setText(bundle.getString("startButton"));
            endTurnButton.setText(bundle.getString("endTurnButton"));
            quitButton.setText(bundle.getString("quitButton"));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error updating text for locale: " + Locale.getDefault());
        }
    }

    /**
     * Displays the main menu of the game.
     */
    public void showMenu() {
        setVisible(true);
        JOptionPane.showMessageDialog(this, bundle.getString("showMenuMessage"));
    }

    /**
     * Displays the Player vs. Environment (PvE) dialog.
     */
    public void showPveDialog() {
        showMessage(bundle.getString("showPveDialogMessage"));
    }

    /**
     * Displays a message indicating it is the player's turn.
     */
    public void showYourTurn() {
        showMessage(bundle.getString("showYourTurnMessage"));
    }

    /**
     * Places a ship part at the specified coordinates on the player's grid.
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @param icon the ImageIcon to place
     */
    public void placeShipPart(int x, int y, ImageIcon icon) {
        gridButtons[x][y].setIcon(icon);
    }

    /**
     * Marks the player's board with the specified icon at the given coordinates.
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @param icon the ImageIcon to place
     */
    public void markPlayerBoard(int x, int y, ImageIcon icon) {
        gridButtons[x][y].setIcon(icon);
    }

    /**
     * Marks the computer's board with the specified icon at the given coordinates.
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @param icon the ImageIcon to place
     */
    public void markComputerBoard(int x, int y, ImageIcon icon) {
        computerGridButtons[x][y].setIcon(icon);
    }

    /**
     * Displays the computer's game board.
     */
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

    /**
     * Displays the player's game board.
     */
    public void showPlayerBoard() {
        if (computerGridPanel.getParent() != null) {
            panel.remove(computerGridPanel);
        }
        panel.add(gridPanel, BorderLayout.CENTER);
        panel.revalidate();
        panel.repaint();
    }

    /**
     * Displays the host's game board.
     */
    public void showHostBoard() {
        showPlayerBoard();
    }

    /**
     * Gets the Player vs. Environment (PvE) menu item.
     * @return the PvE menu item
     */
    public JMenuItem getPveItem() {
        return pveItem;
    }

    /**
     * Gets the Player vs. Player (PvP) menu.
     * @return the PvP menu
     */
    public JMenu getPvpItem() {
        return pvpMenu;
    }

    /**
     * Gets the host game menu item.
     * @return the host game menu item
     */
    public JMenuItem getHostItem() {
        return hostItem;
    }

    /**
     * Gets the connect game menu item.
     * @return the connect game menu item
     */
    public JMenuItem getConnectItem() {
        return connectItem;
    }

    /**
     * Gets the disconnect game menu item.
     * @return the disconnect game menu item
     */
    public JMenuItem getDisconnectItem() {
        return disconnectItem;
    }

    /**
     * Gets the restart game menu item.
     * @return the restart game menu item
     */
    public JMenuItem getRestartItem() {
        return restartItem;
    }

    /**
     * Gets the exit game menu item.
     * @return the exit game menu item
     */
    public JMenuItem getExitItem() {
        return exitItem;
    }

    /**
     * Gets the English language menu item.
     * @return the English language menu item
     */
    public JMenuItem getEnglishItem() {
        return englishItem;
    }

    /**
     * Gets the Chinese language menu item.
     * @return the Chinese language menu item
     */
    public JMenuItem getChineseItem() {
        return chineseItem;
    }

    /**
     * Gets the rotate button.
     * @return the rotate button
     */
    public JButton getRotateButton() {
        return rotateButton;
    }

    /**
     * Gets the start button.
     * @return the start button
     */
    public JButton getStartButton() {
        return startButton;
    }

    /**
     * Gets the end turn button.
     * @return the end turn button
     */
    public JButton getEndTurnButton() {
        return endTurnButton;
    }

    /**
     * Gets the quit button.
     * @return the quit button
     */
    public JButton getQuitButton() {
        return quitButton;
    }

    /**
     * Gets the east-facing bow image icon.
     * @return the east-facing bow image icon
     */
    public ImageIcon getBowEast() {
        return bowEast;
    }

    /**
     * Gets the north-facing bow image icon.
     * @return the north-facing bow image icon
     */
    public ImageIcon getBowNorth() {
        return bowNorth;
    }

    /**
     * Gets the south-facing bow image icon.
     * @return the south-facing bow image icon
     */
    public ImageIcon getBowSouth() {
        return bowSouth;
    }

    /**
     * Gets the west-facing bow image icon.
     * @return the west-facing bow image icon
     */
    public ImageIcon getBowWest() {
        return bowWest;
    }

    /**
     * Gets the horizontal mid-hull image icon.
     * @return the horizontal mid-hull image icon
     */
    public ImageIcon getMidHullHoriz() {
        return midHullHoriz;
    }

    /**
     * Gets the vertical mid-hull image icon.
     * @return the vertical mid-hull image icon
     */
    public ImageIcon getMidHullVert() {
        return midHullVert;
    }

    /**
     * Gets the hit marker image icon.
     * @return the hit marker image icon
     */
    public ImageIcon getHitIcon() {
        return hitIcon;
    }

    /**
     * Gets the miss marker image icon.
     * @return the miss marker image icon
     */
    public ImageIcon getMissIcon() {
        return missIcon;
    }

    /**
     * Gets the player's grid buttons.
     * @return a 2D array of player's grid buttons
     */
    public JButton[][] getGridButtons() {
        return gridButtons;
    }

    /**
     * Gets the computer's grid buttons.
     * @return a 2D array of computer's grid buttons
     */
    public JButton[][] getComputerGridButtons() {
        return computerGridButtons;
    }

    /**
     * Gets the chat input field.
     * @return the chat input field
     */
    public JTextField getChatInput() {
        return chatInput;
    }

    /**
     * Displays a message indicating the ship rotation direction.
     * @param isVertical true if the ship is placed vertically, false otherwise
     */
    public void showRotationMessage(boolean isVertical) {
        showMessage(bundle.getString("showRotationMessage") + (isVertical ? bundle.getString("vertical") : bundle.getString("horizontal")));
    }

    /**
     * Displays a message indicating that all ships need to be placed before starting the game.
     */
    public void showPlaceAllShipsMessage() {
        showMessage(bundle.getString("showPlaceAllShipsMessage"));
    }

    /**
     * Displays a message indicating that all ships have been placed.
     */
    public void showAllShipsPlacedMessage() {
        showMessage(bundle.getString("showAllShipsPlacedMessage"));
    }

    /**
     * Displays a message indicating that a ship cannot be placed at the specified location.
     */
    public void showCannotPlaceShipMessage() {
        showMessage(bundle.getString("showCannotPlaceShipMessage"));
    }

    /**
     * Displays a victory message.
     */
    public void showVictoryMessage() {
        JOptionPane.showMessageDialog(this, bundle.getString("showVictoryMessage"));
    }

    /**
     * Displays a loss message.
     */
    public void showLossMessage() {
        JOptionPane.showMessageDialog(this, bundle.getString("showLossMessage"));
    }

    /**
     * Enables or disables the start button.
     * @param enable true to enable the button, false to disable
     */
    public void enableStartButton(boolean enable) {
        startButton.setEnabled(enable);
    }

    /**
     * Displays a message indicating that the player cannot take two turns in a row.
     */
    public void showCannotGoTwiceMessage() {
        showMessage(bundle.getString("showCannotGoTwiceMessage"));
    }

    /**
     * Exits the game.
     */
    public void exitGame() {
        System.exit(0);
    }

    /**
     * Displays a message in the game log.
     * @param message the message to display
     */
    public void showMessage(String message) {
        gameLog.append(message + "\n");
        gameLog.setCaretPosition(gameLog.getDocument().getLength());
    }

    /**
     * Displays a chat message in the chat area.
     * @param message the chat message to display
     */
    public void showChatMessage(String message) {
        chatArea.append(message + "\n");
        chatArea.setCaretPosition(chatArea.getDocument().getLength());
    }
}
