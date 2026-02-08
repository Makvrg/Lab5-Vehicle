package ru.ifmo.se.commands;

import ru.ifmo.se.io.input.readers.Reader;
import ru.ifmo.se.io.output.print.Printer;

public class UnknownCommand extends Command {

    private final Printer printer;

    public UnknownCommand(Printer printer) {
        super("unknown",
                "вызывается автоматически при вводе команды, "
                        + "которая не поддерживается программой"
        );
        this.printer = printer;
    }

    @Override
    public void execute(String[] inputArgs, Reader ignoredReader) {
        printer.forcePrintln(
                String.format(
                        "Передана неизвестная команда: %s", inputArgs[0]
                )
        );
    }
}
