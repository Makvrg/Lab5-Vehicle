package ru.ifmo.se.application;

import ru.ifmo.se.io.output.print.CollectionActionsMessages;

import java.io.IOException;

public class AppStarter {

    private final AppCompositionRoot appCompositionRoot =
            new AppCompositionRoot();

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
