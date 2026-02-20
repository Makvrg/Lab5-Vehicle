package ru.ifmo.se.application;

import lombok.Getter;
import ru.ifmo.se.collection.CollectionWithInfo;
import ru.ifmo.se.entity.Vehicle;
import ru.ifmo.se.io.input.CommandInput;
import ru.ifmo.se.io.input.env.EnvVariableProvider;
import ru.ifmo.se.io.input.fileparser.FileParser;
import ru.ifmo.se.io.input.fileparser.VehicleCsvParser;
import ru.ifmo.se.io.input.readers.Reader;
import ru.ifmo.se.io.input.readers.factory.ReaderFactory;
import ru.ifmo.se.io.input.readers.file.FileProvider;
import ru.ifmo.se.io.input.readers.file.DataProvider;
import ru.ifmo.se.io.output.fileparser.FileWriter;
import ru.ifmo.se.io.output.fileparser.VehicleCsvWriter;
import ru.ifmo.se.io.output.formatter.OutputStringFormatter;
import ru.ifmo.se.io.output.print.Printer;
import ru.ifmo.se.repository.CollectionRepository;
import ru.ifmo.se.service.CollectionService;
import ru.ifmo.se.typer.DataTyper;
import ru.ifmo.se.validator.CommandValidatorProvider;

import java.util.Collection;
import java.util.HashSet;

public final class AppCompositionRoot {

    private static final String ENV_VAR_NAME = "VEHICLE_FILE";
    private static final String BACKUP_FILE_NAME =
            "backup_collection_save_file_wl64fI983T";

    private final Collection<Vehicle> collection = new HashSet<>();

    private final CollectionWithInfo collectionWithInfo =
            new CollectionWithInfo(
                    collection,
                    collection.getClass(),
                    Vehicle.class
            );

    @Getter
    private final Printer printer = new Printer();

    private final OutputStringFormatter formatter = new OutputStringFormatter();

    private final ReaderFactory readerFactory = new ReaderFactory();
    private final Reader reader =
            readerFactory.createTerminalReader("Main Terminal");

    private final CommandValidatorProvider validatorProvider = new CommandValidatorProvider();
    private final DataTyper dataTyper = new DataTyper();

    private final EnvVariableProvider environmentProvider =
            new EnvVariableProvider(ENV_VAR_NAME);
    private final FileWriter<Vehicle> csvWriter =
            new VehicleCsvWriter(BACKUP_FILE_NAME);
    private final DataProvider dataProvider =
            new FileProvider();
    private final FileParser<Vehicle> csvParser =
            new VehicleCsvParser(formatter);

    private final CollectionRepository collectionRepository =
            new CollectionRepository(collectionWithInfo);

    @Getter
    private final CollectionService collectionService =
            new CollectionService(collectionRepository, formatter);

    @Getter
    private final CommandInput commandInput = new CommandInput(
            reader,
            readerFactory,
            printer,
            collectionService,
            validatorProvider,
            dataTyper,
            formatter,
            environmentProvider,
            csvWriter,
            dataProvider,
            csvParser
    );
}
