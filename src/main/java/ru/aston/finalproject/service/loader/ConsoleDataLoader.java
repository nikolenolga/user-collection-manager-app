package ru.aston.finalproject.service.loader;

import ru.aston.finalproject.collection.CustomArrayList;
import ru.aston.finalproject.environment.AppException;
import ru.aston.finalproject.environment.AppRequest;
import ru.aston.finalproject.parser.Parsing;
import ru.aston.finalproject.util.Message;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Stream;

import static ru.aston.finalproject.parser.UserParser.USER_FORMAT;

public class ConsoleDataLoader<T> extends AbstractLoaderWithParser<T> {
    public static final String STOP_CONSOLE_LOADER_COMMAND = "q";

    public ConsoleDataLoader(Parsing<T> parser) {
        super(parser);
    }

    @Override
    public Stream<T> loadEntityList(Integer size, AppRequest request) {
        System.out.println(Message.ENTER_USERS_EXPECTED_FORMAT_S.formatted(USER_FORMAT));
        System.out.println(Message.ENTER_X_TO_INTERRUPT_CONSOLE_INPUT.formatted(STOP_CONSOLE_LOADER_COMMAND));
        List<T> loadedEntities = new CustomArrayList<>();

        try {
            return new BufferedReader(new InputStreamReader(System.in))
                    .lines()
                    .takeWhile(this::checkExitConsoleCommand)
                    .map(this::parseEntity)
                    .filter(Objects::nonNull)
                    .limit(size);

        } catch (UncheckedIOException | NoSuchElementException e) {
            throw new AppException(Message.CONSOLE_INPUT_FAILED);
        }
    }

    private boolean checkExitConsoleCommand(String line) {
        boolean equals = STOP_CONSOLE_LOADER_COMMAND.equals(line);
        if (equals) {
            System.out.println(Message.CONSOLE_INPUT_INTERRUPTED);
        }
        return !equals;
    }
}
