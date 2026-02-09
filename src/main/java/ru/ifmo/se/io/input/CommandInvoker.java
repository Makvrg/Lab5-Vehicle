package ru.ifmo.se.io.input;

import ru.ifmo.se.commands.*;
import ru.ifmo.se.io.input.env.EnvironmentProvider;
import ru.ifmo.se.io.input.readers.Reader;
import ru.ifmo.se.io.input.readers.factory.ReaderFactory;
import ru.ifmo.se.io.input.readers.file.DataProvider;
import ru.ifmo.se.io.output.formatter.OutputStringFormatter;
import ru.ifmo.se.io.output.json.CityJsonWriter;
import ru.ifmo.se.io.output.print.Printer;
import ru.ifmo.se.service.CollectionService;
import ru.ifmo.se.typer.DataTyper;
import ru.ifmo.se.validator.CommandValidatorProvider;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class CommandInvoker {

    private final DataProvider dataProvider;
    private final ReaderFactory readerFactory;
    private final List<Reader> commandInputReaders;
    private final OutputStringFormatter formatter;
    private final Map<String, Command> commands;
    private String keyOfUnknownCommand;

    public CommandInvoker(DataProvider dataProvider,
                          ReaderFactory readerFactory,
                          CollectionService collectionService,
                          CommandValidatorProvider validatorProvider,
                          DataTyper dataTyper,
                          List<Reader> readers,
                          OutputStringFormatter formatter,
                          Printer printer,
                          EnvironmentProvider environmentProvider,
                          CityJsonWriter fileWriter) {
        this.dataProvider = dataProvider;
        this.readerFactory = readerFactory;
        this.commandInputReaders = readers;
        this.formatter = formatter;
        commands = buildMapOfCommands(
                collectionService,
                validatorProvider,
                dataTyper,
                printer,
                environmentProvider,
                fileWriter
        );
    }

    public void invokeCommand(String[] inputArgs) {
        if (commands.containsKey(inputArgs[0])) {
            commands.get(inputArgs[0])
                    .execute(inputArgs,
                             commandInputReaders.get(
                                     commandInputReaders.size() - 1
                             )
                    );
        } else {
            commands.get(keyOfUnknownCommand)
                    .execute(inputArgs, null);
        }
    }

    private Map<String, Command> buildMapOfCommands(
            CollectionService collectionService,
            CommandValidatorProvider validatorProvider,
            DataTyper dataTyper,
            Printer printer,
            EnvironmentProvider environmentProvider,
            CityJsonWriter fileWriter) {
        Map<String, Command> commands = new LinkedHashMap<>();
        Command currentCommand;
        Function<Command, String> getCommandName = 
                cmd -> cmd.getCommandSignature().split(" ")[0];
        
        currentCommand = new UnknownCommand(printer);
        commands.put(getCommandName.apply(currentCommand), currentCommand);

        keyOfUnknownCommand = getCommandName.apply(currentCommand);
        
        currentCommand = new HelpCommand(printer, commands.values());
        commands.put(getCommandName.apply(currentCommand), currentCommand);

        currentCommand = new InfoCommand(collectionService, printer);
        commands.put(getCommandName.apply(currentCommand), currentCommand);

        currentCommand = new ShowCommand(collectionService, printer, formatter);
        commands.put(getCommandName.apply(currentCommand), currentCommand);

        currentCommand = new AddCommand(
                collectionService, validatorProvider,
                dataTyper, printer
        );
        commands.put(getCommandName.apply(currentCommand), currentCommand);

        currentCommand = new UpdateByIdCommand(
                collectionService, validatorProvider,
                dataTyper, printer
        );
        commands.put(getCommandName.apply(currentCommand), currentCommand);

        currentCommand = new RemoveByIdCommand(
                collectionService, validatorProvider, printer
        );
        commands.put(getCommandName.apply(currentCommand), currentCommand);

        currentCommand = new ClearCommand(collectionService, printer);
        commands.put(getCommandName.apply(currentCommand), currentCommand);

        currentCommand = new SaveCommand(
                collectionService, printer, environmentProvider, fileWriter
        );
        commands.put(getCommandName.apply(currentCommand), currentCommand);

        currentCommand = new ExecuteScriptCommand(
                validatorProvider, dataProvider,
                readerFactory, commandInputReaders, printer
        );
        commands.put(getCommandName.apply(currentCommand), currentCommand);

        currentCommand = new ExitCommand(collectionService, printer);
        commands.put(getCommandName.apply(currentCommand), currentCommand);

        currentCommand = new AddIfMinCommand(
                collectionService, validatorProvider,
                dataTyper, printer
        );
        commands.put(getCommandName.apply(currentCommand), currentCommand);

        currentCommand = new RemoveGreaterCommand(
                collectionService, validatorProvider, printer
        );
        commands.put(getCommandName.apply(currentCommand), currentCommand);

        currentCommand = new RemoveLowerCommand(
                collectionService, validatorProvider, printer
        );
        commands.put(getCommandName.apply(currentCommand), currentCommand);

        currentCommand = new MaxByEnginePowerCommand(
                collectionService, printer, formatter
        );
        commands.put(getCommandName.apply(currentCommand), currentCommand);

        currentCommand = new GroupCountingByDistanceTravelledCommand(
                collectionService, printer, formatter
        );
        commands.put(getCommandName.apply(currentCommand), currentCommand);

        currentCommand = new CountLessThanTypeCommand(
                collectionService, validatorProvider, printer
        );
        commands.put(getCommandName.apply(currentCommand), currentCommand);

        return commands;
    }
}
