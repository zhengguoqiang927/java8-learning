package com.zhengguoqiang.senior;

/**
 * False sharing is a situation that can cause performance degradations when two or more threads write to different variables located within the same CPU cache line.
 * 伪共享就是一种特殊的场景，在该场景中两个或者多个线程会对同一个缓存行内的两个不同变量频繁更新，从而导致性能下降
 */
public class FalseSharingExample {

    public static void main(String[] args) {
        Counter counter1 = new Counter();
        Counter counter2 = counter1;
//        Counter counter2 = new Counter();

        long iterations = 1_000_000_000;

        Thread thread1 = new Thread(() -> {
            long startTime = System.currentTimeMillis();
            for (long i=0;i<iterations;i++){
                counter1.count1++;
            }
            long endTime = System.currentTimeMillis();
            System.out.println("total time: " + (endTime - startTime));
        });

        Thread thread2 = new Thread(() -> {
           long startTime = System.currentTimeMillis();
           for (long i=0;i<iterations;i++){
               counter2.count2++;
           }
           long endTime = System.currentTimeMillis();
            System.out.println("total time: " + (endTime - startTime));
        });

        thread1.start();
        thread2.start();
    }
}
