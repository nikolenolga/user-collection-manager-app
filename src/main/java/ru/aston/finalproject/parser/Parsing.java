package ru.aston.finalproject.parser;

import org.apache.poi.ss.usermodel.Row;
public interface Parsing<T> {

    String parseToString(T t);

    T parse(String data);

    T parse(String data, String delimiter);

    T excelParser(Row currentRow);
}
