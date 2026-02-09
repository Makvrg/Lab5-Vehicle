package ru.ifmo.se.commands;

import ru.ifmo.se.entity.Vehicle;
import ru.ifmo.se.io.input.readers.Reader;
import ru.ifmo.se.io.output.formatter.OutputStringFormatter;
import ru.ifmo.se.io.output.print.Printer;
import ru.ifmo.se.service.CollectionService;

import java.util.Collection;

public class ShowCommand extends Command {

    private final CollectionService collectionService;
    private final Printer printer;
    private final OutputStringFormatter formatter;

    public ShowCommand(CollectionService collectionService,
                       Printer printer,
                       OutputStringFormatter formatter) {
        super("show",
                "вывести в стандартный поток вывода все элементы "
                        + "коллекции в строковом представлении"
        );
        this.collectionService = collectionService;
        this.printer = printer;
        this.formatter = formatter;
    }

    @Override
    public void execute(String[] ignoredArgs, Reader ignoredReader) {
        Collection<Vehicle> vehicles = collectionService.show();
        if (!vehicles.isEmpty()) {
            printer.forcePrintln("Содержимые в коллекции объекты Vehicle:\n"
                    + formatter.formatVehicleCollection(vehicles));
        } else {
            printer.forcePrintln("Коллекция пуста");
        }
    }
}
