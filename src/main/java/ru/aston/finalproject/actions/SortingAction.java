package ru.aston.finalproject.actions;

import org.apache.commons.lang3.ObjectUtils;
import ru.aston.finalproject.collection.CustomArrayList;
import ru.aston.finalproject.entity.user.User;
import ru.aston.finalproject.environment.appdata.AppData;
import ru.aston.finalproject.environment.AppException;
import ru.aston.finalproject.environment.AppRequest;
import ru.aston.finalproject.environment.appdata.UserAppData;
import ru.aston.finalproject.util.Message;

import java.util.List;

public class SortingAction<T extends Comparable<T>> extends AppAction<T> {
    private static final Integer EXPECTED_PARAMETERS_AMOUNT = 1;
    private static final String COMMAND_PARAMETER_BASIC = "-basic";
    private static final String COMMAND_PARAMETER_USER = "-user";

    @Override
    public void action(AppData<T> appData, AppRequest request) throws AppException {
        request.checkParametersAmount(EXPECTED_PARAMETERS_AMOUNT);
        List<T> list = appData.getEntityList();

        if (!request.containsParameter(COMMAND_PARAMETER_BASIC) && !request.containsParameter(COMMAND_PARAMETER_USER)) {
            throw new AppException(Message.WRONG_REQUEST_PARAMETER_SYNTAXES);
        }

        if (ObjectUtils.isEmpty(list)) {
            throw new AppException(Message.LIST_NOT_LOADED);
        }

        if (request.containsParameter(COMMAND_PARAMETER_BASIC)) {
            list = appData.getSorter().sort(list);
            appData.setEntityList(list);
        } else if (request.containsParameter(COMMAND_PARAMETER_USER)) {
            List<User> userList = new CustomArrayList<>();
            for (T t : list) {
                if (t instanceof User) {
                    userList.add((User)t);
                }
            }
            userList = appData.getStrangeSorter().sort(userList, User::getAge);
            ((UserAppData)appData).setEntityList(userList);
        }


        System.out.println(Message.ENTITIES_SORTED);
    }
}