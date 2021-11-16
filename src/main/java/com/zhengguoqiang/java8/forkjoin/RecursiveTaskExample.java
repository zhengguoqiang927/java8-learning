package com.zhengguoqiang.java8.forkjoin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

/**
 * @author zhengguoqiang
 */
public class RecursiveTaskExample extends RecursiveTask<Integer> {

    private int[] arr;

    private static final int THRESHOLD = 20;

    public RecursiveTaskExample(int[] arr) {
        this.arr = arr;
    }

    @Override
    protected Integer compute() {
        if (arr.length > THRESHOLD) {
            return ForkJoinTask.invokeAll(createSubtasks())
                    .stream()
                    .mapToInt(ForkJoinTask::join)
                    .sum();
        }
        return processing(arr);
    }

    private Collection<RecursiveTaskExample> createSubtasks() {
        List<RecursiveTaskExample> dividedTasks = new ArrayList<>();
        dividedTasks.add(new RecursiveTaskExample(
                Arrays.copyOfRange(arr, 0, arr.length / 2)));
        dividedTasks.add(new RecursiveTaskExample(
                Arrays.copyOfRange(arr, arr.length / 2, arr.length)
        ));
        return dividedTasks;
    }

    private Integer processing(int[] arr) {
        return Arrays.stream(arr)
                .filter(a -> a > 10 && a < 27)
                .map(a -> a * 10)
                .sum();
    }
}
