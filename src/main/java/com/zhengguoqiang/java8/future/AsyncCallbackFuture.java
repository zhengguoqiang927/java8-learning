package com.zhengguoqiang.java8.future;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author zhengguoqiang
 */
public class AsyncCallbackFuture {

    public static void main(String[] args) {
        Future<String> future = invoke(() -> {
            try {
                Thread.sleep(5000);
                return "I am finished";
            } catch (Exception e) {
                return "Error";
            }
        });
        future.setCompletable(new Completable<String>() {
            @Override
            public void complete(String value) {
                System.out.println(value);
            }

            @Override
            public void exception(Throwable cause) {
                System.out.println("Error");
                cause.printStackTrace();
            }
        });
    }

    private static <T> Future<T> invoke(Callable<T> callable) {
        AtomicReference<T> reference = new AtomicReference<>();
        AtomicBoolean status = new AtomicBoolean(false);
        Future<T> future = new Future<T>() {
            private Completable<T> completable;

            @Override
            public T get() {
                return reference.get();
            }

            @Override
            public boolean isDone() {
                return status.get();
            }

            @Override
            public void setCompletable(Completable<T> completable) {
                this.completable = completable;
            }

            @Override
            public Completable<T> getCompletable() {
                return completable;
            }
        };
        Thread t = new Thread(() -> {
            try {
                T value = callable.action();
                reference.set(value);
                status.set(true);
                if (future.getCompletable() != null)
                    //回调函数
                    future.getCompletable().complete(value);
            } catch (Throwable cause) {
                if (future.getCompletable() != null)
                    future.getCompletable().exception(cause);
            }
        });
        t.start();

        return future;
    }

    private interface Future<T> {
        T get();

        boolean isDone();

        void setCompletable(Completable<T> completable);

        Completable<T> getCompletable();
    }

    private interface Callable<T> {
        T action();
    }

    private interface Completable<T> {
        void complete(T value);

        void exception(Throwable cause);
    }
}
