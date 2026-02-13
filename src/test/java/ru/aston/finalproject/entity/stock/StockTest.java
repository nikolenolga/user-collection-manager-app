package ru.aston.finalproject.entity.stock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import ru.aston.finalproject.entity.validator.StockBuilderValidator;
import ru.aston.finalproject.entity.validator.Validate;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StockTest {

    Validate<Stock.Builder> validate;

    @BeforeEach
    void setUp() {
        validate = new StockBuilderValidator();
    }

    @Nested
    class StockCreationTest {

        @Test
        void givenAllValidData_whenBuildStock_thenStockCreatedSuccessfully() {
            Stock stock = Stock.builder()
                    .setName("gazProm")
                    .setNowValue("75")
                    .setMaxValue("100")
                    .setMinValue("50")
                    .setEps("12")
                    .setPe("12")
                    .setEpsFrom5Years("138.24")
                    .setDividends("true")
                    .setBuyInThisPeriod("true")
                    .build(validate);

            assertNotNull(stock);
            assertEquals("gazProm", stock.getName());
            assertEquals(new BigDecimal("75"), stock.getNowValue());
            assertEquals(new BigDecimal("100"), stock.getMaxValue());
            assertEquals(new BigDecimal("50"), stock.getMinValue());
            assertEquals(new BigDecimal("12"), stock.getEps());
            assertEquals(new BigDecimal("12"), stock.getPe());
            assertEquals(new BigDecimal("138.24"), stock.getEpsFrom5Years());
            assertTrue(stock.isDividends());
            assertTrue(stock.isBuyInThisPeriod());
        }

    }
}
