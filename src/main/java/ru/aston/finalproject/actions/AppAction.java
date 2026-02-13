package ru.aston.finalproject.actions;

import ru.aston.finalproject.environment.appdata.AppData;
import ru.aston.finalproject.environment.AppException;
import ru.aston.finalproject.environment.AppRequest;

public abstract class AppAction<T> {

    public abstract void action(AppData<T> appData, AppRequest request) throws AppException;
}
