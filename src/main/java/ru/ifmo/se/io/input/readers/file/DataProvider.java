package ru.ifmo.se.io.input.readers.file;

import java.io.IOException;
import java.util.Scanner;

public interface DataProvider {

    Scanner open(String fileName) throws IOException;
}
