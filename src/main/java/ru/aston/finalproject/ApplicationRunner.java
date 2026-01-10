package ru.aston.finalproject;

import org.apache.commons.lang3.StringUtils;
import ru.aston.finalproject.actions.*;
import ru.aston.finalproject.config.AppData;
import ru.aston.finalproject.config.AppException;
import ru.aston.finalproject.config.AppRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

public class ApplicationRunner {
    public final static String HELP = "help";
    public final static String LOAD_USERS = "load";
    public final static String PRINT_USERS = "print";
    public final static String SORT_USERS = "sort";
    public final static String WRITE_ALL_USERS = "write";

    private static final Map<String, AppAction> actionMap = Map.of(
            HELP, new HelpAction(),
            LOAD_USERS, new LoadAction(),
            PRINT_USERS, new PrintAction(),
            WRITE_ALL_USERS, new WriteAction(),
            SORT_USERS, new SorterAction()
    );

    public static void main(String[] args) {
        try (BufferedReader inReader = new BufferedReader(new InputStreamReader(System.in))) {
            AppData appData = new AppData();

            while (true) {
                String rawInput = inReader.readLine();
                if (StringUtils.isBlank(rawInput)) {
                    continue;
                }

                AppRequest appRequest = AppRequest.createRequest(rawInput);
                if (appRequest.isExitRequest()) {
                    break;
                }

                String command = appRequest.getCommandName();
                if (actionMap.containsKey(command)) {
                    AppAction action = actionMap.get(command);
                    try {
                        action.action(appData, appRequest);
                    } catch (AppException exception) {
                        System.out.println(exception.getMessage());
                    }
                    continue;
                }

                System.out.println(String.format(
                        "Неверный синтаксис команды - \"%s\".",
                        command
                ));
            }
        } catch (IOException exception) {
            System.out.println(String.format(
                    "Ошибка ввода: %s", exception.getMessage()
            ));
        }


    }
}
