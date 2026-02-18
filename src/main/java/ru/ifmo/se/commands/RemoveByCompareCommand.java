package ru.ifmo.se.commands;

import ru.ifmo.se.entity.Coordinates;
import ru.ifmo.se.entity.Vehicle;
import ru.ifmo.se.io.input.readers.Reader;
import ru.ifmo.se.io.output.formatter.OutputStringFormatter;
import ru.ifmo.se.io.output.print.Printer;
import ru.ifmo.se.service.CollectionService;
import ru.ifmo.se.service.exceptions.RemoveByIdIllegalStateException;
import ru.ifmo.se.validator.CommandValidatorProvider;
import ru.ifmo.se.validator.exceptions.ExecuteScriptValidateException;

public abstract class RemoveByCompareCommand extends VehicleAwareCommand {

    protected RemoveByCompareCommand(
            String commandSignature,
            String commandDescription,
            CollectionService collectionService,
            CommandValidatorProvider validatorProvider,
            Printer printer,
            OutputStringFormatter formatter) {
        super(commandSignature, commandDescription,
                collectionService, validatorProvider, printer, formatter);
    }

    @Override
    public void execute(String[] ignoredArgs, Reader reader) {
        this.reader = reader;
        vehicle = new Vehicle();
        vehicle.setCoordinates(new Coordinates());
        try {
            readManage();
        } catch (ExecuteScriptValidateException e) {
            printer.forcePrintln(e.getMessage());
            return;
        }
        try {

            if (useService(vehicle)) {
                printer.printlnIfOn(
                        "Прошло успешное удаление");
            } else {
                printer.printlnIfOn(
                        "Объекты для удаления не были найдены"
                );
            }
        } catch (RemoveByIdIllegalStateException e) {
            printer.printlnIfOn(
                    "Объекты не были удалены, так как произошла ошибка во время работы: "
                            + e.getMessage()
            );
        }
    }

    protected abstract boolean useService(Vehicle vehicle);
}
