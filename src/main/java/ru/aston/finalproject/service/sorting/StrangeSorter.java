package ru.aston.finalproject.service.sorting;

import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

@AllArgsConstructor
public class StrangeSorter<T> {
    private final MergeSorter<T> mergeSorter;

    public List<T> sort(List<T> list, Function<T, Integer> getIntegerField) {
        List<T> evens = new ArrayList<>();
        List<Integer> sequentialPositionsOfEvens = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            T reference = list.get(i);
            if (
                    getIntegerField.apply(reference) % 2 == 0
            ) {
                evens.add(reference);
                sequentialPositionsOfEvens.add(i);
            }
        }

        mergeSorter.sort(evens, Comparator.comparing(getIntegerField));

        for (int i = 0; i < sequentialPositionsOfEvens.size(); i++) {
            list.set(
                    sequentialPositionsOfEvens.get(i),
                    evens.get(i)
            );
        }

        return list;
    }
}
