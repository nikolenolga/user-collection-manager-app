package ru.aston.finalproject.parser;

import org.apache.poi.ss.usermodel.Row;
import ru.aston.finalproject.entity.stock.Stock;
import ru.aston.finalproject.entity.validator.Validate;

public class ParsingStockFromExcel implements ParsingFromExcel<Stock> {

    private final Validate<Stock.Builder> stockValidator;
    private static final int cellWithName = 0;
    private final String empty = "";

    public ParsingStockFromExcel(Validate<Stock.Builder> stockValidator) {
        this.stockValidator = stockValidator;
    }

    @Override
    public Stock entityParser(Row currentRow) {
        return Stock.builder()
                .setName(nameStock(currentRow))
                .setNowValue(changeNumericToString(currentRow, 1))
                .setMaxValue(changeNumericToString(currentRow, 2))
                .setMinValue(changeNumericToString(currentRow, 3))
                .setDividends(changeBooleanToString(currentRow, 4))
                .setPe(changeNumericToString(currentRow, 5))
                .setEps(changeNumericToString(currentRow, 6))
                .setEpsFrom5Years(changeNumericToString(currentRow, 7))
                .setBuyInThisPeriod(changeBooleanToString(currentRow, 8))
                .build(stockValidator);
    }


    private String nameStock(Row currentRow) {
        return currentRow.getCell(cellWithName).getStringCellValue();
    }

    private String changeNumericToString(Row currentRow, int index) {
        return empty + currentRow.getCell(index).getNumericCellValue();
    }

    private String changeBooleanToString(Row currentRow, int index) {
        return empty + currentRow.getCell(index).getBooleanCellValue();
    }
}
