package ru.aston.finalproject.actions;

import ru.aston.finalproject.environment.appdata.AppData;
import ru.aston.finalproject.environment.AppException;
import ru.aston.finalproject.environment.AppRequest;

public class HelpAction<T> extends AppAction<T> {
    private static final Integer EXPECTED_MAX_PARAMETERS_AMOUNT = 0;

    @Override
    public void action(AppData<T> appData, AppRequest request) throws AppException {
        request.checkParametersAmount(EXPECTED_MAX_PARAMETERS_AMOUNT);

        for (Action action : Action.values()){
            System.out.println(action.name() + action.getInfo());
        }
    }
}
