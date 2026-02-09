package ru.ifmo.se.commands;

import ru.ifmo.se.entity.Vehicle;
import ru.ifmo.se.io.input.readers.Reader;
import ru.ifmo.se.io.output.formatter.OutputStringFormatter;
import ru.ifmo.se.io.output.print.Printer;
import ru.ifmo.se.service.CollectionService;

import java.util.Optional;

public class MaxByEnginePowerCommand extends Command {

    private final CollectionService collectionService;
    private final Printer printer;
    private final OutputStringFormatter formatter;

    public MaxByEnginePowerCommand(CollectionService collectionService,
                                   Printer printer,
                                   OutputStringFormatter formatter) {
        super("max_by_engine_power",
                "вывести любой объект из коллекции, " +
                        "значение поля enginePower которого является максимальным"
        );
        this.collectionService = collectionService;
        this.printer = printer;
        this.formatter = formatter;
    }

    @Override
    public void execute(String[] ignoredArgs, Reader ignoredReader) {
        Optional<Vehicle> vehicle = collectionService.maxByEnginePower();
        if (vehicle.isPresent()) {
            printer.forcePrintln("Найденный объект:\n"
                    + formatter.formatVehicle(vehicle.get()));
        } else {
            printer.forcePrintln("Коллекция пуста");
        }
    }
}
