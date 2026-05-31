package ru.aston.finalproject.parser;

import org.apache.poi.ss.usermodel.Row;
import ru.aston.finalproject.environment.AppException;

import static ru.aston.finalproject.util.ConstantFields.SPACE;
import static ru.aston.finalproject.util.ConstantMethods.checkedStringOnEmpty;
import static ru.aston.finalproject.util.Message.DATA_AT_INDEX_X;
import static ru.aston.finalproject.util.Message.INVALID_DATA_X;
import static ru.aston.finalproject.util.Message.X_IS_NOT_A_VALID_X;

public abstract class AbstractParser<T> implements Parsing<T>, ParsingFromExcel<T> {

    private static final String NO_DIGITS_REGS = "\\D+";
    private final String empty = "";

    protected String[] preparingForParsing(String data, String delimiter, int lengthParameter) {
        String[] dataArray = data.split(delimiter);

        if (dataArray.length != lengthParameter) {
            throw new AppException(String.format(INVALID_DATA_X, data));
        }
        for (int i = 0; i < lengthParameter; i++) {
            checkedStringOnEmpty(dataArray[i], String.format(DATA_AT_INDEX_X, i));
        }
        return dataArray;
    }

    protected int createdDigitFromFirstInteger(String stringWithDigit, String nameParameter) {
        try {
            return Integer.parseInt(stringDigitFromFirstInteger(stringWithDigit, nameParameter));
        } catch (NumberFormatException e) {
            throw new AppException(String.format(INVALID_DATA_X, stringWithDigit));
        }
    }

    protected String stringDigitFromFirstInteger(String stringWithDigit, String nameParameter) {
        checkedStringContainDigitsOnly(stringWithDigit, nameParameter);
        return stringWithDigit.trim().split(SPACE)[0];
    }

    private void checkedStringContainDigitsOnly(String string, String nameParameter) {
        checkedStringOnEmpty(string, nameParameter);
        String onlyDigits = string.replaceAll(NO_DIGITS_REGS, "").trim();
        if (onlyDigits.trim().isEmpty()) {
            throw new AppException(String.format(X_IS_NOT_A_VALID_X, string, nameParameter));
        }
    }

    protected String[] replaceToDot(String[] stockConstr) {
        for (int i = 1; i < stockConstr.length; i++) {
            stockConstr[i] = stockConstr[i].replace(",", ".");
        }
        return stockConstr;
    }

    protected String cellWithString(Row currentRow, int index) {
        if (currentRow.getCell(index) != null) {
            return currentRow.getCell(index).getStringCellValue();
        } else return empty;
    }

    protected String changeNumericToString(Row currentRow, int index) {
        if (currentRow.getCell(index) != null) {
            return empty + currentRow.getCell(index).getNumericCellValue();
        } else return empty;

    }

    protected String changeBooleanToString(Row currentRow, int index) {
        if (currentRow.getCell(index) != null) {
            return empty + currentRow.getCell(index).getBooleanCellValue();
        } else return empty;

    }
}
