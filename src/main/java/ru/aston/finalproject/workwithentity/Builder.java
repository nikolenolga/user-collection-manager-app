package ru.aston.finalproject.workwithentity;

public interface Builder<T> {

    public Builder<T> setValueForField(String fieldName, Object value);

    public T build();
}
