package ru.aston.finalproject.actions;

import ru.aston.finalproject.config.AppData;
import ru.aston.finalproject.config.AppException;
import ru.aston.finalproject.config.AppRequest;

public abstract class AppAction {

    public abstract void action(AppData appData, AppRequest request) throws AppException;
}
