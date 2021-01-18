package com.zhengguoqiang.concurrency.geektime;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 阻塞队列
 * 优化流程：
 * 1.while(true)：消耗CPU
 * 2.wait-notify等待通知机制实现：解决1中消耗CPU的问题，但是notifyAll()引起所有等待的线程被唤起，包括生产者线程和消费者线程
 * 3.lock-condition多个条件变量优化：同一个lock，两个条件变量notFull和notEmpty，这样只会唤醒指定类别的线程
 * 4.细粒度锁实现：3中生产者线程和消费者线程公用统一把锁lock，可将其拆分成putLock和takeLock两把锁，并分别创建条件变量，但要注意死锁的风险
 * 5.细节优化：只有队列为空或者已满时才会进行阻塞唤醒操作，平时不会进行这些操作只会正常生产和消费
 */
public class BlockedQueue {
    //存放元素的素组
    private final Object[] items;

    //弹出元素的位置
    private int takeIndex;

    //插入元素的位置
    private int putIndex;

    //队列中的元素总数
//    private int count;
    private final AtomicInteger count = new AtomicInteger(0);

//    private final ReentrantLock lock = new ReentrantLock();

    //插入锁
    private final ReentrantLock putLock = new ReentrantLock();
    //弹出锁
    private final ReentrantLock takeLock = new ReentrantLock();

    //    private final Condition condition = lock.newCondition();
    //条件变量：队列不满
//    private final Condition notFull = lock.newCondition();
    private final Condition notFull = putLock.newCondition();
    //条件变量：队列不空
//    private final Condition notEmpty = lock.newCondition();
    private final Condition notEmpty = takeLock.newCondition();

    public BlockedQueue(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException();
        }
        items = new Object[capacity];
    }

    /**
     * 唤醒等待队列不满的线程即生产者线程
     */
    private void signalNotFull() {
        putLock.lock();
        try {
            //唤醒一个等待不满的生产者线程
            notFull.signal();
        } finally {
            putLock.unlock();
        }
    }

    /**
     * 唤醒等待队列非空线程即消费者线程
     */
    private void signalNotEmpty() {
        //唤醒消费者线程需要先获取弹出锁takeLock
        takeLock.lock();
        try {
            //唤醒一个等待不空的消费者线程
            notEmpty.signal();
        } finally {
            takeLock.unlock();
        }
    }

    public void put(Object o) throws InterruptedException {
//        synchronized (this)
        putLock.lockInterruptibly();
        try {
            while (count.get() == items.length) {
//                this.wait();
//                condition.await();
                notFull.await();
            }

            //实际执行入队操作
            enqueue(o);

            //唤醒所有等待消费的线程
//            this.notifyAll();
//            condition.signalAll();
//            notEmpty.signalAll();
        } finally {
            putLock.unlock();
        }
        //唤醒等待队列不空的消费者线程
        //为了防止死锁，不能在释放putLock之前获取takeLock
        signalNotEmpty();
    }

    public Object take() throws InterruptedException {
//        synchronized (this)
        takeLock.lockInterruptibly();
        Object e = null;
        try {
            //队列不空才执行出队操作
            while (count.get() == 0) {
//                this.wait();
//                condition.await();
                notEmpty.await();
            }
            e = dequeue();
            //唤醒等待生产的线程
//            this.notifyAll();
//            condition.signalAll();
//            notFull.signalAll();

        } finally {
            takeLock.unlock();
        }

        //唤醒等待队列不满的生产者线程
        signalNotFull();
        return e;
    }

    private void enqueue(Object o) {
        //将对象放入putIndex指向的位置
        items[putIndex] = o;
        //putIndex向后移动一位，如果已到数组末尾，则回到开头
        if (++putIndex == items.length) {
            putIndex = 0;
        }
        //元素总数加一
        count.getAndIncrement();
    }

    private Object dequeue() {
        //取出takeIndex位置的元素，并将其置空
        Object o = items[takeIndex];
        items[takeIndex] = null;

        //takeIndex向后移动一位，如果已到数组末尾，则回到开头
        if (++takeIndex == items.length) {
            takeIndex = 0;
        }
        //元素总数减一
        count.getAndDecrement();
        return o;
    }


    public static void main(String[] args) throws InterruptedException {
        //创建一个大小为2的阻塞队列
        final BlockedQueue queue = new BlockedQueue(2);

        //生产者消费者线程各创建2个
        final int threads = 400;

        //每个线程执行10次
        final int times = 100;

        //线程列表，用于等待所有线程完成
        List<Thread> threadList = new ArrayList<>(threads * 2);
        long startTime = System.currentTimeMillis();

        // 创建2个生产者线程，向队列中并发放入数字0到19，每个线程放入10个数字
        for (int i = 0; i < threads; i++) {
            final int offset = i * times;
            Thread producer = new Thread(() -> {
                for (int j = 0; j < times; j++) {
                    try {
                        queue.put(offset + j);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            threadList.add(producer);
            producer.start();
        }

        //创建2个消费者线程，从队列中弹出20次数字并打印
        for (int i = 0; i < threads; i++) {
            Thread consumer = new Thread(() -> {
                try {
                    for (int j = 0; j < times; j++) {
                        Integer integer = (Integer) queue.take();
                        System.out.println(integer);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            threadList.add(consumer);
            consumer.start();
        }

        for (Thread thread : threadList) {
            thread.join();
        }

        long endTime = System.currentTimeMillis();
        System.out.println(String.format("总耗时：%.2fs", (endTime - startTime) / 1e3));
    }
}
