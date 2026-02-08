package ru.ifmo.se.commands;

import ru.ifmo.se.io.input.readers.Reader;
import ru.ifmo.se.io.input.readers.factory.ReaderCreateException;
import ru.ifmo.se.io.input.readers.factory.ReaderFactory;
import ru.ifmo.se.io.input.readers.file.DataProvider;
import ru.ifmo.se.io.output.print.Printer;
import ru.ifmo.se.validator.CommandValidatorProvider;
import ru.ifmo.se.validator.exceptions.ExecuteScriptValidateException;

import java.util.List;

public class ExecuteScriptCommand extends Command {

    private final CommandValidatorProvider validatorProvider;
    private final DataProvider dataProvider;
    private final ReaderFactory readerFactory;
    private final List<Reader> commandInputReaders;
    private final Printer printer;

    public ExecuteScriptCommand(CommandValidatorProvider validatorProvider,
                                DataProvider dataProvider,
                                ReaderFactory readerFactory,
                                List<Reader> commandInputReaders,
                                Printer printer) {
        super("execute_script file_name",
                "считать и исполнить скрипт из указанного файла. В скрипте содержатся "
                        + "команды в таком же виде, в котором их вводит пользователь "
                        + "в интерактивном режиме."
        );
        this.validatorProvider = validatorProvider;
        this.dataProvider = dataProvider;
        this.readerFactory = readerFactory;
        this.commandInputReaders = commandInputReaders;
        this.printer = printer;
    }

    @Override
    public void execute(String[] inputArgs, Reader ignoredReader) {
        String fileName = (inputArgs.length > 1) ? inputArgs[1] : null;
        try {
            validatorProvider.getDataValidator().validateExecuteScript(fileName);
        } catch (ExecuteScriptValidateException e) {
            printer.forcePrintln(e.getMessage());
            return;
        }
        Reader currentFileReader;
        try {
            currentFileReader = readerFactory.createFileReader(
                    fileName,
                    dataProvider
            );
        } catch (ReaderCreateException e) {
            printer.forcePrintln(
                    "Файл с указанным названием не найден или к нему нет доступа");
            return;
        }

        for (Reader reader : commandInputReaders) {
            if (reader.getName().equals(currentFileReader.getName())) {
                printer.forcePrintln(
                        "Во избежание рекурсии выполняется "
                                + "принудительное завершение всей цепочки скриптов");
                while (commandInputReaders.size() > 1) {
                    commandInputReaders.remove(commandInputReaders.size() - 1);
                }
                printer.forcePrintln("Активен режим чтения терминала");
                printer.on();
                return;
            }
        }

        commandInputReaders.add(currentFileReader);
        printer.forcePrintln("Активен режим чтения файла " + currentFileReader.getName());
        printer.off();
    }
}
