package com.zhengguoqiang.concurrency.geektime;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Account {
    private int balance;

    //方案一
    //缺点：需要保证传入的lock必须是同一个对象，如果不是同一个对象转账操作不在线程安全
    private Object lock;

    private Account() {
    }

    // 创建Account时传入同一个lock对象
    public Account(Object lock) {
        this.lock = lock;
    }

    //转账
    void transfer1(Account target, int amt) {
        //所有对象共享一把锁，所有对象的转账操作串行执行
        synchronized (lock) {
            if (this.balance > amt) {
                this.balance -= amt;
                target.balance += amt;
            }
        }
    }

    //方案二
    //解决方案一中锁的唯一性，采用Account.class来替代，Account.class是所有Account对象共享的
    //缺点：所有账户的转账操作依然是串行的
    void transfer2(Account target, int amt) {
        synchronized (Account.class) {
            if (this.balance > amt) {
                this.balance -= amt;
                target.balance += amt;
            }
        }
    }

    //方案三
    //采用细粒度锁，只对涉及转账的两个账户上锁
    //缺点：容易造成死锁
    //造成死锁的四个原因：
    //1.互斥 2.占有且等待 3.不可抢占 4.循环等待
    void transfer3(Account target, int amt) {
        synchronized (this) {
            synchronized (target) {
                if (this.balance > amt) {
                    this.balance -= amt;
                    target.balance += amt;
                }
            }
        }
    }

    //方案四
    //破坏死锁的占有且等待条件：一次性获取所有依赖的资源才执行临界区
    private String name;

    public Account(String name, int balance) {
        this.name = name;
        this.balance = balance;
    }

    void transfer4(Account target, int amt) {
        System.out.println(this.name + " 给 " + target.name + " 转账：" + amt + "元");
        Allocator allocator = Allocator.getInstance();
//        while (!allocator.apply(this, target)) {
//            System.out.println(this.name + " 给 " + target.name + " 转账暂时不满足条件，继续申请...");
//            try {
//                Thread.sleep(1);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
        allocator.apply(this,target);
        System.out.println(this.name + " 和 " + target.name + " 账户达到可转账状态！！！");
        try {
            synchronized (this) {
                synchronized (target) {
                    if (this.balance > amt) {
                        this.balance -= amt;
                        target.balance += amt;
                        System.out.println(this.name + "账户余额：" + this.balance + "," + target.name + "账户月：" + target.balance);
                    }
                }
            }
        } finally {
            allocator.free(this, target);
        }
    }

    //方案五
    //破坏死锁的循环等待条件：通过对资源排序，然后按照从小到大的顺序申请资源。
    private int id;
    void transfer5(Account target,int amt){
        Account left = this;
        Account right = target;
        if (this.id > target.id){
            left = target;
            right = this;
        }
        synchronized (left){
            synchronized (right){
                if (this.balance > amt){
                    this.balance -= amt;
                    target.balance += amt;
                }
            }
        }
    }

    public static void main(String[] args) {
        Random random = new Random();
        List<Account> accounts = IntStream.rangeClosed(0, 3).mapToObj(i -> new Account("Account" + i, 1000)).collect(Collectors.toList());
        for (int i = 0; i < 100; i++) {
            new Thread(() -> {
                Account a = accounts.get(random.nextInt(4));
                Account b = accounts.get(random.nextInt(4));
                while (a == b) {
                    System.out.println("生成的a和b账户是同一个，需再次生成...");
                    b = accounts.get(random.nextInt(4));
                }
                a.transfer4(b, 10);
            }).start();
        }
    }
}

//统一管理账户资源的管理员
class Allocator {
    private static final Allocator instance = new Allocator();

    private Allocator() {
    }

    public static Allocator getInstance() {
        return instance;
    }

    private final List<Object> als = new ArrayList<>();

    //一次性申请所有资源
//    synchronized boolean apply(Object from, Object to) {
//        if (als.contains(from) || als.contains(to)) {
//            return false;
//        } else {
//            als.add(from);
//            als.add(to);
//        }
//        return true;
//    }

    //释放资源
//    synchronized void free(Object from, Object to) {
//        als.remove(from);
//        als.remove(to);
//    }

    //等待-通知机制优化
    synchronized void apply(Object from,Object to){
        while (als.contains(from) || als.contains(to)){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        als.add(from);
        als.add(to);
    }

    synchronized void free(Object from,Object to){
        als.remove(from);
        als.remove(to);
        //尽量使用notifyAll()；notify()会随机的唤醒等待队列中的一个线程，notifyAll()会唤醒等待队列中的所有线程。
        notifyAll();
    }
}
