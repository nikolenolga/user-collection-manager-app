package ru.aston.finalproject.actions;

import ru.aston.finalproject.collection.CustomArrayList;
import ru.aston.finalproject.collection.CustomArrayListCollector;
import ru.aston.finalproject.entity.user.User;
import ru.aston.finalproject.environment.AppException;
import ru.aston.finalproject.environment.AppRequest;
import ru.aston.finalproject.environment.appdata.AppData;
import ru.aston.finalproject.service.counters.MultithreadCounter;

import java.util.List;

public class UsersCountAction extends AppAction<User> {
    private static final Integer EXPECTED_PARAMETERS_AMOUNT = 1;
    private static final String THREAD_COUNT_PARAMETER = "-threads";

    @Override
    public void action(AppData<User> appData, AppRequest request) throws AppException {
        request.checkParametersAmount(EXPECTED_PARAMETERS_AMOUNT);
        int threadCount = request.getIntegerParameter(THREAD_COUNT_PARAMETER);

        CustomArrayList<User> inputUsers = appData.getLoaderService()
                .loadEntityList("console", 1, request)
                .collect(CustomArrayListCollector.toCustomArrayList());
        User targetUser;
        try {
            targetUser = inputUsers.get(0);
        } catch (IndexOutOfBoundsException exception) {
            throw new AppException(exception.getMessage());
        }

        List<User> appUsers = appData.getEntityList();
        MultithreadCounter<User> counter = appData.getEntryCounter();
        int count = counter.count(appUsers, targetUser, threadCount);

        System.out.printf("Found %d entires.%n", count);
    }
}
