package ru.aston.finalproject.environment.appdata;

import lombok.Getter;

public enum Data {
    user(new UserAppData()),
    stock(new StockAppData());

    @Getter
    private final AppData<?> appData;

    Data(AppData<?> appData) {
        this.appData = appData;
    }
}
