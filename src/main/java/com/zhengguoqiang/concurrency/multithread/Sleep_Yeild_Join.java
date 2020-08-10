package com.zhengguoqiang.concurrency.multithread;

public class Sleep_Yeild_Join {
    static void testJoin(){
        Thread t1 = new Thread(()->{
            for (int i=0;i<100;i++){
                System.out.println("A" + i);
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        Thread t2 = new Thread(()->{
            try {
                //当前线程等待t1线程执行完之后在执行
                t1.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            for (int i=0;i<100;i++){
                System.out.println("B" + i);
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t1.start();
        t2.start();
    }

    static void testYield(){
        new Thread(()->{
            for (int i=0;i<1000;i++){
                System.out.println("A" + i);
                //yield表示当前线程愿意让出cpu使用权，进入就绪状态，等待调度器再次调度
                if (i%10==0) Thread.yield();
            }
        }).start();

        new Thread(()->{
            for (int i=0;i<100;i++){
                System.out.println("---------B" + i);
                if (i%10==0) Thread.yield();
            }
        }).start();
    }

    public static void main(String[] args) {
//        testYield();
        testJoin();
    }
}
