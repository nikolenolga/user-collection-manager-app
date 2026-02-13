package ru.aston.finalproject.service.filters;

import java.util.function.Predicate;

public interface FilterList<T> {

    FilterList<T> filter(Predicate<T> predicate);
}
