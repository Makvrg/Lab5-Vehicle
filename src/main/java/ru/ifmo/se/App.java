package ru.ifmo.se;

import ru.ifmo.se.application.AppStarter;

public class App {

    private App() {
    }

    public static void main(String[] args) {
        AppStarter appStarter = new AppStarter();
        appStarter.start();
    }
}
