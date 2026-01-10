package ru.aston.finalproject.service.sorting;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MergeSorter<T> {

    public List<T> sort(List<T> list) {
        return sort(list, (Comparator<T>) Comparator.naturalOrder());
    }

    public List<T> sort(List<T> list, Comparator<T> comparator) {
        if (list.size() <= 1) {
            return list;
        }

        int mid = list.size() / 2;
        List<T> left = new ArrayList<>(list.subList(0, mid));
        List<T> right = new ArrayList<>(list.subList(mid, list.size()));

        sort(left, comparator);
        sort(right, comparator);

        merge(list, left, right, comparator);

        return list;
    }

    private void merge(List<T> result, List<T> left, List<T> right,
                       Comparator<T> comparator) {
        int i = 0;
        int j = 0;
        int k = 0;

        while (i < left.size() && j < right.size()) {
            if (comparator.compare(left.get(i), right.get(j)) <= 0) {
                result.set(k++, left.get(i++));
            } else {
                result.set(k++, right.get(j++));
            }
        }
        while (i < left.size()) {
            result.set(k++, left.get(i++));
        }
        while (j < right.size()) {
            result.set(k++, right.get(j++));
        }
    }
}
