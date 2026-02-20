package ru.ifmo.se.commands;

import ru.ifmo.se.entity.Vehicle;
import ru.ifmo.se.io.input.env.EnvVariableProvider;
import ru.ifmo.se.io.input.readers.Reader;
import ru.ifmo.se.io.output.fileparser.FileWriter;
import ru.ifmo.se.io.output.print.CollectionActionsMessages;
import ru.ifmo.se.io.output.print.Printer;
import ru.ifmo.se.service.CollectionService;

import java.io.IOException;

public class SaveCommand extends Command {

    private final CollectionService collectionService;
    private final Printer printer;
    private final EnvVariableProvider environmentProvider;
    private final FileWriter<Vehicle> fileWriter;

    public SaveCommand(CollectionService collectionService,
                       Printer printer,
                       EnvVariableProvider environmentProvider,
                       FileWriter<Vehicle> fileWriter) {
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
