package ru.ifmo.se.io.input;

import jakarta.validation.ConstraintViolation;
import ru.ifmo.se.entity.Vehicle;
import ru.ifmo.se.event.ShutdownListener;
import ru.ifmo.se.io.input.env.EnvironmentProvider;
import ru.ifmo.se.io.input.json.CityJsonParser;
import ru.ifmo.se.io.input.json.JsonValidationException;
import ru.ifmo.se.io.input.readers.InputTextHandler;
import ru.ifmo.se.io.input.readers.Reader;
import ru.ifmo.se.io.input.readers.factory.ReaderFactory;
import ru.ifmo.se.io.input.readers.file.FileReader;
import ru.ifmo.se.io.input.readers.file.DataProvider;
import ru.ifmo.se.io.input.readers.terminal.TerminalReader;
import ru.ifmo.se.io.output.formatter.OutputStringFormatter;
import ru.ifmo.se.io.output.json.CityJsonWriter;
import ru.ifmo.se.io.output.print.CollectionActionsMessages;
import ru.ifmo.se.io.output.print.Printer;
import ru.ifmo.se.service.CollectionService;
import ru.ifmo.se.service.exceptions.CreationDateIsAfterNowException;
import ru.ifmo.se.service.exceptions.NonUniqueIdException;
import ru.ifmo.se.typer.DataTyper;
import ru.ifmo.se.validator.CommandValidatorProvider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class CommandInput implements Runnable, ShutdownListener {

    private final List<Reader> readers = new ArrayList<>();
    private final Printer printer;
    private final CollectionService collectionService;
    private final CommandValidatorProvider validatorProvider;
    private final OutputStringFormatter formatter;
    private final EnvironmentProvider environmentProvider;
    private final DataProvider dataProvider;
    private final CityJsonParser initCitiesParser;
    private final CommandInvoker commandInvoker;
    private boolean shutdown = false;

    public CommandInput(Reader reader,
                        ReaderFactory readerFactory,
                        Printer printer,
                        CollectionService collectionService,
                        CommandValidatorProvider validatorProvider,
                        DataTyper dataTyper,
                        OutputStringFormatter formatter,
                        EnvironmentProvider environmentProvider,
                        CityJsonWriter fileWriter,
                        DataProvider dataProvider,
                        CityJsonParser initCitiesParser) {
        this.readers.add(reader);
        this.printer = printer;
        this.collectionService = collectionService;
        this.validatorProvider = validatorProvider;
        this.formatter = formatter;
        this.environmentProvider = environmentProvider;
        this.dataProvider = dataProvider;
        this.initCitiesParser = initCitiesParser;
        this.commandInvoker =
                new CommandInvoker(
                        dataProvider,
                        readerFactory,
                        collectionService,
                        validatorProvider,
                        dataTyper,
                        readers,
                        formatter,
                        printer,
                        environmentProvider,
                        fileWriter
                );
    }

    @Override
    public void run() {
        while (!shutdown) {
            printer.printIfOn("> ");
            Reader currentReader = readers.get(readers.size() - 1);
            try {
                String inputLine = currentReader.readLine();

                if (inputLine == null) {
                    if (currentReader instanceof FileReader) {
                        readers.remove(readers.size() - 1);
                        if (readers.size() == 1) {
                            printer.on();
                        }
                        printer.forcePrintln(
                                formatter.formatCurrentReaderInfo(List.copyOf(readers))
                        );
                        continue;
                    }
                    inputLine = "";
                    printer.forcePrintln("");
                }

                String[] inputArgs = InputTextHandler.parseArguments(
                        inputLine
                );
                commandInvoker.invokeCommand(inputArgs);
            } catch (IOException e) {
                if (currentReader instanceof TerminalReader) {
                    printer.forcePrintln(
                            "При чтении с терминала произошла ошибка"
                    );
                }
                if (currentReader instanceof FileReader) {
                    printer.forcePrintln(
                            "Файл с указанным названием не найден или к нему нет доступа"
                    );
                    readers.remove(readers.size() - 1);
                    if (readers.size() == 1) {
                        printer.on();
                    }
                    printer.forcePrintln(
                            formatter.formatCurrentReaderInfo(List.copyOf(readers))
                    );
                }
            }
        }
    }

    public void initialize() throws IOException {
        printer.on();
        printer.forcePrintln("Приложение запускается");

        String fileName = environmentProvider.getFileName();

        if (fileName == null) {
            printer.forcePrintln("Не найдена переменная окружения с названием файла");
            return;
        }
        try (Scanner reader = dataProvider.open(fileName)) {
            List<Vehicle> vehicles;
            try {
                vehicles = initCitiesParser.parse(reader);
            } catch (JsonValidationException e) {
                printer.forcePrintln("Произошла ошибка инициализации коллекции:");
                printer.forcePrintln(e.getMessage());
                return;
            }

            if (checkVehiclesFromFile(vehicles)) {
                if (addAllVehiclesFromFile(vehicles)) {
                    printer.forcePrintln(
                            "Инициализация коллекции объектами Vehicle из файла завершена успешно");
                } else {
                    collectionService.clear();
                }
            }
        }
    }

    private boolean checkVehiclesFromFile(List<Vehicle> cities) {
        int counter = 1;
        for (Vehicle vehicle : cities) {

            Set<ConstraintViolation<Vehicle>> violations =
                    validatorProvider.getBeanValidator().validate(vehicle);

            if (!violations.isEmpty()) {
                printer.forcePrintln(
                        String.format(CollectionActionsMessages.VEHICLE_INIT_VALID_EXC,
                                vehicle.getId(), counter)
                );
                printer.forcePrintln("Выявленные в нём ошибки: ");
                for (ConstraintViolation<Vehicle> vio : violations) {
                    printer.forcePrintln(vio.getMessage());
                }
                return false;
            }
            counter++;
        }
        return true;
    }

    private boolean addAllVehiclesFromFile(List<Vehicle> cities) {
        int counter = 1;
        for (Vehicle vehicle : cities) {
            try {
                if (collectionService.addInitVehicle(vehicle)) {
                    counter++;
                } else {
                    printer.forcePrintln(
                            String.format(
                                    CollectionActionsMessages.VEHICLE_INIT_UNKNOWN_EXC,
                                    vehicle.getId(), counter
                            )
                    );
                    return false;
                }
            } catch (NonUniqueIdException | CreationDateIsAfterNowException e) {
                printer.forcePrintln(
                        String.format(
                                CollectionActionsMessages.VEHICLE_INIT_ADD_EXC,
                                vehicle.getId(),
                                counter
                        )
                );
                printer.forcePrintln("Ошибка добавления в коллекцию: " + e.getMessage());
                return false;
            }
        }
        return true;
    }

    @Override
    public void onShutdown() {
        shutdown = true;
    }
}
