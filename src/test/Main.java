package test;

import view.GameUi;
import controller.Controller;

/**
 * Main is the entry point for the Battleship game application.
 * It initializes the game UI and the controller, and starts the game by showing the main menu.
 */
public class Main {

    /**
     * The main method to start the Battleship game.
     * It initializes the game UI and the controller, and displays the main menu.
     * 
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        GameUi gameUi = new GameUi();
        new Controller(gameUi);
        gameUi.showMenu();
    }
}
