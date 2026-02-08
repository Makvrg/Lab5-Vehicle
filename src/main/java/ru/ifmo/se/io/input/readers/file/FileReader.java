package ru.ifmo.se.io.input.readers.file;

import lombok.RequiredArgsConstructor;
import ru.ifmo.se.io.input.readers.Reader;

import java.io.IOException;
import java.util.Scanner;

@RequiredArgsConstructor
public class FileReader implements Reader {

    private final String name;
    private final DataProvider dataProvider;
    private final String fileName;
    private Scanner scanner;
    private boolean lastIsNewLine = false;

    @Override
    public String readLine() throws IOException {
        if (scanner == null) {
            scanner = dataProvider.open(fileName);
        }
        if (!scanner.hasNextLine()) {
            if (lastIsNewLine) {
                lastIsNewLine = false;
                return "";
            } else {
                scanner.close();
                return null;
            }
        }

        String line = scanner.nextLine();
        lastIsNewLine = true;
        return line;
    }

    @Override
    public String getName() {
        return name;
    }
}
