package ru.ifmo.se.commands;

import ru.ifmo.se.io.input.readers.Reader;
import ru.ifmo.se.io.output.print.Printer;
import ru.ifmo.se.service.CollectionService;

public class ExitCommand extends Command {

    private final CollectionService collectionService;
    private final Printer printer;

    public ExitCommand(CollectionService collectionService, Printer printer) {
        super("exit", "завершить программу (без сохранения в файл)");
        this.collectionService = collectionService;
        this.printer = printer;
    }

    @Override
    public void execute(String[] ignoredArgs, Reader ignoredReader) {
        if (collectionService.exit()) {
            printer.forcePrintln("Закрытие приложения");
        }
    }
}
