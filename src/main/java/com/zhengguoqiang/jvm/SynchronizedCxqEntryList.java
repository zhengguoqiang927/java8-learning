package com.zhengguoqiang.jvm;

public class SynchronizedCxqEntryList {
    private static final Object lock = new Object();

    public void startThreadA(){
        new Thread(()->{
            synchronized (lock){
                System.out.println("A get lock.");
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("A release lock.");
            }
        },"Thread-A").start();
    }

    public void startThreadB(){
        new Thread(()->{
            synchronized (lock){
                System.out.println("B get lock.");
            }
        },"Thread-B").start();
    }

    public void startThreadC(){
        new Thread(()->{
            synchronized (lock){
                System.out.println("C get lock.");
            }
        },"Thread-B").start();
    }

    public static void main(String[] args) {
        SynchronizedCxqEntryList demo = new SynchronizedCxqEntryList();
        demo.startThreadA();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        demo.startThreadB();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        demo.startThreadC();
    }
}
