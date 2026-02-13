package ru.aston.finalproject.service.loader;

import org.apache.commons.lang3.StringUtils;
import ru.aston.finalproject.environment.AppException;
import ru.aston.finalproject.environment.AppRequest;
import ru.aston.finalproject.parser.Parsing;
import ru.aston.finalproject.util.Message;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Stream;


public class FileDataLoader<T> extends AbstractLoaderWithParser<T> {
    private static final String FILE_PATH_PARAMETER = "-path";

    public FileDataLoader(Parsing<T> parser) {
        super(parser);
    }

    @Override
    public Stream<T> loadEntityList(Integer size, AppRequest request) {
        String filePath = request.getStringParameter(FILE_PATH_PARAMETER);

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
