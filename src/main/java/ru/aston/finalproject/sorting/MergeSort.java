package ru.aston.finalproject.sorting;

import ru.aston.finalproject.entity.User;

import java.util.ArrayList;
import java.util.List;

public class MergeSort {
    private List<User> users;

    public MergeSort(List<User> users) {
        this.users = users;
        mergeSort(this.users);
    }

    private void mergeSort(List<User> list) {
        if (list.size() <= 1) return;

        int mid = list.size() / 2;
        List<User> left = new ArrayList<>(list.subList(0, mid));
        List<User> right = new ArrayList<>(list.subList(mid, list.size()));

        mergeSort(left);
        mergeSort(right);

        merge(list, left, right);
    }

    private void merge(List<User> result, List<User> left, List<User> right) {
        int i = 0;
        int j = 0;
        int k = 0;

        while (i < left.size() && j < right.size()) {
            if (left.get(i).compareTo(right.get(j)) <= 0) {
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

    @Override
    public String toString() {
        return super.toString();
    }
}
