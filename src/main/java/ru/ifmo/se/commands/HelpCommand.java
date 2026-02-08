package ru.ifmo.se.commands;

import ru.ifmo.se.io.input.readers.Reader;
import ru.ifmo.se.io.output.print.Printer;

import java.util.Collection;

public class HelpCommand extends Command {

    private final Printer printer;
    private final Collection<Command> commands;

    public HelpCommand(Printer printer, Collection<Command> commands) {
        super("help", "вывести справку по доступным командам");
        this.printer = printer;
        this.commands = commands;
    }

    @Override
    public void execute(String[] ignoredArgs, Reader ignoredReader) {
        StringBuilder helpText = new StringBuilder();
        helpText.append("Справка по командам приложения:\n");
        for (Command command : commands) {
            if (!command.getCommandSignature().equals("unknown")) {
                helpText.append(command.getCommandSignature())
                        .append(" : ")
                        .append(command.getCommandDescription())
                        .append("\n\n");
            }
        }
        helpText.delete(helpText.length() - 2, helpText.length());
        printer.forcePrintln(helpText.toString());
    }
}
