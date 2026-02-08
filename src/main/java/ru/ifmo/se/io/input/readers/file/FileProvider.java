package ru.ifmo.se.io.input.readers.file;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class FileProvider implements DataProvider {

    @Override
    public Scanner open(String fileName) throws IOException {
        return new Scanner(new File(fileName), StandardCharsets.UTF_8);
    }
}
