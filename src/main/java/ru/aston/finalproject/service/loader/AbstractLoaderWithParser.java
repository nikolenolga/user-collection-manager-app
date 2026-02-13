package ru.aston.finalproject.service.loader;

import lombok.AllArgsConstructor;
import ru.aston.finalproject.environment.AppException;
import ru.aston.finalproject.parser.Parsing;

@AllArgsConstructor
public abstract class AbstractLoaderWithParser<T> implements DataLoader<T> {
    protected final Parsing<T> parser;

    protected T parseEntity(String line) {
        try {
            return parser.parse(line);
        } catch (AppException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}
