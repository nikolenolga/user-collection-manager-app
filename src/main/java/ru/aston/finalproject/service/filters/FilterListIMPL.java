package ru.aston.finalproject.service.filters;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@AllArgsConstructor
public class FilterListIMPL<T> implements FilterList<T> {

    @Getter
    private List<T> list;

    @Override
    public FilterListIMPL<T> filter(Predicate<T> predicate) {
        list = list.stream().filter(predicate).collect(Collectors.toList());
        return this;
    }
}
