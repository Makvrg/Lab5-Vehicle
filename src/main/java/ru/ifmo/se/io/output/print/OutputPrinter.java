package ru.ifmo.se.io.output.print;

public class OutputPrinter implements Printer {

    private boolean onOff = true;

    @Override
    public void printlnIfOn(String inputLine) {
        if (onOff) {
            System.out.println(inputLine);
        }
    }

    @Override
    public void printIfOn(String inputLine) {
        if (onOff) {
            System.out.print(inputLine);
        }
    }

    @Override
    public void forcePrintln(String inputLine) {
        System.out.println(inputLine);
    }

    @Override
    public void forcePrint(String inputLine) {
        System.out.print(inputLine);
    }

    @Override
    public void on() {
        onOff = true;
    }

    @Override
    public void off() {
        onOff = false;
    }
}
