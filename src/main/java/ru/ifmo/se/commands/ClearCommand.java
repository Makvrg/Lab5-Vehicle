package ru.ifmo.se.commands;

import ru.ifmo.se.io.input.readers.Reader;
import ru.ifmo.se.io.output.print.Printer;
import ru.ifmo.se.service.CollectionService;

public class ClearCommand extends Command {

    private final CollectionService collectionService;
    private final Printer printer;

    public ClearCommand(CollectionService collectionService, Printer printer) {
        super("clear", "очистить коллекцию");
        this.collectionService = collectionService;
        this.printer = printer;
    }

    @Override
    public void execute(String[] ignoredArgs, Reader ignoredReader) {
        if (collectionService.clear()) {
            printer.forcePrintln("Коллекция успешно очищена");
        } else {
            printer.forcePrintln("Коллекция уже пуста");
        }
    }
}
