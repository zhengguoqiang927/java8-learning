package com.zhengguoqiang.concurrency.multithread;

/**
 * @author zhengguoqiang
 */
public class TicketWindowRunnable implements Runnable {

    private int index = 1;

    private final int MAX = 500;

    @Override
    public void run() {
        while (index <= MAX){
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() +"当前的号码：" + index++);
        }
    }
}
