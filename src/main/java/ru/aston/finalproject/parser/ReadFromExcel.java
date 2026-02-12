package ru.aston.finalproject.parser;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ru.aston.finalproject.collection.CustomArrayList;
import ru.aston.finalproject.entity.stock.Stock;
import ru.aston.finalproject.entity.validator.StockBuilderValidator;
import ru.aston.finalproject.entity.validator.Validate;
import ru.aston.finalproject.environment.AppException;
import ru.aston.finalproject.util.Message;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class ReadFromExcel {

    private final Validate<Stock.Builder> stockValidator;
    private final String nowSpace = "";

    public ReadFromExcel(Validate<Stock.Builder> stockValidator) {
        this.stockValidator = stockValidator;
    }

    public List<Stock> readXlsStocks(String filePath) {

        List<Stock> stockLists = new CustomArrayList<>();

        try (FileInputStream inputStream = new FileInputStream(filePath);
             XSSFWorkbook workbook = new XSSFWorkbook(inputStream)) {

            XSSFSheet sheet = workbook.getSheet("stocks");
            Iterator<Row> rows = sheet.iterator();
            rows.next();

            while (rows.hasNext()) {
                stockLists.add(stockBuilder(rows.next()));
            }

        } catch (IOException | NoSuchElementException e) {
            throw new AppException(Message.FILE_INPUT_FAILED);
        }
        return stockLists;
    }

    private Stock stockBuilder(Row currentRow) {
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
        return nowSpace + currentRow.getCell(index).getNumericCellValue();
    }

    private String changeBooleanToString(Row currentRow, int index) {
        return nowSpace + currentRow.getCell(index).getBooleanCellValue();
    }
}