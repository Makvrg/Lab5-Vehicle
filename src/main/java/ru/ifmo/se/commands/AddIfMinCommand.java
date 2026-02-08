package ru.ifmo.se.commands;

import ru.ifmo.se.entity.Vehicle;
import ru.ifmo.se.io.input.dto.ParamRawData;
import ru.ifmo.se.io.output.print.Printer;
import ru.ifmo.se.service.CollectionService;
import ru.ifmo.se.service.ParamTypedData;
import ru.ifmo.se.typer.DataTyper;
import ru.ifmo.se.validator.DataValidator;

public class AddIfMinCommand extends AbstractAddCommand {

    public AddIfMinCommand(CollectionService collectionService,
                           DataValidator dataValidator,
                           DataTyper dataTyper,
                           Printer printer) {
        super("add_if_min {element}",
                "добавить новый элемент в коллекцию, если его значение меньше, "
                        + "чем у наименьшего элемента этой коллекции",
                collectionService, dataValidator, dataTyper, printer
        );
    }

    @Override
    protected ParamRawData makeParamRawData(String[] inputArgs) {
        return new ParamRawData();
    }

    @Override
    protected boolean useService(Vehicle vehicle,
                                 ParamTypedData paramTypedData) {
        return collectionService.addIfMin(vehicle);
    }

    @Override
    protected void workWithPrintedText(boolean result) {
        if (result) {
            super.printer.forcePrintln("Новый объект успешно добавлен в коллекцию");
        } else {
            super.printer.forcePrintln("Новый объект не был добавлен в коллекцию");
        }
    }
}
