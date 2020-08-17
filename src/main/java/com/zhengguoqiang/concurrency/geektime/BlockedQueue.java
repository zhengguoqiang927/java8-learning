package com.zhengguoqiang.concurrency.geektime;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BlockedQueue<T> {
    //存放元素的素组
    private final Object[] items;

    //弹出元素的位置
    private int takeIndex;

    //插入元素的位置
    private int putIndex;

    //队列中的元素总数
    private int count;

    public BlockedQueue(int capacity){
        if (capacity <= 0){
            throw new IllegalArgumentException();
        }
        items = new Object[capacity];
    }

    public  void put(Object o) throws InterruptedException {
        while (true){
            //队列未满才执行入队操作
            if (count != items.length){
                //实际执行入队操作
                enqueue(o);
                break;
            }
            Thread.sleep(200);
        }
    }

    public Object take() throws InterruptedException {
        while (true){
            //队列不空才执行出队操作
            if (count != 0){
                return dequeue();
            }
            Thread.sleep(200);
        }
    }

    private void enqueue(Object o){
        //将对象放入putIndex指向的位置
        items[putIndex] = o;
        //putIndex向后移动一位，如果已到数组末尾，则回到开头
        if (++putIndex == items.length){
            putIndex = 0;
        }
        //元素总数加一
        count++;
    }

    private Object dequeue(){
        //取出takeIndex位置的元素，并将其置空
        Object o = items[takeIndex];
        items[takeIndex] = null;

        //takeIndex向后移动一位，如果已到数组末尾，则回到开头
        if (++takeIndex == items.length){
            takeIndex = 0;
        }
        //元素总数减一
        count--;
        return o;
    }
}
