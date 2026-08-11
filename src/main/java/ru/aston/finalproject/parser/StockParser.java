package ru.aston.finalproject.parser;

import org.apache.poi.ss.usermodel.Row;
import ru.aston.finalproject.entity.stock.Stock;
import ru.aston.finalproject.entity.validator.Validate;
import ru.aston.finalproject.environment.AppException;

import static ru.aston.finalproject.util.ConstantMethods.checkedStringOnEmpty;
import static ru.aston.finalproject.util.Message.X_CANNOT_BE_EMPTY;

public class StockParser extends AbstractParser<Stock> {

    private final Validate<Stock.Builder> stockValidator;

    public static final String STOCK_FORMAT =
            "name nowValue maxValue minValue dividends pe(withPE) eps(withEPS) epsFrom5years buyInThisPeriod";

    private static final String DELIMITER = "\\s+";
    private static final int LENGTH_PARAMETER = 9;
    private final int NAME_PARAMETER = 0;
    private final int NOW_VALUE_PARAMETER = 1;
    private final int MAX_VALUE_PARAMETER = 2;
    private final int MIN_VALUE_PARAMETER = 3;
    private final int DIVIDENDS_PARAMETER = 4;
    private final int PE_PARAMETER = 5;
    private final int EPS_PARAMETER = 6;
    private final int EPS_FROM_5_YEARS_PARAMETER = 7;
    private final int BUY_IN_THIS_PERIOD_PARAMETER = 8;

    public StockParser(Validate<Stock.Builder> stockValidator) {
        this.stockValidator = stockValidator;
    }

    @Override
    public String parseToString(Stock stock) {
        if (stock == null) {
            throw new AppException(String.format(X_CANNOT_BE_EMPTY, "Stock"));
        }
        return stock.getName() + " " + stock.getNowValue() + " " + stock.getMaxValue() + " " + stock.getMinValue() +
                " " + stock.isDividends() + " pe" + stock.getPe() + " eps" + stock.getEps() + " " +
                stock.getEpsFrom5Years() + " " + stock.isBuyInThisPeriod();
    }

    @Override
    public Stock parse(String data) {
        return parse(data, DELIMITER);
    }

    @Override
    public Stock parse(String data, String delimiter) {

        checkedStringOnEmpty(data, "data in parser");
        String[] dataArray = preparingForParsing(data, delimiter, LENGTH_PARAMETER);
        dataArray = replaceToDot(dataArray);
        String name = dataArray[NAME_PARAMETER];
        String nowValue = stringDigitFromFirstInteger(dataArray[NOW_VALUE_PARAMETER], "nowValue");
        String maxValue = stringDigitFromFirstInteger(dataArray[MAX_VALUE_PARAMETER], "maxValue");
        String minValue = stringDigitFromFirstInteger(dataArray[MIN_VALUE_PARAMETER], "minValue");
        String dividends = dataArray[DIVIDENDS_PARAMETER];
        String pe = stringDigitFromFirstInteger(dataArray[PE_PARAMETER].substring(2), "PE");
        String eps = stringDigitFromFirstInteger(dataArray[EPS_PARAMETER].substring(3), "EPS");
        String epsFrom5Years =
                stringDigitFromFirstInteger(dataArray[EPS_FROM_5_YEARS_PARAMETER], "epsFrom5Years");
        String buyInThisPeriod = dataArray[BUY_IN_THIS_PERIOD_PARAMETER];

        return Stock.builder().setName(name).setNowValue(nowValue).setMaxValue(maxValue).setMinValue(minValue)
                .setDividends(dividends).setPe(pe).setEps(eps).setEpsFrom5Years(epsFrom5Years)
                .setBuyInThisPeriod(buyInThisPeriod).build(stockValidator);
    }

    @Override
    public Stock excelParser(Row currentRow) {
        return Stock.builder()
                .setName(cellWithString(currentRow, NAME_PARAMETER))
                .setNowValue(changeNumericToString(currentRow, NOW_VALUE_PARAMETER))
                .setMaxValue(changeNumericToString(currentRow, MAX_VALUE_PARAMETER))
                .setMinValue(changeNumericToString(currentRow, MIN_VALUE_PARAMETER))
                .setDividends(changeBooleanToString(currentRow, DIVIDENDS_PARAMETER))
                .setPe(changeNumericToString(currentRow, PE_PARAMETER))
                .setEps(changeNumericToString(currentRow, EPS_PARAMETER))
                .setEpsFrom5Years(changeNumericToString(currentRow, EPS_FROM_5_YEARS_PARAMETER))
                .setBuyInThisPeriod(changeBooleanToString(currentRow, BUY_IN_THIS_PERIOD_PARAMETER))
                .build(stockValidator);
    }
}
