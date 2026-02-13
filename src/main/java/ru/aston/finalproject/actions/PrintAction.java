package ru.aston.finalproject.actions;

import org.apache.commons.lang3.ObjectUtils;
import ru.aston.finalproject.environment.AppException;
import ru.aston.finalproject.environment.AppRequest;
import ru.aston.finalproject.environment.appdata.AppData;
import ru.aston.finalproject.util.Message;

import java.io.PrintWriter;
import java.util.List;

public class PrintAction<T> extends AppAction<T> {
    private static final Integer EXPECTED_MAX_PARAMETERS_AMOUNT = 0;

    @Override
    public void action(AppData<T> appData, AppRequest request) throws AppException {
        request.checkParametersAmount(EXPECTED_MAX_PARAMETERS_AMOUNT);

        List<T> entityList = appData.getEntityList();
        if (ObjectUtils.isEmpty(entityList)) {
            System.out.println(Message.LIST_NOT_LOADED);
        }

        PrintWriter writer = new PrintWriter(System.out, true);
        entityList.stream()
                .map(appData.getParser()::parseToString)
                .forEach(writer::println);
    }
}
