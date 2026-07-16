package ru.aston.finalproject.service.loader;

import org.apache.commons.lang3.StringUtils;
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
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Stream;


public class FileDataLoader<T> extends AbstractLoaderWithParser<T> {
    private static final String FILE_PATH_PARAMETER = "-path";
    private String nameSheet;

    public FileDataLoader(Parsing<T> parser) {
        super(parser);
    }

    public FileDataLoader(Parsing<T> parser, String nameSheet) {
        super(parser);
        this.nameSheet = nameSheet;
    }

    @Override
    public Stream<T> loadEntityList(Integer size, AppRequest request) {

        String filePath = request.getStringParameter(FILE_PATH_PARAMETER);

        if (nameSheet != null) {
            List<T> stockLists = new CustomArrayList<>();

            try (FileInputStream inputStream = new FileInputStream(filePath);
                 XSSFWorkbook workbook = new XSSFWorkbook(inputStream)) {

                XSSFSheet sheet = workbook.getSheet(nameSheet);
                Iterator<Row> rows = sheet.iterator();
                rows.next();
                while (rows.hasNext() && stockLists.size() < size) {
                    stockLists.add(parser.excelParser(rows.next()));
                }
            } catch (IOException | NoSuchElementException e) {
                throw new AppException(Message.FILE_INPUT_FAILED);
            }

            return stockLists.stream();
        } else {
            try {
                return Files.lines(Paths.get(filePath), StandardCharsets.UTF_8)
                        .filter(StringUtils::isNotBlank)
                        .map(this::parseEntity)
                        .filter(Objects::nonNull)
                        .limit(size);
            } catch (IOException | NoSuchElementException e) {
                throw new AppException(Message.FILE_INPUT_FAILED);
            }
        }
    }
}
