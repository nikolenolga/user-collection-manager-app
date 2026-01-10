package ru.aston.finalproject.service.loader;

import ru.aston.finalproject.config.AppException;
import ru.aston.finalproject.config.AppRequest;
import ru.aston.finalproject.parser.Parsing;
import ru.aston.finalproject.util.Message;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static ru.aston.finalproject.util.ConstantFields.USER_FORMAT;

public class ConsoleDataLoader<T> implements DataLoader<T> {
    private final Parsing<T> parser;

    public ConsoleDataLoader(Parsing<T> parser) {
        this.parser = parser;
    }

    @Override
    public List<T> loadEntityList(Integer size, AppRequest request) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.printf(Message.ENTER_USERS_EXPECTED_FORMAT_S_N.formatted(USER_FORMAT));

            return reader.lines()
                    .limit(size)
                    .map(parser::parse)
                    .collect(Collectors.toList());
        } catch (UncheckedIOException | NoSuchElementException e) {
            throw new AppException(Message.EXCEPTION_WRONG_CONSOLE_INPUT);
        }
    }
}
