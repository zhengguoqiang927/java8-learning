package com.zhengguoqiang.java8.forkjoin;

import java.util.concurrent.RecursiveTask;

/**
 * @author zhengguoqiang
 */
public class AccumulatorRecursiveTask extends RecursiveTask<Integer> {
    private final int start;
    private final int end;
    private final int[] data;
    private final int threshold = 3;

    public AccumulatorRecursiveTask(int start, int end, int[] data) {
        this.start = start;
        this.end = end;
        this.data = data;
    }

    @Override
    protected Integer compute() {
        if (end - start < threshold) {
            int result = 0;
            for (int i = start; i < end; i++) {
                result += data[i];
            }
            return result;
        }

        int mid = (start + end) / 2;
        AccumulatorRecursiveTask left = new AccumulatorRecursiveTask(start, mid, data);
        AccumulatorRecursiveTask right = new AccumulatorRecursiveTask(mid, end, data);
        left.fork();
//        int rightResult = right.compute();
        right.fork();
        int leftResult = left.join();
        int rightResult = right.join();
        return leftResult + rightResult;
    }
}
