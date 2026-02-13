package ru.aston.finalproject.util;

import org.apache.commons.lang3.StringUtils;
import ru.aston.finalproject.environment.AppException;

import static ru.aston.finalproject.util.Message.X_CANNOT_BE_EMPTY;

public class ConstantMethods {

    public static void checkedStringOnEmpty(String string, String field) {
        if (StringUtils.isBlank(string)) {
            throw new AppException(String.format(X_CANNOT_BE_EMPTY, field));
        }
    }
}