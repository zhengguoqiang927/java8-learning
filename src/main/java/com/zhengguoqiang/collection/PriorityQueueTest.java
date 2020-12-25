package com.zhengguoqiang.collection;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.PriorityBlockingQueue;

//https://www.javazhiyin.com/25500.html

public class PriorityQueueTest {

    PriorityBlockingQueue<Double> queue;

    int thresold;


    class Task implements Runnable{

        private double num;

        public Task(double num) {
            this.num = num;
        }

        @Override
        public void run() {
            queue.add(num);
            if (queue.size() > thresold){
//                System.out.println("Thread:" + Thread.currentThread().getName() + ",size:" + queue.size() + ",num:" + num);
                queue.poll();
            }
        }
    }

    public void process(int capacity){
        long startTime = System.currentTimeMillis();
        List<Double> result = new ArrayList<>(capacity);
        Random random = new Random();
        thresold = capacity;
        queue = new PriorityBlockingQueue<>(capacity);

        List<CompletableFuture<Void>> list = new ArrayList<>();

        for (int i = 0;i<20_000_000;i++){
            list.add(CompletableFuture.runAsync(new Task(random.nextDouble())));
        }

        CompletableFuture.allOf(list.toArray(new CompletableFuture[0])).join();

        System.out.println("size:" + queue.size());
        Double d = null;
        while ((d = queue.poll()) != null){
            result.add(d);
        }
        Collections.reverse(result);
        System.out.println("time:" + (System.currentTimeMillis() - startTime));
        result.forEach(System.out::println);
    }



    public static void main(String[] args) {
        PriorityQueueTest test = new PriorityQueueTest();
        test.process(10);
    }
}
