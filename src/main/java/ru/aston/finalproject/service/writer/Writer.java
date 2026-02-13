package ru.aston.finalproject.service.writer;

import lombok.NonNull;

import java.util.List;

public interface Writer<T> {

    public void write(@NonNull List<T> items, String filePath);
}
