package test;

import view.GameUi;
import controller.Controller;

public class Main {
    public static void main(String[] args) {
        GameUi gameUi = new GameUi();
        new Controller(gameUi);
        gameUi.showMenu();
    }
}
