package com.zhengguoqiang.concurrency.geektime;

public class ThreadInterrupt {

    public static void main(String[] args) {
        Thread thread = new Thread(() -> {
            while (true) {
                if (Thread.currentThread().isInterrupted()) break;
                System.out.println(Thread.currentThread().getName() + " is running...");
                try {
                    Thread.sleep(1_000);
                } catch (InterruptedException e) {
                    //当前线程响应中断之后会自动清除中断标志isInterrupted，所以需要在捕获异常之后重置中断标志
                    Thread.currentThread().interrupt();
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        try {
            Thread.sleep(10_000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        thread.interrupt();
    }
}
