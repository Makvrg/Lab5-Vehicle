package ru.ifmo.se.commands;

import ru.ifmo.se.entity.Vehicle;
import ru.ifmo.se.entity.Coordinates;
import ru.ifmo.se.io.input.dto.ParamRawData;
import ru.ifmo.se.io.input.readers.Reader;
import ru.ifmo.se.io.output.print.Printer;
import ru.ifmo.se.service.CollectionService;
import ru.ifmo.se.service.ParamTypedData;
import ru.ifmo.se.typer.DataTyper;
import ru.ifmo.se.validator.CommandValidatorProvider;
import ru.ifmo.se.validator.exceptions.ExecuteScriptValidateException;
import ru.ifmo.se.validator.exceptions.ParamRawDataValidationException;

public abstract class AbstractAddCommand extends VehicleAwareCommand {

    private final DataTyper dataTyper;

    protected AbstractAddCommand(
            String commandSignature,
            String commandDescription,
            CollectionService collectionService,
            CommandValidatorProvider validatorProvider,
            DataTyper dataTyper,
            Printer printer) {
        super(commandSignature, commandDescription,
                collectionService, validatorProvider, printer);
        this.dataTyper = dataTyper;
    }

    @Override
    public void execute(String[] inputArgs, Reader reader) {
        this.reader = reader;
        ParamRawData paramRawData = makeParamRawData(inputArgs);

        if (!paramRawData.getEmptyFields().isEmpty()) {
            printer.forcePrintln("Отсутствуют аргументы команды: ");
            for (String emptyField : paramRawData.getEmptyFields()) {
                printer.forcePrintln(emptyField);
            }
            return;
        }
        try {
            validatorProvider.getDataValidator().validateParamRawData(paramRawData);
        } catch (ParamRawDataValidationException e) {
            printer.forcePrintln(e.getMessage());
            return;
        }

        vehicle = new Vehicle();
        vehicle.setCoordinates(new Coordinates());
        try {
            readManage();
        } catch (ExecuteScriptValidateException e) {
            printer.forcePrintln(e.getMessage());
            return;
        }

        ParamTypedData paramTypedData =
                dataTyper.typifyParamRawData(paramRawData);

        workWithPrintedText(
                useService(
                        vehicle,
                        paramTypedData
                )
        );
    }

    protected abstract ParamRawData makeParamRawData(String[] inputArgs);

    protected abstract void workWithPrintedText(boolean result);

    protected abstract boolean useService(Vehicle vehicle,
                                          ParamTypedData paramTypedData);
}
