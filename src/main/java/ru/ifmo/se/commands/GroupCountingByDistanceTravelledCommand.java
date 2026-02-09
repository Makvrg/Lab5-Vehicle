package ru.ifmo.se.commands;

import ru.ifmo.se.io.input.readers.Reader;
import ru.ifmo.se.io.output.formatter.OutputStringFormatter;
import ru.ifmo.se.io.output.print.Printer;
import ru.ifmo.se.service.CollectionService;

import java.util.Map;

public class GroupCountingByDistanceTravelledCommand extends Command {

    private final CollectionService collectionService;
    private final Printer printer;
    private final OutputStringFormatter formatter;

    public GroupCountingByDistanceTravelledCommand(CollectionService collectionService,
                                                   Printer printer,
                                                   OutputStringFormatter formatter) {
        super("group_counting_by_distance_travelled",
                "сгруппировать элементы коллекции по значению поля distanceTravelled, " +
                        "вывести количество элементов в каждой группе"
        );
        this.collectionService = collectionService;
        this.printer = printer;
        this.formatter = formatter;
    }

    @Override
    public void execute(String[] ignoredArgs, Reader ignoredReader) {
        Map<Float, Integer> numberOfGroups = collectionService.groupCountingByDistanceTravelled();
        if (!numberOfGroups.isEmpty()) {
            printer.forcePrintln(formatter.formatNumberOfGroups(numberOfGroups));
        } else {
            printer.forcePrintln("Коллекция пуста");
        }
    }
}
