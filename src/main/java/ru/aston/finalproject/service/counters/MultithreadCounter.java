package ru.aston.finalproject.service.counters;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import ru.aston.finalproject.config.AppException;
import ru.aston.finalproject.util.Message;

public class MultithreadCounter <T> {
    public int count(
           List<T> list,
           T target,
           int threadCount
    ) throws AppException {
        if (threadCount < 1) {
            throw new AppException(
                    Message.EXCEPTION_BAD_THREAD_COUNT
            );
        }

        int step = list.size() / threadCount;
        if (threadCount == 1 || step < 1) {
           return count(list, target);
        }

        int returnValue;
        // ExecutorService не является AutoCloseable до 19й версии java.
        ExecutorService tasks = Executors.newFixedThreadPool(threadCount);
        try {
            List<Future<Integer>> results = new ArrayList<>();
            for (int multiplier = 0; multiplier < threadCount - 1; multiplier++) {
                int finalMultiplier = multiplier;
                results.add(tasks.submit(
                        () -> count(
                                list.subList(step * finalMultiplier, step * (finalMultiplier + 1)),
                                target
                        )
                ));
            }
            results.add(tasks.submit(
                    () -> count(
                            list.subList(step * (threadCount - 1), list.size()),
                            target
                    )
            ));

            returnValue = 0;
            for (Future<Integer> value : results) {
                returnValue += value.get();
            }
        } catch (ExecutionException|InterruptedException e) {
            throw new AppException(String.format(
                    Message.EXCEPTION_THREAD_FAILED, e.getMessage()
            ));
        } finally {
            tasks.shutdown();
        }
        return returnValue;
    }

    public int count(
           List<T> list,
           T target
    ) {
        int count = 0;
        for (T item : list) {
            if (item.equals(target)) {
                count++;
            }
        }
        return count;
    }
}
