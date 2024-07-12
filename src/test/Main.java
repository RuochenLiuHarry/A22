package test;

import view.GameUi;
import controller.GameController;

public class Main {
    public static void main(String[] args) {
        GameUi gameUi = new GameUi();
        GameController gameController = new GameController(gameUi);
        gameUi.showMenu();
    }
}
