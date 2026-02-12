package ru.ifmo.se.commands;

import ru.ifmo.se.io.input.env.EnvironmentProvider;
import ru.ifmo.se.io.input.readers.Reader;
import ru.ifmo.se.io.output.fileparser.VehicleCsvWriter;
import ru.ifmo.se.io.output.print.CollectionActionsMessages;
import ru.ifmo.se.io.output.print.Printer;
import ru.ifmo.se.service.CollectionService;

import java.io.IOException;

public class SaveCommand extends Command {

    private final CollectionService collectionService;
    private final Printer printer;
    private final EnvironmentProvider environmentProvider;
    private final VehicleCsvWriter fileWriter;

    public SaveCommand(CollectionService collectionService,
                       Printer printer,
                       EnvironmentProvider environmentProvider,
                       VehicleCsvWriter fileWriter) {
        super("save", "сохранить коллекцию в файл");
        this.collectionService = collectionService;
        this.printer = printer;
        this.environmentProvider = environmentProvider;
        this.fileWriter = fileWriter;
    }

    @Override
    public void execute(String[] ignoredArgs, Reader ignoredReader) {
        String fileName = environmentProvider.getFileName();
        try {
            if (fileName == null) {
                fileWriter.writeBackup(collectionService.getVehiclesForSave());
            } else {
                fileWriter.write(fileName, collectionService.getVehiclesForSave());
            }
            printer.forcePrintln("Сохранение коллекции прошло успешно");
        } catch (IOException e) {
            printer.forcePrintln(CollectionActionsMessages.SAVE_COLLECTION_EXC);
        }
    }
}
