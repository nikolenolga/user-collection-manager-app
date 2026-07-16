package ru.aston.finalproject.entity.validator;

import ru.aston.finalproject.entity.stock.Stock;
import ru.aston.finalproject.environment.AppException;

import java.math.BigDecimal;

import static ru.aston.finalproject.util.Message.INVALID_DATA;

public class StockBuilderValidator implements Validate<Stock.Builder> {

    private BigDecimal nowValue;
    private BigDecimal maxValue;
    private BigDecimal minValue;

    @Override
    public void validate(Stock.Builder builder) throws AppException {
        checkedFieldsNotNull(builder);
        nowValue = builder.getNowValue();
        maxValue = builder.getMaxValue();
        minValue = builder.getMinValue();
        checkedNowValue(builder.getName(), builder.getNowValue());
        checkedMaxValue(builder.getName(), builder.getMaxValue());
        checkedMinValue(builder.getName(), builder.getMinValue());
    }

    private void checkedNowValue(String nameStock, BigDecimal value) {
        if (value == null) {
            throw new AppException(INVALID_DATA);
        }
        if (nowValue.compareTo(maxValue) > 0) {
            throw new AppException(nameStock + " nowValue: " + value + " can't be greater than maxValue");
        }
        if (nowValue.compareTo(minValue) < 0) {
            throw new AppException(nameStock + " nowValue can't be less than minValue");
        }
    }

    private void checkedMaxValue(String nameStock, BigDecimal value) {
        if (value == null) {
            throw new AppException(INVALID_DATA);
        }
        value = nowValue.max(maxValue).max(minValue);
        if (maxValue.compareTo(value) != 0) {
            throw new AppException(nameStock + " maxValue can't be less than all Values");
        }
    }

    private void checkedMinValue(String nameStock, BigDecimal value) {
        if (value == null) {
            throw new AppException(INVALID_DATA);
        }
        value = nowValue.min(maxValue).min(minValue);
        if (minValue.compareTo(value) != 0) {
            throw new AppException(nameStock + " minValue can't be greater than all Values");
        }
    }
}