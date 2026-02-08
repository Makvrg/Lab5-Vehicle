package ru.ifmo.se.commands;

import ru.ifmo.se.io.input.readers.Reader;
import ru.ifmo.se.io.output.print.Printer;
import ru.ifmo.se.service.CollectionService;

public class InfoCommand extends Command {

    private final CollectionService collectionService;
    private final Printer printer;

    public InfoCommand(CollectionService collectionService, Printer printer) {
        super("info", "вывести в стандартный поток вывода информацию о коллекции "
                + "(тип, дата инициализации, тип и количество элементов)"
        );
        this.collectionService = collectionService;
        this.printer = printer;
    }

    @Override
    public void execute(String[] ignoredArgs, Reader ignoredReader) {
        printer.forcePrintln(collectionService.info());
    }
}
