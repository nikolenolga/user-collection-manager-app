package ru.aston.finalproject.entity.validator;

import ru.aston.finalproject.environment.AppException;
import ru.aston.finalproject.util.Message;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public interface Validate<T> {

    void validate(T t) throws AppException;

    default void checkedFieldsNotNull(T t) {
        for (Field field : getAllFields(t.getClass())) {
            try {
                field.setAccessible(true);
                if (field.get(t) == null) {
                    throw new AppException(String.format(Message.X_CANNOT_BE_EMPTY, field.getName()));
                }
            } catch (IllegalAccessException e) {
                throw new AppException(e.getMessage());
            }
        }
    }

    private List<Field> getAllFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();
        if (clazz == null || clazz == Object.class) {
            return fields;
        }
        fields.addAll(List.of(clazz.getDeclaredFields()));
        return fields;
    }
}
