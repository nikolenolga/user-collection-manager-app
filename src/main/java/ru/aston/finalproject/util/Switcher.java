package ru.aston.finalproject.util;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import ru.aston.finalproject.actions.Action;
import ru.aston.finalproject.actions.AppAction;
import ru.aston.finalproject.environment.AppException;
import ru.aston.finalproject.environment.AppRequest;
import ru.aston.finalproject.environment.appdata.AppData;
import ru.aston.finalproject.environment.appdata.Data;
import ru.aston.finalproject.environment.appdata.StockAppData;
import ru.aston.finalproject.environment.appdata.UserAppData;

import java.io.BufferedReader;
import java.io.IOException;

import static ru.aston.finalproject.parser.StockParser.STOCK_FORMAT;
import static ru.aston.finalproject.parser.UserParser.USER_FORMAT;

public class Switcher {

    @Getter
    private AppData<?> appData;

    @Getter
    private boolean running = true;

    public void handleEntitySelection(BufferedReader reader) throws IOException {
        startMessage();
        String input = readInput(reader);

        try {
            appData = Data.valueOf(input).getAppData();
            System.out.println("The selected entity: " + input);
        } catch (IllegalArgumentException e) {
            System.out.println(Message.INVALID_DATA);
        }

        if (appData instanceof UserAppData) {
            System.out.printf((Message.ENTER_USERS_EXPECTED_FORMAT_S) + "%n", USER_FORMAT);
        } else if (appData instanceof StockAppData) {
            System.out.printf((Message.ENTER_STOCKS_EXPECTED_FORMAT_S) + "%n", STOCK_FORMAT);
        }
    }

    public void handleUserCommand(BufferedReader reader) throws IOException {
        String input = readInput(reader);

        if (StringUtils.isBlank(input)) {
            return;
        }

        AppRequest appRequest = AppRequest.createRequest(input);

        if (appRequest.isChangeEntityRequest()) {
            appData = null;
            return;
        }

        if (appRequest.isExitRequest()) {
            running = false;
        } else {

            String command = appRequest.getCommandName();

            try {
                AppAction appAction = Action.valueOf(command).getAppAction();
                appAction.action(appData, appRequest);
            } catch (AppException exception) {
                System.out.println("Error: " + exception.getMessage());
            } catch (ClassCastException e) {
                System.out.println("Error: This command is not supported for the current entity");
                System.out.println("Use the 'change' command to change the entity");
            } catch (IllegalArgumentException e) {
                System.out.printf((Message.WRONG_REQUEST_SYNTAXES_X) + "%n", command);
                System.out.println("\n" + Message.COMMAND_FOR_HELP);
            }
        }
    }

    private String readInput(BufferedReader reader) throws IOException {
        String input = reader.readLine();
        if (input == null) {
            System.out.println("\nShut down...");
            System.exit(0);
        }
        return input.trim();
    }

    private void startMessage() {
        System.out.println("\n=== Select an entity to work with ===");
        System.out.print("Available entities: ");
        for (Data data : Data.values()) {
            System.out.print(data.name() + " ");
        }
        System.out.println();
        System.out.print("Enter the name of the entity : ");
    }
}
