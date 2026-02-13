package ru.aston.finalproject.parser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.aston.finalproject.entity.stock.Stock;
import ru.aston.finalproject.entity.validator.StockBuilderValidator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class StockParserTest {

    private Parsing<Stock> parsing;

    @BeforeEach
    public void setup() {
        parsing = new StockParser(new StockBuilderValidator());
    }

    @Test
    public void givenValidDataInStringWhitDefaultDelimiter_whenParse_thenReturnUserNotNull() {
        String input = "ГруппаЧеркизово 3446 4745 2911 true pe13.46 eps255.93 199.7 true";
        Stock resalt = parsing.parse(input);
        assertNotNull(resalt);
    }

    @Test
    public void givenValidDataInStringWhitCustomDelimiter_whenParse_thenReturnUserNotNull() {
        String input = "ГруппаЧеркизово/3446/4745/2911/true/pe13.46/eps255.93/199.7/true";
        String delimiter = "/";
        Stock resalt = parsing.parse(input, delimiter);
        assertNotNull(resalt);
    }

    @Test
    public void givenValidDataInStringWhitCustomDelimiter_whenParse_thenReturnString() {
        String input = "ГруппаЧеркизово 3446 4745 2911 true pe13.46 eps255.93 199.7 true";
        Stock resalt = parsing.parse(input);
        String serialized = parsing.parseToString(resalt);
        assertEquals("ГруппаЧеркизово 3446 4745 2911 true pe13.46 eps255.93 199.7 true", serialized);
    }
}
