package com.zhengguoqiang.java8.forkjoin;

import java.util.concurrent.ForkJoinPool;

/**
 * @author zhengguoqiang
 */
public class Main {

    private static void customRecursiveAction() {
        String s = "i'm learning java 8 fork/join framework";
        RecursiveActionExample example =
                new RecursiveActionExample(s);
        example.compute();
    }

    private static void customRecursiveTask() {
        int[] arr = {1, 2, 10, 3, 4, 5, 8, 9, 5, 2, 0, 3, 3, 5, 2, 100, 1000, 30000,
                1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,20};
        RecursiveTaskExample example = new RecursiveTaskExample(arr);
        System.out.println("The result is : " + example.compute());
    }

    public static void main(String[] args) {
//        customRecursiveAction();
//        customRecursiveTask();
        accumulatorTest();
    }

    public static void accumulatorTest(){
        int[] data = {1,2,3,4,5,6,7,8,9,10};
        AccumulatorRecursiveTask task = new AccumulatorRecursiveTask(0,data.length,data);
        ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();
        Integer result = forkJoinPool.invoke(task);
        System.out.println("AccumulatorRecursiveTask result >> " + result);

        AccumulatorRecursiveAction action = new AccumulatorRecursiveAction(0,data.length,data);
        forkJoinPool.invoke(action);
        System.out.println("AccumulatorRecursiveAction result >> " + AccumulatorRecursiveAction.AccumulatorHelper.getResult());
    }
}
