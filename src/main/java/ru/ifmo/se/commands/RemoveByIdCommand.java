package ru.ifmo.se.commands;

import ru.ifmo.se.io.input.readers.Reader;
import ru.ifmo.se.io.output.print.Printer;
import ru.ifmo.se.service.CollectionService;
import ru.ifmo.se.service.exceptions.RemoveByIdIllegalStateException;
import ru.ifmo.se.validator.CommandValidatorProvider;
import ru.ifmo.se.validator.exceptions.RemoveByIdValidationException;

public class RemoveByIdCommand extends Command {

    private final CollectionService collectionService;
    private final CommandValidatorProvider validatorProvider;
    private final Printer printer;

    public RemoveByIdCommand(CollectionService collectionService,
                             CommandValidatorProvider validatorProvider,
                             Printer printer) {
        super("remove_by_id id", "удалить элемент из коллекции по его id");
        this.collectionService = collectionService;
        this.validatorProvider = validatorProvider;
        this.printer = printer;
    }

    @Override
    public void execute(String[] inputArgs, Reader ignoredReader) {
        String id = (inputArgs.length > 1) ? inputArgs[1] : null;
        try {
            validatorProvider.getDataValidator().validateRemoveById(id);

            if (collectionService.removeById(Long.parseLong(id))) {
                printer.printlnIfOn(
                        "Объект Vehicle успешно удалён из коллекции по заданному id");
            } else {
                printer.printlnIfOn(
                        "Объект Vehicle с заданным id не найден в коллекции");
            }
        } catch (RemoveByIdValidationException e) {
            printer.printlnIfOn(e.getMessage());
        } catch (RemoveByIdIllegalStateException e) {
            printer.printlnIfOn(
                    "Объект не удалён, так как произошла ошибка во время работы: " +
                            e.getMessage()
            );
        }
    }
}
