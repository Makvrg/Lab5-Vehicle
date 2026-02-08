package ru.ifmo.se.application;

public class App {

    private App() {
    }

    public static void main(final String[] args) {
        Starter appStarter = new AppStarter();
        appStarter.start();
    }
}
