package ru.aston.finalproject.actions;

import ru.aston.finalproject.collection.CustomArrayList;
import ru.aston.finalproject.collection.CustomArrayListCollector;
import ru.aston.finalproject.environment.appdata.AppData;
import ru.aston.finalproject.environment.AppException;
import ru.aston.finalproject.environment.AppRequest;
import ru.aston.finalproject.util.Message;

public class LoadAction<T> extends AppAction<T> {

    private static final Integer EXPECTED_MAX_PARAMETERS_AMOUNT = 3;
    private static final String SIZE_PARAMETER = "-size";
    private static final String LOADER_TYPE_PARAMETER = "-type";

    @Override
    public void action(AppData<T> appData, AppRequest request) throws AppException {
        request.checkParametersAmount(EXPECTED_MAX_PARAMETERS_AMOUNT);
        Integer size = request.getIntegerParameter(SIZE_PARAMETER);
        String loaderKey = request.getStringParameter(LOADER_TYPE_PARAMETER);

        CustomArrayList<T> entities = appData.getLoaderService()
                .loadEntityList(loaderKey, size, request)
                .collect(CustomArrayListCollector.toCustomArrayList());

        appData.getEntityList().addAll(entities);

        System.out.println(Message.X_ENTITIES_LOADED.formatted(entities.size()));
    }
}
