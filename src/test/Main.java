package test;

import view.GameUi;
import controller.Controller;

/**
 * Main class to start the Battleship game.
 */
public class Main {
    /**
     * The main method to start the Battleship game.
     * 
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        GameUi gameUi = new GameUi();
        new Controller(gameUi);
        gameUi.showMenu();
    }
}
