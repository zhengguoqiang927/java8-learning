package com.zhengguoqiang.concurrency.multithread;

/**
 * 线程生命周期
 *
 * @author zhengguoqiang
 */
public class ThreadLifeCycle {
    private static int index = 0;

    private static void testRunnableState() {
        Thread t = new Thread(() -> {
            System.out.println("线程状态");
            while (index < 100) {
                index++;
                System.out.println(index);
            }
        });
        t.start();
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(t.getState());
    }

    private static void testBlockedState() throws InterruptedException {
        Thread t1 = new Thread(new DemoThreadB());
        Thread t2 = new Thread(new DemoThreadB());

        t1.start();
        t2.start();

        Thread.sleep(1000);
        System.out.println(t2.getState());
    }

    private static class DemoThreadB implements Runnable {

        @Override
        public void run() {
            commonResource();
        }

        private static synchronized void commonResource() {
            try {
                System.out.println("当前线程：" + Thread.currentThread().getName() + " 开始执行同步方法");
                Thread.sleep(60_000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static void testWaitingState() {
        WaitingState.t1 = new Thread(new WaitingState());
        WaitingState.t1.start();
    }

    private static class WaitingState implements Runnable {
        public static Thread t1;

        private final Object object = new Object();

        @Override
        public void run() {
            Thread t = new Thread(new DemoThreadWS());
            t.start();

            try {
                //wait(),join(),LockSupport.park();
//                t.join();
                synchronized (object) {
                    object.wait(5000);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Thread interrupted " + Thread.currentThread().getName());
                e.printStackTrace();
            }
        }
    }

    private static class DemoThreadWS implements Runnable {
        @Override
        public void run() {
            try {
                Thread.sleep(3_000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Thread interrupted " + Thread.currentThread().getName());
                e.printStackTrace();
            }
            System.out.println("The Current Thread's state is " + WaitingState.t1.getState());
        }
    }


    private static void testTimedWaiting() throws InterruptedException {
        Thread t = new Thread(new DemoThreadTimedWaiting());
        t.start();

        // The following sleep will give enough time for ThreadScheduler
        // to start processing of thread t
        Thread.sleep(1000);
        System.out.println(t.getState());
    }

    private static class DemoThreadTimedWaiting implements Runnable {
        @Override
        public void run() {
            try {
                //5种方式使线程进入Timed Waiting状态
                //1. sleep(long millis)
                //2. wait(long millis) || wait(long millis,int nanos)
                //3. join(long millis) || join(long millis,int nanos)
                //4. LockSupport.parkNanos()
                //5. LockSupport.parkUntil()
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
//        RunnableState();
//        testBlockedState();
        testWaitingState();
//        testTimedWaiting();
    }
}
