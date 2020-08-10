package com.zhengguoqiang.concurrency.multithread;

public class Add10K {
    private long count = 0;
    private void add10k(){
        int idx = 0;
        while (idx++ < 10000){
            count += 1;
        }
    }

    public static long calc() throws InterruptedException {
        final Add10K instance = new Add10K();
        Thread a = new Thread(() -> instance.add10k());
        Thread b = new Thread(() -> instance.add10k());
        //启动线程
        a.start();
        b.start();
        //当前线程等待子线程结束
        a.join();
        b.join();
        return instance.count;
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println(calc());
    }
}
