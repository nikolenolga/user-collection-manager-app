package ru.aston.finalproject.service.loader;

import ru.aston.finalproject.entity.stock.Stock;
import ru.aston.finalproject.entity.validator.Validate;
import ru.aston.finalproject.environment.AppRequest;
import ru.aston.finalproject.parser.ReadStockFromExcel;

import java.util.stream.Stream;

public class LoadStockFromExcel implements DataLoader<Stock> {

    private static final String FILE_PATH_PARAMETER = "-xlsx";
    private final Validate<Stock.Builder> validate;

    public LoadStockFromExcel(Validate<Stock.Builder> validate) {
        this.validate = validate;
    }

    @Override
    public Stream<Stock> loadEntityList(Integer size, AppRequest request) {

        String filePath = request.getStringParameter(FILE_PATH_PARAMETER);
        ReadStockFromExcel readFromExcel = new ReadStockFromExcel(validate);

        return readFromExcel.creatListStocks(filePath, "stocks").stream().limit(size);
    }
}
