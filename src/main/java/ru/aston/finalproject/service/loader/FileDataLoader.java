package ru.aston.finalproject.service.loader;

import ru.aston.finalproject.config.AppException;
import ru.aston.finalproject.config.AppRequest;
import ru.aston.finalproject.parser.Parsing;
import ru.aston.finalproject.util.Message;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileDataLoader<T> implements DataLoader<T> {
    private static final String FILE_PATH_PARAMETER = "-path";
    private final Parsing<T> parser;

    public FileDataLoader(Parsing<T> parser) {
        this.parser = parser;
    }

    @Override
    public List<T> loadEntityList(Integer size, AppRequest request) {
        String filePath = request.getStringParameter(FILE_PATH_PARAMETER);

        try (Stream<String> fileLines = Files.lines(Paths.get(filePath), StandardCharsets.UTF_8)) {
            return fileLines.limit(size)
                    .map(parser::parse)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new AppException(Message.EXCEPTION_FILE_NOT_FOUND_X.formatted(filePath));
        }
    }
}
