package ru.ifmo.se.commands;

import ru.ifmo.se.entity.VehicleType;
import ru.ifmo.se.io.input.readers.Reader;
import ru.ifmo.se.io.output.print.Printer;
import ru.ifmo.se.service.CollectionService;
import ru.ifmo.se.validator.CommandValidatorProvider;
import ru.ifmo.se.validator.exceptions.InputFieldValidationException;

public class CountLessThanTypeCommand extends Command {

    private final CollectionService collectionService;
    private final CommandValidatorProvider validatorProvider;
    private final Printer printer;

    public CountLessThanTypeCommand(CollectionService collectionService,
                                    CommandValidatorProvider validatorProvider,
                                    Printer printer) {
        super("count_less_than_type type",
                "вывести количество элементов, значение поля type которых меньше заданного");
        this.collectionService = collectionService;
        this.validatorProvider = validatorProvider;
        this.printer = printer;
    }

    @Override
    public void execute(String[] inputArgs, Reader ignoredReader) {
        String rusType = (inputArgs.length > 1) ? inputArgs[1] : null;
        try {
            validatorProvider.getDataValidator().validateRusVehicleType(rusType);
            if (collectionService.getCountElementsCollection() != 0) {
                long countVeh = collectionService.countLessThanType(
                        VehicleType.fromRussianString(rusType));
                printer.forcePrintln("Количество элементов равно " + countVeh);
            } else {
                printer.forcePrintln("Коллекция пуста");
            }
        } catch (InputFieldValidationException e) {
            printer.printlnIfOn(e.getMessage());
        }
    }
}
