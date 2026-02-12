package ru.ifmo.se.io.output.fileparser;

import java.io.IOException;

public interface FileWriter<T> {

    void write(String fileName, T data) throws IOException;

    void writeBackup(T data) throws IOException;
}
