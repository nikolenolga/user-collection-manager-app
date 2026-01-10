package ru.aston.finalproject.actions;

import org.apache.commons.lang3.ObjectUtils;
import ru.aston.finalproject.config.AppData;
import ru.aston.finalproject.config.AppException;
import ru.aston.finalproject.config.AppRequest;
import ru.aston.finalproject.entity.User;
import ru.aston.finalproject.util.Message;

import java.util.List;

public class PrintAction extends AppAction {
    private static final Integer EXPECTED_PARAMETERS_AMOUNT = 0;

    @Override
    public void action(AppData appData, AppRequest request) throws AppException {
        request.checkParametersAmount(EXPECTED_PARAMETERS_AMOUNT);

        List<User> userList = appData.getUserList();
        if (ObjectUtils.isEmpty(userList)) {
            System.out.println(Message.EXCEPTION_LIST_NOT_LOADED);
            return;
        }

        userList.stream()
                .map(appData.getUserParser()::parseToString)
                .forEach(System.out::println);
    }
}
