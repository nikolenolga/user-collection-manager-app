package ru.aston.finalproject.parser;

import org.apache.poi.ss.usermodel.Row;

public interface ParsingFromExcel<T> {

    T entityParser(Row currentRow);
}
