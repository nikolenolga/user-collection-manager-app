package ru.aston.finalproject.workwithentity;

public interface Builder<T> {

    public Builder<T> setField(String fieldType, String fieldName, Object value);

    public T build();
}
