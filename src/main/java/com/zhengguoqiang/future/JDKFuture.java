package com.zhengguoqiang.future;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author zhengguoqiang
 */
public class JDKFuture {

    public static void main(String[] args)
            throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<String> future = executorService.submit(() -> {
            try {
                Thread.sleep(5000);
                return "I am finished";
            } catch (InterruptedException e) {
                return "Error";
            }
        });

        while (!future.isDone()){
            Thread.sleep(10);
        }
        System.out.println(future.get());
        //The threads in the pool will exist
        // until it is explicitly {@link ExecutorService#shutdown shutdown}.
        //关闭线程池
        executorService.shutdown();
    }
}
