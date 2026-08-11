package ru.aston.finalproject.service.loader;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ru.aston.finalproject.collection.CustomArrayList;
import ru.aston.finalproject.environment.AppException;
import ru.aston.finalproject.environment.AppRequest;
import ru.aston.finalproject.parser.Parsing;
import ru.aston.finalproject.util.Message;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

public class LoadFromExcel<T> implements DataLoader<T> {

    private static final String FILE_PATH_PARAMETER = "-path";
    private static final String NAME_SHEET = "-sheet";
    private final Parsing<T> parsing;

    public LoadFromExcel(Parsing<T> parsing) {
        this.parsing = parsing;
    }

    @Override
    public Stream<T> loadEntityList(Integer size, AppRequest request) {

        String filePath = request.getStringParameter(FILE_PATH_PARAMETER);
        String nameSheet = request.getStringParameter(NAME_SHEET);
        List<T> stockLists = new CustomArrayList<>();

        try (FileInputStream inputStream = new FileInputStream(filePath);
             XSSFWorkbook workbook = new XSSFWorkbook(inputStream)) {

            XSSFSheet sheet = workbook.getSheet(nameSheet);
            if (sheet != null) {
                Iterator<Row> rows = sheet.iterator();
                rows.next();
                while (rows.hasNext() && stockLists.size() < size) {
                    stockLists.add(parsing.excelParser(rows.next()));
                }
            }
            else System.out.println("Name sheet: \"" + nameSheet + "\", is incorrect");
        } catch (IOException | NoSuchElementException e) {
            throw new AppException(Message.FILE_INPUT_FAILED);
        }

        return stockLists.stream();
    }
}
