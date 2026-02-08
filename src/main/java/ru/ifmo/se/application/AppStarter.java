package ru.ifmo.se.application;

import ru.ifmo.se.io.output.print.CollectionActionsMessages;

import java.io.IOException;

public class AppStarter implements Starter {

    private final AppCompositionRoot appCompositionRoot =
            new AppCompositionRoot();

    @Override
    public void start() {
        appCompositionRoot.getCollectionService()
                          .addShutdownListener(
                                  appCompositionRoot.getCommandInput()
                );
        try {
            appCompositionRoot.getCommandInput().initialize();
        } catch (IOException e) {
            appCompositionRoot.getPrinter().forcePrintln(
                    CollectionActionsMessages.VEHICLE_INIT_OPEN_FILE_EXC
                            + e.getMessage()
            );
        }
        appCompositionRoot.getCommandInput().run();
    }
}
