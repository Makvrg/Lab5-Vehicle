package ru.ifmo.se.commands;

import ru.ifmo.se.entity.Vehicle;
import ru.ifmo.se.io.output.formatter.OutputStringFormatter;
import ru.ifmo.se.io.output.print.Printer;
import ru.ifmo.se.service.CollectionService;
import ru.ifmo.se.validator.CommandValidatorProvider;

public class RemoveGreaterCommand extends RemoveByCompareCommand {

    public RemoveGreaterCommand(CollectionService collectionService,
                                CommandValidatorProvider validatorProvider,
                                Printer printer,
                                OutputStringFormatter formatter) {
        super("remove_greater {element}",
                "удалить из коллекции все элементы, превышающие заданный",
                collectionService, validatorProvider, printer, formatter);
    }

    @Override
    protected boolean useService(Vehicle vehicle) {
        return collectionService.removeGreater(vehicle);
    }
}
