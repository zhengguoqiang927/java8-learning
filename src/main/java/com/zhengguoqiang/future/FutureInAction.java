package com.zhengguoqiang.future;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author zhengguoqiang
 */
public class FutureInAction {

    public static void main(String[] args) throws InterruptedException {

        Future<String> future = invoke(() -> {
            try {
                Thread.sleep(10000);
                return "I am finished";
            } catch (InterruptedException e) {
                return "Error";
            }
        });
        System.out.println(future.get());
        System.out.println(future.get());
        System.out.println(future.get());
        System.out.println("process other business logic");
        while (!future.isDone()){
            Thread.sleep(10);
        }
        System.out.println(future.get());
    }

    private static <T> Future<T> invoke(Callable<T> callable){
        AtomicReference<T> reference = new AtomicReference<>();
        AtomicBoolean status = new AtomicBoolean(false);
        Thread t = new Thread(()->{
            T value = callable.action();
            reference.set(value);
            status.set(true);
        });
        t.start();

        return new Future<T>() {
            @Override
            public T get() {
                return reference.get();
            }

            @Override
            public boolean isDone() {
                return status.get();
            }
        };
    }

    private interface Future<T>{
        T get();
        boolean isDone();
    }

    private interface Callable<T>{
        T action();
    }
}
