package ru.aston.finalproject;

import ru.aston.finalproject.environment.appdata.AppData;
import ru.aston.finalproject.util.Message;
import ru.aston.finalproject.util.Switcher;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class AppRunner {

    public static void main(String[] args) {
        try (BufferedReader inReader = new BufferedReader(new InputStreamReader(System.in))) {
            runApplication(inReader);
        } catch (IOException exception) {
            System.out.printf((Message.INPUT_ERROR_X) + "%n", exception.getMessage());
        }
    }

    private static void runApplication(BufferedReader reader) throws IOException {
        Switcher switcher = new Switcher();

        while (switcher.isRunning()) {
            AppData<?> appData = switcher.getAppData();
            try {
                if (appData == null) {
                    switcher.handleEntitySelection(reader);
                } else {
                    switcher.handleUserCommand(reader);
                }
            } catch (IllegalStateException e) {
                System.out.println(e.getMessage());
//                e.printStackTrace();
            }
        }
    }
}