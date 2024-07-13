package test;

import view.GameUi;
import controller.Controller;

public class Main {
    public static void main(String[] args) {
        GameUi gameUi = new GameUi();
        Controller gameController = new Controller(gameUi);
        gameUi.showMenu();
    }
}
