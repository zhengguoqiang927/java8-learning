package com.zhengguoqiang.concurrency.producer;

import java.util.stream.Stream;

/**
 * @author zhengguoqiang
 */
public class ProducerConsumerMultiThread {

    private int i = 0;

    private final Object LOCK = new Object();

    private volatile boolean isProduced = false;

    public void produce(){
        synchronized (LOCK){
            while (isProduced){
                try {
//                    System.out.println("Producer Thread " + Thread.currentThread().getName() + " enter waiting...");
                    LOCK.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            i++;
            System.out.println("P->" + i);
            LOCK.notifyAll();
            isProduced = true;
        }
    }

    public void consumer(){
        synchronized (LOCK){
            if (!isProduced){
                try {
//                    System.out.println("Consumer Thread " + Thread.currentThread().getName() + " enter waiting...");
                    LOCK.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("C->" + i);
            LOCK.notifyAll();
            isProduced = false;
        }
    }

    public static void main(String[] args) {
        ProducerConsumerMultiThread pcm = new ProducerConsumerMultiThread();
        Stream.of("P1","P2","P3","P4").forEach(name -> {
            new Thread(name){
                @Override
                public void run() {
                    while (true){
                        pcm.produce();
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }.start();
        });

        Stream.of("C1","C2","C3","C4").forEach(name -> {
            new Thread(name){
                @Override
                public void run() {
                    while (true){
                        pcm.consumer();
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }.start();
        });
    }
}
