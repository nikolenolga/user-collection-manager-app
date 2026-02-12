package ru.aston.finalproject.parser;

import org.apache.poi.ss.usermodel.Row;
import ru.aston.finalproject.entity.stock.Stock;
import ru.aston.finalproject.entity.validator.Validate;

public class StockParserFromExcel {

    private final Validate<Stock.Builder> stockValidator;
    private final String nowSpace = "";

    public StockParserFromExcel(Validate<Stock.Builder> stockValidator) {
        this.stockValidator = stockValidator;
    }

    public Stock stockBuilder(Row currentRow) {

        return Stock.builder()
                .setName(setField(currentRow, 0))
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

    private String setField(Row currentRow, int index) {
        return currentRow.getCell(index).getStringCellValue();
    }

    private String changeNumericToString(Row currentRow, int index) {
        return nowSpace + currentRow.getCell(index).getCellType();
    }

    private String changeBooleanToString(Row currentRow, int index) {
        return nowSpace + currentRow.getCell(index).getBooleanCellValue();
    }
}
