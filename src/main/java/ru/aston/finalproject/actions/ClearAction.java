package ru.aston.finalproject.actions;

import ru.aston.finalproject.environment.appdata.AppData;
import ru.aston.finalproject.environment.AppException;
import ru.aston.finalproject.environment.AppRequest;
import ru.aston.finalproject.util.Message;

public class ClearAction<T> extends AppAction<T> {
    private static final Integer EXPECTED_MAX_PARAMETERS_AMOUNT = 0;

    @Override
    public void action(AppData<T> appData, AppRequest request) throws AppException {
        request.checkParametersAmount(EXPECTED_MAX_PARAMETERS_AMOUNT);
        appData.getEntityList().clear();

        System.out.println(Message.ENTITIES_CLEARED);
    }
}
