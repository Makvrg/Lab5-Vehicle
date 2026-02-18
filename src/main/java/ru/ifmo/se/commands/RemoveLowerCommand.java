package ru.ifmo.se.commands;

import ru.ifmo.se.entity.Vehicle;
import ru.ifmo.se.io.output.formatter.OutputStringFormatter;
import ru.ifmo.se.io.output.print.Printer;
import ru.ifmo.se.service.CollectionService;
import ru.ifmo.se.validator.CommandValidatorProvider;

public class RemoveLowerCommand extends RemoveByCompareCommand {

    public RemoveLowerCommand(CollectionService collectionService,
                              CommandValidatorProvider validatorProvider,
                              Printer printer,
                              OutputStringFormatter formatter) {
        super("remove_lower {element}",
                "удалить из коллекции все элементы, меньшие, чем заданный",
                collectionService, validatorProvider, printer, formatter);
    }

    @Override
    protected boolean useService(Vehicle vehicle) {
        return collectionService.removeLower(vehicle);
    }
}
