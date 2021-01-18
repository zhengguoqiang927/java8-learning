package com.zhengguoqiang.concurrency.producer;

import java.util.stream.Stream;

/**
 * @author zhengguoqiang
 */
public class ProducerConsumerModel {

    private int i = 0;

    private final Object LOCK = new Object();

    private volatile boolean isProduced = false;

    public void produce() {
        synchronized (LOCK) {
            if (isProduced) {
                try {
                    System.out.println("Producer Thread " + Thread.currentThread().getName() + " enter waiting...");
                    LOCK.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                i++;
                System.out.println("Producer P->" + i + ",Thread:" + Thread.currentThread().getName());
                LOCK.notify();
                isProduced = true;
            }
        }
    }

    public void consumer() {
        synchronized (LOCK) {
            if (isProduced) {
                System.out.println("Consumer C->" + i + ",Thread:" + Thread.currentThread().getName());
                LOCK.notify();
                isProduced = false;
            } else {
                try {
                    System.out.println("Consumer Thread " + Thread.currentThread().getName() + " enter waiting...");
                    LOCK.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        ProducerConsumerModel pcm = new ProducerConsumerModel();
        Stream.of("P1", "P2").forEach(name -> new Thread(name) {
            @Override
            public void run() {
                while (true) {
                    pcm.produce();
                }
            }
        }.start());

        Stream.of("C1", "C2").forEach(name -> {
            new Thread(name) {
                @Override
                public void run() {
                    while (true) {
                        pcm.consumer();
                    }
                }
            }.start();
        });
        //打印结果
        /*
        Producer P->1,Thread:P1
        Producer Thread P1 enter waiting...
        Producer Thread P2 enter waiting...
        Consumer C->1,Thread:C1
        Consumer Thread C1 enter waiting...
        Consumer Thread C2 enter waiting...
        Producer P->2,Thread:P1
        Producer Thread P1 enter waiting...
        Producer Thread P2 enter waiting...
         */
    }
}
