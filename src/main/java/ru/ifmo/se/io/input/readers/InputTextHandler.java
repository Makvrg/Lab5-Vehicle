package ru.ifmo.se.io.input.readers;

public class InputTextHandler {

    public static String[] parseArguments(String line) {
        return line.strip().split("\\s+");
    }

    public static String stripOrNullField(String line) {
        if (line.isEmpty()) {
            return null;
        }
        return line.strip();
    }
}
