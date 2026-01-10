package ru.aston.finalproject.actions;

import org.apache.commons.lang3.ObjectUtils;
import ru.aston.finalproject.config.AppData;
import ru.aston.finalproject.config.AppException;
import ru.aston.finalproject.config.AppRequest;
import ru.aston.finalproject.entity.User;
import ru.aston.finalproject.service.sorting.MergeSorter;
import ru.aston.finalproject.service.sorting.StrangeSorter;
import ru.aston.finalproject.util.Message;

import java.util.List;

public class SorterAction extends AppAction {
    private static final Integer EXPECTED_PARAMETERS_AMOUNT = 1;
    private static final String BASIC_PARAMETER = "-basic";
    private static final String STRANGE_PARAMETER = "-strange";

    @Override
    public void action(AppData appData, AppRequest request) throws AppException {
        request.checkParametersAmount(EXPECTED_PARAMETERS_AMOUNT);

        List<User> userList = appData.getUserList();
        if (ObjectUtils.isEmpty(userList)) {
            System.out.println(Message.EXCEPTION_LIST_NOT_LOADED);
        }

        if (request.containsParameter(BASIC_PARAMETER)) {
            if(!User.class.isAssignableFrom(Comparable.class)) {
                throw new AppException("Entity does not support basic sorting operation. Need to implement Comparable interface.");
            }
            MergeSorter<User> mergeSorter = appData.getMergeSorter();
            userList = mergeSorter.sort(userList);
        } else if (request.containsParameter(STRANGE_PARAMETER)) {
            StrangeSorter<User> strangeSorter = appData.getStrangeSorter();
            userList = strangeSorter.sort(userList, User::getAge);
        } else {
            throw new AppException(Message.EXCEPTION_WRONG_REQUEST_PARAMETER_SYNTAXES);
        }
        appData.setUserList(userList);

        System.out.println(Message.USERS_SORTED);
    }
}

