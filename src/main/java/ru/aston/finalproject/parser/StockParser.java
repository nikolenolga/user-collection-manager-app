package ru.aston.finalproject.parser;

import ru.aston.finalproject.entity.stock.Stock;
import ru.aston.finalproject.entity.validator.Validate;
import ru.aston.finalproject.environment.AppException;

import static ru.aston.finalproject.util.ConstantMethods.checkedStringOnEmpty;
import static ru.aston.finalproject.util.Message.X_CANNOT_BE_EMPTY;

public class StockParser extends AbstractParser<Stock> {

    private static final String DELIMITER = "\\s+";
    private static final int LENGTH_PARAMETER = 9;
    public static final String STOCK_FORMAT =
            "name nowValue maxValue minValue dividends pe(withPE) eps(withEPS) epsFrom5years buyInThisPeriod";
    private static final int NAME_PARAMETER = 0;
    private static final int NOW_VALUE_PARAMETER = 1;
    private static final int MAX_VALUE_PARAMETER = 2;
    private static final int MIN_VALUE_PARAMETER = 3;
    private static final int DIVIDENDS_PARAMETER = 4;
    private static final int PE_PARAMETER = 5;
    private static final int EPS_PARAMETER = 6;
    private static final int EPS_FROM_5_YEARS_PARAMETER = 7;
    private static final int BUY_IN_THIS_PERIOD_PARAMETER = 8;
    private final Validate<Stock.Builder> stockValidator;

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
}
