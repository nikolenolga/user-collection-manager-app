package ru.aston.finalproject.service.loader;

import ru.aston.finalproject.config.AppException;
import ru.aston.finalproject.config.AppRequest;
import ru.aston.finalproject.util.Message;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoaderService<T> {
    private final Map<String, DataLoader<T>> loaders = new HashMap<>();

    public List<T> loadEntityList(String loaderKey, Integer size, AppRequest request) {
        if (!loaders.containsKey(loaderKey)) {
            throw new AppException(Message.EXCEPTION_WRONG_LOADER_KEY_X);
        }
        return loaders.get(loaderKey).loadEntityList(size, request);
    }

    public void addLoader(String loaderKey, DataLoader<T> dataLoader) {
        loaders.put(loaderKey, dataLoader);
    }
}
