package ru.ifmo.se.commands;

import ru.ifmo.se.entity.Vehicle;
import ru.ifmo.se.io.input.dto.ParamRawData;
import ru.ifmo.se.io.output.formatter.OutputStringFormatter;
import ru.ifmo.se.io.output.print.Printer;
import ru.ifmo.se.service.CollectionService;
import ru.ifmo.se.service.ParamTypedData;
import ru.ifmo.se.typer.DataTyper;
import ru.ifmo.se.validator.CommandValidatorProvider;

public class UpdateByIdCommand extends AbstractAddCommand {

    public UpdateByIdCommand(CollectionService collectionService,
                             CommandValidatorProvider validatorProvider,
                             DataTyper dataTyper,
                             Printer printer, OutputStringFormatter formatter) {
        super("update id {element}",
                "обновить значение элемента коллекции, id которого равен заданному",
                collectionService, validatorProvider,
                dataTyper, printer, formatter
        );
    }

    @Override
    protected ParamRawData makeParamRawData(String[] inputArgs) {
        ParamRawData paramRawData = new ParamRawData();
        paramRawData.setId(
                (inputArgs.length == 1 || inputArgs[1] == null) ? "" : inputArgs[1]
        );
        return paramRawData;
    }

    @Override
    protected boolean useService(Vehicle vehicle,
                                 ParamTypedData paramTypedData) {
        return collectionService.updateById(vehicle, paramTypedData);
    }

    @Override
    protected void workWithPrintedText(boolean result) {
        if (result) {
            super.printer.forcePrintln("Объект успешно обновлён");
        } else {
            super.printer.forcePrintln(
                    "Объект не был обновлён, так как в коллекции нет объекта с данным id");
        }
    }
}
