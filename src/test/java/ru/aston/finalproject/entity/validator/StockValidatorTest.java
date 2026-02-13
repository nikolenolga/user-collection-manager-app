package ru.aston.finalproject.entity.validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.aston.finalproject.entity.stock.Stock;
import ru.aston.finalproject.environment.AppException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StockValidatorTest {

    StockBuilderValidator stockValidator;
    String name;

    @BeforeEach
    public void setUp() {
        stockValidator = new StockBuilderValidator();
        name = "name";
    }

    @Test
    public void givenValidData_whenValidate_thenSuccess() {
        Stock.Builder builder = Stock.builder().setName(name).setNowValue("75").setMaxValue("100").setMinValue("50")
                .setDividends("true").setPe("12").setEps("10").setEpsFrom5Years("1")
                .setBuyInThisPeriod("false");
        stockValidator.validate(builder);
    }

    @Test
    public void givenName_whenValidate_thenThrowsException() {
        Stock.Builder builder = Stock.builder().setName(name);
        AppException exception = assertThrows(AppException.class, () -> stockValidator.validate(builder));
        assertTrue(exception.getMessage().contains("empty"));
    }

    @Test
    public void givenNameNowValue_whenValidate_thenThrowsException() {
        Stock.Builder builder = Stock.builder().setName(name).setNowValue("75");
        AppException exception = assertThrows(AppException.class, () -> stockValidator.validate(builder));
        assertTrue(exception.getMessage().contains("empty"));
    }

    @Test
    public void givenNameNowValueMaxValue_whenValidate_thenThrowsException() {
        Stock.Builder builder = Stock.builder().setName(name).setNowValue("75").setMaxValue("100");
        AppException exception = assertThrows(AppException.class, () -> stockValidator.validate(builder));
        assertTrue(exception.getMessage().contains("empty"));
    }

    @Test
    public void givenMaxValueLessNowValue_whenValidate_thenThrowsException() {
        Stock.Builder builder = Stock.builder().setName(name).setNowValue("150").setMaxValue("100").setMinValue("50")
                .setDividends("true").setPe("12").setEps("10").setEpsFrom5Years("1")
                .setBuyInThisPeriod("false");
        AppException exception = assertThrows(AppException.class, () -> {
            stockValidator.validate(builder);
        });
        assertTrue(exception.getMessage().contains("can't be"));
    }

    @Test
    public void givenNowValueLessMinValue_whenValidate_thenThrowsException() {
        Stock.Builder builder = Stock.builder().setName(name).setNowValue("15").setMaxValue("100").setMinValue("50")
                .setDividends("true").setPe("12").setEps("10").setEpsFrom5Years("1")
                .setBuyInThisPeriod("false");
        AppException exception = assertThrows(AppException.class, () -> {
            stockValidator.validate(builder);
        });
        assertTrue(exception.getMessage().contains("can't be"));
    }
}