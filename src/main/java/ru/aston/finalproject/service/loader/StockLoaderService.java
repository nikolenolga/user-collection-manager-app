package ru.aston.finalproject.service.loader;

import ru.aston.finalproject.entity.stock.Stock;

public class StockLoaderService extends LoaderService<Stock> {
    public static final String LOAD_FROM_FILE = "file";
    public static final String LOAD_FROM_CONSOLE = "console";
    public static final String LOAD_FROM_EXCEL = "excel";

    public StockLoaderService(FileDataLoader<Stock> stockFileDataLoader,
                              ConsoleDataLoader<Stock> stockConsoleDataLoader,
                              LoadStockFromExcel loadStockFromExcel) {
        addLoader(LOAD_FROM_FILE, stockFileDataLoader);
        addLoader(LOAD_FROM_CONSOLE, stockConsoleDataLoader);
        addLoader(LOAD_FROM_EXCEL, loadStockFromExcel);
    }
}
