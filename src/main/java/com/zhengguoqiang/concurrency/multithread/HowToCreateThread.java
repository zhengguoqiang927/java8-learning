package com.zhengguoqiang.concurrency.multithread;

public class HowToCreateThread {
    static class MyThread extends Thread{
        @Override
        public void run() {
            System.out.println("Hello MyThread");
        }
    }

    static class MyRunnable implements Runnable{
        @Override
        public void run() {
            System.out.println("Hello MyRunnable");
        }
    }

    public static void main(String[] args) {
        new MyThread().start();
        new Thread(new MyRunnable()).start();
        new Thread(() -> {
            System.out.println("Hello Lambda");
        }).start();
    }
}
